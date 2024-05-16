package org.quiteoldorange.i3textutils.qfix.movemethodtoregion;

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

    @Fix("DUMMY")
    public void fixModuleStructureFormEventRegions(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        // TODO: fixme
        ScriptVariant scriptVariant = ScriptVariant.RUSSIAN;

        if (addResolutionsForBadCurrentRegion(issue, acceptor, scriptVariant))
            return;

        addResolutionsForSpecifiedRegion(issue, acceptor, scriptVariant);

    }

    @Fix("DUMMY")
    public void fixModuleStructureEventRegions(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        // TODO: fixme
        ScriptVariant scriptVariant = ScriptVariant.RUSSIAN;

        addResolutionsForBadCurrentRegion(issue, acceptor, scriptVariant);

    }

    @Fix("DUMMY")
    public void fixModuleStructureMethodInRegions(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        // TODO: fixme
        ScriptVariant scriptVariant = ScriptVariant.RUSSIAN;
        addResolutionsForSpecifiedRegion(issue, acceptor, scriptVariant);
    }

    /**
     * @param issue
     * @param acceptor
     * @param scriptVariant
     */
    private boolean addResolutionsForBadCurrentRegion(final Issue issue, IssueResolutionAcceptor acceptor,
        ScriptVariant scriptVariant)
    {
        var suggestions = SuggestedRegionsComputer.parseIssue(issue,
            SuggestedRegionsComputer.METHOD_SHOULD_NOT_BE_IN_THIS_REGION, scriptVariant);

        if (suggestions != null)
        {
            var suggestedRegions = suggestions.getRecommendedRegions();

            if (suggestedRegions.size() == 0)
                suggestedRegions.add("СлужебныеПроцедурыИФункции"); //$NON-NLS-1$

            // TODO: развить логику таким образом чтобы если нет предлагаемых регионов и регионов в модулей - оставить только перемещение в "Private"
            acceptor.accept(issue, Messages.ModuleRegionQuickFixProvider_MoveMethodToOtherRegion, "", null, //$NON-NLS-1$
                new BadRegionIssueResolver(issue, suggestedRegions, suggestions.getBadRegions()));

            // TODO: английский вариант
            acceptor.accept(issue,
                String.format(Messages.ModuleRegionQuickFixProvider_MoveMethodToRegion,
                    Messages.ModuleRegionQuickFixProvider_Private),
                Messages.ModuleRegionQuickFixProvider_Description, null,
                new BadRegionIssueResolver(issue, Messages.ModuleRegionQuickFixProvider_Private));

            return true;
        }

        return false;
    }

    /**
     * @param issue
     * @param acceptor
     * @param scriptVariant
     */
    private void addResolutionsForSpecifiedRegion(final Issue issue, IssueResolutionAcceptor acceptor,
        ScriptVariant scriptVariant)
    {
        if (addResolutionsForBadCurrentRegion(issue, acceptor, scriptVariant))
            return;

        var suggestions = SuggestedRegionsComputer.parseIssue(issue,
            SuggestedRegionsComputer.METHOD_SHOULD_IN_SPECIFIED_REGION, null);

        if (suggestions != null)
        {
            var suggestedRegions = suggestions.getRecommendedRegions();

            if (suggestedRegions.size() == 0)
                suggestedRegions.add("СлужебныеПроцедурыИФункции"); //$NON-NLS-1$

            acceptor.accept(issue, Messages.ModuleRegionQuickFixProvider_MoveMethodToOtherRegion, "", null, //$NON-NLS-1$
                new BadRegionIssueResolver(issue, suggestedRegions, suggestions.getBadRegions()));

            // TODO: английский вариант
            acceptor.accept(issue,
                String.format(Messages.ModuleRegionQuickFixProvider_MoveMethodToRegion,
                    Messages.ModuleRegionQuickFixProvider_Private),
                Messages.ModuleRegionQuickFixProvider_Description, null,
                new BadRegionIssueResolver(issue, Messages.ModuleRegionQuickFixProvider_Private));

            return;
        }
    }

}
