package org.quiteoldorange.i3textutils.qfix;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
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
            List<CandidateRegion> candidates = makeCandidatesList(items, mIssue, doc);

            var dlg = new MoveMethodToRegionDialog(candidates);
            dlg.open();

            CandidateRegion regionDesc = dlg.getSelectedRegion();

            if (regionDesc == null)
                return;

            String methodName = StringUtils.parseMethodFromURIToProblem(mIssue.getUriToProblem().toString());
            Method method = Utils.findModuleMethod(methodName, module);
            MethodSourceInfo info = Utils.getMethodSourceInfo(method, doc);

            int targetOffset = 0;
            int replaceLength = 0;

            if (!regionDesc.isExists())
            {
                StringBuilder builder = new StringBuilder();

                builder.append(String.format("#Область %s\n", regionDesc.getName()));
                builder.append("\n" + info.getSourceText().trim() + "\n\n");
                builder.append(String.format("#КонецОбласти // %s\n\n", regionDesc.getName()));

                doc.replace(info.getStartOffset(), info.getLength(), "");
                doc.replace(0, 0, builder.toString());
                return;
            }
            else
            {
                targetOffset = regionDesc.getOffset();
                replaceLength = 0;
            }

            // Если исходное тело модуля до области - надо сначала убрать тело модуля
            // затем поместить его в область, и соответственно наоборот, иначе смещения будут неактуальные
            // и все поплывет :(
            if (info.getStartOffset() > targetOffset)
            {
                doc.replace(info.getStartOffset(), info.getLength(), "");
                moveMethodBodyToTarget(doc, info, targetOffset, replaceLength);
            }
            else
            {
                moveMethodBodyToTarget(doc, info, targetOffset, replaceLength);
                doc.replace(info.getStartOffset(), info.getLength(), "");
            }

        }

        /**
         * @param doc
         * @param info
         * @param targetOffset
         * @param replaceLength
         * @throws BadLocationException
         */
        private void moveMethodBodyToTarget(IXtextDocument doc, MethodSourceInfo info, int targetOffset,
            int replaceLength) throws BadLocationException
        {
            if (replaceLength > 0)
            {
                doc.replace(targetOffset, replaceLength, info.getSourceText());
            }
            else
            {
                doc.replace(targetOffset, replaceLength, "\n" + info.getSourceText() + "\n\n");
            }
        }

        private List<CandidateRegion> makeCandidatesList(List<RegionPreprocessor> items, Issue issue,
            IXtextDocument doc)
        {

            List<CandidateRegion> result = new LinkedList<>();

            for (var existingItem : items)
            {
                result.add(new CandidateRegion(existingItem, doc));
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

    @Fix("SU391")
    public void Shit(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        acceptor.accept(issue, "Переместить метод в область", "", null, new BadRegionIssueResolver(issue));
    }

}
