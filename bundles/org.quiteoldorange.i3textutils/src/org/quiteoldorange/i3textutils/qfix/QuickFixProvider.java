package org.quiteoldorange.i3textutils.qfix;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.StringUtils;
import org.quiteoldorange.i3textutils.qfix.movemethodtoregion.CandidateRegion;
import org.quiteoldorange.i3textutils.qfix.movemethodtoregion.MoveMethodToRegionDialog;
import org.quiteoldorange.i3textutils.refactoring.MethodSourceInfo;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.RegionPreprocessor;
import com._1c.g5.v8.dt.bsl.model.util.BslUtil;
import com._1c.g5.v8.dt.bsl.ui.quickfix.AbstractExternalQuickfixProvider;

public class QuickFixProvider
    extends AbstractExternalQuickfixProvider
{

    /**
     * @author ozolotarev
     *
     */
    private final class BadRegionIssueResolver
        implements IModification
    {
        /**
         *
         */
        private final Issue mIssue;

        /**
         * @param issue
         */
        private BadRegionIssueResolver(Issue issue)
        {
            mIssue = issue;
        }

        @Override
        public void apply(IModificationContext context) throws Exception
        {
            var doc = context.getXtextDocument();
            var module = Utils.getModuleFromXTextDocument(doc);

            List<RegionPreprocessor> items = BslUtil.getAllRegionPreprocessors(module);

            List<CandidateRegion> candidates = makeCandidatesList(items, mIssue);

            var dlg = new MoveMethodToRegionDialog(candidates);
            dlg.open();

            CandidateRegion regionDesc = dlg.getSelectedRegion();

            if (regionDesc == null)
                return;

            String methodName = StringUtils.parseMethodFromURIToProblem(mIssue.getUriToProblem().toString());
            Method method = Utils.findModuleMethod(methodName, module);
            MethodSourceInfo info = Utils.getMethodSourceInfo(method, doc);

            doc.replace(info.getStartOffset(), info.getLength(), "");

            int targetOffset = 0;
            int replaceLength = 0;

            if (!regionDesc.isExists())
            {
                StringBuilder builder = new StringBuilder();

                builder.append(String.format("#Область %s\n\n", regionDesc.getName()));
                builder.append("// %MOVE_METHOD%\n\n");
                builder.append(String.format("#КонецОбласти // %s\n\n", regionDesc.getName()));

                doc.replace(0, 0, builder.toString());

                targetOffset = doc.get().indexOf("// %MOVE_METHOD%\n");
                replaceLength = "// %MOVE_METHOD%".length();
            }
            else
            {
                targetOffset = regionDesc.getOffset();
                replaceLength = 0;
            }

            doc.replace(targetOffset, replaceLength, info.getSourceText());

        }

        private List<CandidateRegion> makeCandidatesList(List<RegionPreprocessor> items, Issue issue)
        {

            List<CandidateRegion> result = new LinkedList<>();

            for (var existingItem : items)
            {
                result.add(new CandidateRegion(existingItem));
            }

            List<String> newRegions = parseSuggestedRegions(items, issue);

            for (var newItem : newRegions)
            {
                result.add(new CandidateRegion(newItem, false));
            }

            return result;
        }

        private boolean hasRegionInList(List<RegionPreprocessor> items, String name)
        {
            for (var item : items)
            {
                if (item.getName().equals(name))
                    return true;
            }

            return false;
        }

        private List<String> parseSuggestedRegions(List<RegionPreprocessor> items, Issue issue)
        {

            List<String> result = new LinkedList<>();
            String issueMessage = issue.getMessage();
            String regionsList = issueMessage.substring(issueMessage.indexOf(':') + 1);

            String[] suggestedRegions = regionsList.split(",");

            for (String item : suggestedRegions)
            {
                String trimmed = item.trim();

                if (hasRegionInList(items, trimmed))
                    continue;

                result.add(trimmed);
            }

            return result;
        }
    }

    public QuickFixProvider()
    {
        // TODO Auto-generated constructor stub
    }

    @Fix("SU39999")
    public void Shit(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        acceptor.accept(issue, "Переместить метод в область", "", null, new BadRegionIssueResolver(issue));
    }

}
