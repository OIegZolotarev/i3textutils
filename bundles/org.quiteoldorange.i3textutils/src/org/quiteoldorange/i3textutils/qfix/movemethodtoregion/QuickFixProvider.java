package org.quiteoldorange.i3textutils.qfix.movemethodtoregion;

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
import org.quiteoldorange.i3textutils.refactoring.MethodSourceInfo;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.RegionPreprocessor;
import com._1c.g5.v8.dt.bsl.model.util.BslUtil;
import com._1c.g5.v8.dt.bsl.ui.quickfix.AbstractExternalQuickfixProvider;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

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
        private List<String> mIssueSuggestedRegions;
        private List<String> mInvalidRegionsForSuggestion;

        /**
         * @param issue
         */
        private BadRegionIssueResolver(Issue issue, List<String> issueSugestedRegions,
            List<String> invalidRegionsForSuggestion)
        {
            mIssue = issue;
            mIssueSuggestedRegions = issueSugestedRegions;
            mInvalidRegionsForSuggestion = invalidRegionsForSuggestion;
        }

        @Override
        public void apply(IModificationContext context) throws Exception
        {
            var doc = context.getXtextDocument();
            var module = Utils.getModuleFromXTextDocument(doc);

            List<RegionPreprocessor> existingRegions = BslUtil.getAllRegionPreprocessors(module);
            List<CandidateRegion> candidates = makeCandidatesList(existingRegions, doc);

            String methodName = StringUtils.parseMethodFromURIToProblem(mIssue.getUriToProblem().toString());

            var dlg = new RegionChooserDialog(candidates, methodName, mIssue.getMessage());
            dlg.open();

            CandidateRegion regionDesc = dlg.getSelectedRegion();

            if (regionDesc == null)
                return;

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

        private List<CandidateRegion> makeCandidatesList(List<RegionPreprocessor> items, IXtextDocument doc)
        {

            List<CandidateRegion> result = new LinkedList<>();

            for (var existingItem : items)
            {
                if (mInvalidRegionsForSuggestion.stream().anyMatch(s -> s.equals(existingItem.getName())))
                    continue;

                result.add(new CandidateRegion(existingItem, doc));
            }

            for (var newItem : mIssueSuggestedRegions)
            {
                if (mInvalidRegionsForSuggestion.stream().anyMatch(s -> s.equals(newItem)))
                    continue;

                if (hasRegionInList(items, newItem))
                    continue;

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

    }

    public QuickFixProvider()
    {
        // TODO Auto-generated constructor stub
    }

    private List<String> parseSuggestedRegions_ModuleStructureMethodInRegions(Issue issue)
    {

        List<String> result = new LinkedList<>();
        String issueMessage = issue.getMessage();
        String regionsList = issueMessage.substring(issueMessage.indexOf(':') + 1);

        String[] suggestedRegions = regionsList.split(",");

        for (String item : suggestedRegions)
        {
            String trimmed = item.trim();
            result.add(trimmed);
        }

        return result;
    }

    @Fix("DUMMY")
    public void fixModuleStructureFormEventRegions(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        // TODO: fixme
        ScriptVariant scriptVariant = ScriptVariant.RUSSIAN;

        var suggestions = SuggestedRegionsComputer.parseIssue(issue,
            SuggestedRegionsComputer.METHOD_SHOULD_NOT_BE_IN_THIS_REGION, scriptVariant);

        if (suggestions != null)
        {
            acceptor.accept(issue, "Переместить метод в другую область...", "", null,
                new BadRegionIssueResolver(issue, suggestions.getRecommendedRegions(), suggestions.getBadRegions()));

            return;
        }

        suggestions = SuggestedRegionsComputer.parseIssue(issue,
            SuggestedRegionsComputer.METHOD_SHOULD_IN_SPECIFIED_REGION, scriptVariant);

        if (suggestions != null)
        {
            var recommendedRegions = suggestions.getRecommendedRegions();
            assert (recommendedRegions.size() > 0);

            String suggestedRegion = recommendedRegions.get(0);

            acceptor.accept(issue, String.format("Переместить метод в область \"%s\"", suggestedRegion), "<Описание>",
                null,
                new BadRegionIssueResolver(issue, suggestions.getRecommendedRegions(), suggestions.getBadRegions()));

            return;
        }

    }

    @Fix("DUMMY")
    public void fixModuleStructureMethodInRegions(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        var suggestedRegions = parseSuggestedRegions_ModuleStructureMethodInRegions(issue);

        acceptor.accept(issue, "SU 39 Переместить метод в область", "<Описание>", null,
            new BadRegionIssueResolver(issue, suggestedRegions, new LinkedList<>()));
    }

}
