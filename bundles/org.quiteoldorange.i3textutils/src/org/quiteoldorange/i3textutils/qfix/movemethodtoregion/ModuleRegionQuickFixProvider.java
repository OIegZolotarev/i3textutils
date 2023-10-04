package org.quiteoldorange.i3textutils.qfix.movemethodtoregion;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

import com._1c.g5.v8.dt.bsl.ui.quickfix.AbstractExternalQuickfixProvider;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

public class ModuleRegionQuickFixProvider
    extends AbstractExternalQuickfixProvider
{

    public ModuleRegionQuickFixProvider()
    {
        // TODO Auto-generated constructor stub
    }

    private List<String> parseSuggestedRegions_ModuleStructureMethodInRegions(Issue issue)
    {

        List<String> result = new LinkedList<>();
        String issueMessage = issue.getMessage();
        String regionsList = issueMessage.substring(issueMessage.indexOf(':') + 1);

        String[] suggestedRegions = regionsList.split(","); //$NON-NLS-1$

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
            acceptor.accept(issue, Messages.ModuleRegionQuickFixProvider_MoveMethodToOtherRegion, "", null, //$NON-NLS-1$
                new BadRegionIssueResolver(issue, suggestions.getRecommendedRegions(), suggestions.getBadRegions()));

            // TODO: английский вариант
            acceptor.accept(issue, String.format(Messages.ModuleRegionQuickFixProvider_MoveMethodToRegion, Messages.ModuleRegionQuickFixProvider_Private),
                Messages.ModuleRegionQuickFixProvider_Description, null, new BadRegionIssueResolver(issue, Messages.ModuleRegionQuickFixProvider_Private));

            return;
        }

        suggestions = SuggestedRegionsComputer.parseIssue(issue,
            SuggestedRegionsComputer.METHOD_SHOULD_IN_SPECIFIED_REGION, scriptVariant);

        if (suggestions != null)
        {
            var recommendedRegions = suggestions.getRecommendedRegions();
            assert (recommendedRegions.size() > 0);

            String suggestedRegion = recommendedRegions.get(0);

            acceptor.accept(issue, String.format(Messages.ModuleRegionQuickFixProvider_MoveMethodToRegion, suggestedRegion), Messages.ModuleRegionQuickFixProvider_Description,
                null, new BadRegionIssueResolver(issue, suggestedRegion));

            return;
        }

    }

    @Fix("DUMMY")
    public void fixModuleStructureEventRegions(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        // TODO: fixme
        ScriptVariant scriptVariant = ScriptVariant.RUSSIAN;

        var suggestions = SuggestedRegionsComputer.parseIssue(issue,
            SuggestedRegionsComputer.METHOD_SHOULD_NOT_BE_IN_THIS_REGION, scriptVariant);

        if (suggestions != null)
        {
            var suggestedRegions = suggestions.getRecommendedRegions();

            // TODO: fixme

            if (suggestedRegions.size() == 0)
                suggestedRegions.add("СлужебныеПроцедурыИФункции"); //$NON-NLS-1$

            // TODO: развить логику таким образом чтобы если нет предлагаемых регионов и регионов в модулей - оставить только перемещение в "Private"
            acceptor.accept(issue, Messages.ModuleRegionQuickFixProvider_MoveMethodToOtherRegion, "", null, //$NON-NLS-1$
                new BadRegionIssueResolver(issue, suggestedRegions, suggestions.getBadRegions()));

            // TODO: английский вариант
            acceptor.accept(issue, String.format(Messages.ModuleRegionQuickFixProvider_MoveMethodToRegion, Messages.ModuleRegionQuickFixProvider_Private),
                Messages.ModuleRegionQuickFixProvider_Description, null, new BadRegionIssueResolver(issue, Messages.ModuleRegionQuickFixProvider_Private));

            return;
        }

    }

    @Fix("DUMMY")
    public void fixModuleStructureMethodInRegions(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        var suggestedRegions = parseSuggestedRegions_ModuleStructureMethodInRegions(issue);

        acceptor.accept(issue, Messages.ModuleRegionQuickFixProvider_MoveMethodToOtherRegion, Messages.ModuleRegionQuickFixProvider_Description, null,
            new BadRegionIssueResolver(issue, suggestedRegions, new LinkedList<>()));
    }

}
