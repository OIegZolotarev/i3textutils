/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.movemethodtoregion;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.validation.Issue;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class SuggestedRegionsComputer
{
    //
    public static final String METHOD_SHOULD_NOT_BE_IN_THIS_REGION = "METHOD_SHOULD_NOT_BE_IN_THIS_REGION"; //$NON-NLS-1$
    private static List<TestStrategy> mTestsShouldNotBeInThisRegion = null;

    public static final String METHOD_SHOULD_IN_SPECIFIED_REGION = "METHOD_SHOULD_IN_SPECIFIED_REGION"; //$NON-NLS-1$
    private static List<TestStrategy> mTestsShouldBeInSpecifiedRegion = null;

    static
    {

        var l = new LinkedList<TestStrategy>();
        l.add(new TestStrategy("Метод \"(.+)\" не следует размещать в области \"(.+)\"", 2, ScriptVariant.RUSSIAN)); //$NON-NLS-1$
        l.add(new TestStrategy("Method \"(.+)\" can not be placed in the \"(.+)\" region", 2, ScriptVariant.ENGLISH)); //$NON-NLS-1$
        mTestsShouldNotBeInThisRegion = l;

        l = new LinkedList<>();
        // ModuleStructureEventFormRegionsCheck_Event_method__0__should_be_placed_in_the_region__1
        l.add(new TestStrategy("Обработчик события \"(.+)\" следует разместить в области \"(.+)\"", 2, //$NON-NLS-1$
            ScriptVariant.RUSSIAN));
        l.add(new TestStrategy("The event method \"(.+)\" should be placed in the \"(.+)\" region", 2, //$NON-NLS-1$
            ScriptVariant.ENGLISH));

        // ModuleStructureEventRegionsCheck_Event_handler__0__not_region__1
        l.add(new TestStrategy("Обработчик событий \"(.+)\" следует разместить в область \"(.+)\"", 2, //$NON-NLS-1$
            ScriptVariant.RUSSIAN));


        mTestsShouldBeInSpecifiedRegion = l;
    }

    public static SuggestedRegions parseIssue(Issue issue, String hint, ScriptVariant scriptVariant)
    {
        if (hint == METHOD_SHOULD_NOT_BE_IN_THIS_REGION)
        {
            String regionName = parseRegion(issue, scriptVariant, mTestsShouldNotBeInThisRegion);

            if (regionName != null)
            {
                return makeBadRegionsSuggestions(regionName);
            }
        }
        else if (hint == METHOD_SHOULD_IN_SPECIFIED_REGION)
        {
            String regionName = parseRegion(issue, scriptVariant, mTestsShouldBeInSpecifiedRegion);

            if (regionName != null)
            {
                return makeGoodRegionsSuggestions(regionName);
            }
        }

        return null;
    }

    /**
     * @param regionName
     * @return
     */
    private static SuggestedRegions makeGoodRegionsSuggestions(String regionName)
    {
        SuggestedRegions result = new SuggestedRegions();
        result.addRecommededRegion(regionName);
        return result;
    }

    /**
     * @param regionName
     * @return
     */
    private static SuggestedRegions makeBadRegionsSuggestions(String regionName)
    {
        SuggestedRegions result = new SuggestedRegions();
        result.addBadRegion(regionName);
        return result;
    }

    /**
     * @param issue
     * @param scriptVariant
     * @return
     */
    private static String parseRegion(Issue issue, ScriptVariant scriptVariant, List<TestStrategy> tests)
    {
        String message = issue.getMessage();

        for (var test : tests)
        {
            if (test.getScriptVariant() != scriptVariant)
                continue;

            String parsedRegion = test.testMessage(message);

            if (parsedRegion != null)
            {
                return parsedRegion;
            }
        }

        return null;

    }
}
