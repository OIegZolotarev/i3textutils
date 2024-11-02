/**
 *
 */
package org.quiteoldorange.i3textutils.preferences.projectoptions.impl.formsdecompiler;

import org.eclipse.core.resources.IProject;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;
import org.quiteoldorange.i3textutils.preferences.projectoptions.IProjectOption;
import org.quiteoldorange.i3textutils.preferences.projectoptions.ProjectOptionsSet;
import org.quiteoldorange.i3textutils.preferences.projectoptions.impl.BooleanProjectOption;
import org.quiteoldorange.i3textutils.preferences.projectoptions.impl.EnumProjectOption;

/**
 * @author ozolotarev
 *
 */
public class FormsDecompilerOptionSet
    extends ProjectOptionsSet
{

    /**
     *
     */
    private static final String USE_REGIONS = "useRegions"; //$NON-NLS-1$
    private static final String GENERATED_CODE_PLACEMENT = "generatedCodePlacement"; //$NON-NLS-1$

    private static final String PLACE_TO_AT_CREATE_AT_SERVER = "placeToAtCreateAtServer"; //$NON-NLS-1$
    private static final String PLACE_TO_COMMON_MODULE = "placeToCommonModule"; //$NON-NLS-1$
    private static final String DO_NOTHING = "doNothing"; //$NON-NLS-1$

    private static final String ID = "formsdecompiler"; //$NON-NLS-1$

    public static enum GeneratedCodePlacementOptions
    {
        DoNothing,
        ToAtCreateAtServer,
        ToCommonModule
    };

    /**
     * @param id
     * @param project
     */
    public FormsDecompilerOptionSet(IProject project)
    {
        super(ID, project);

        addOption(new BooleanProjectOption(USE_REGIONS, Messages.FormsDecompilerOptionSet_UseRegionsDescription,
            Boolean.FALSE));
        addOption(new GeneratedCodePlacement());

    }

    public static String ID()
    {
        return ID;
    }

    private static class GeneratedCodePlacement
        extends EnumProjectOption
    {
        /**
         * @param key
         * @param descritption
         * @param defaultValue
         */
        GeneratedCodePlacement()
        {
            super(GENERATED_CODE_PLACEMENT, Messages.FormsDecompilerOptionSet_GeneratedCodePlacement, DO_NOTHING);

            setWidgetType(WidgetType.RadioGroup);

            addValue(Messages.FormsDecompilerOptionSet_Manual, DO_NOTHING);
            addValue(Messages.FormsDecompilerOptionSet_ToAtCreateAtServer, PLACE_TO_AT_CREATE_AT_SERVER);
            addValue(Messages.FormsDecompilerOptionSet_ToCommonModule, PLACE_TO_COMMON_MODULE);
        }

    }

    public static boolean useRegions(IProject project)
    {
        ProjectOptionsSet options = i3TextUtilsPlugin.getProjectOptionsManager().getProjectOptionsSet(project, ID);
        BooleanProjectOption useRegionsOption = (BooleanProjectOption)options.getOption(USE_REGIONS);

        return useRegionsOption.getValueBoolean();
    }

    public static GeneratedCodePlacementOptions generatedCodePlacement(IProject project)
    {
        ProjectOptionsSet options = i3TextUtilsPlugin.getProjectOptionsManager().getProjectOptionsSet(project, ID);
        IProjectOption opt = options.getOption(GENERATED_CODE_PLACEMENT);

        if (opt.getValue().equals(DO_NOTHING))
            return GeneratedCodePlacementOptions.DoNothing;
        else if (opt.getValue().equals(PLACE_TO_AT_CREATE_AT_SERVER))
            return GeneratedCodePlacementOptions.ToAtCreateAtServer;
        else if (opt.getValue().equals(PLACE_TO_COMMON_MODULE))
            return GeneratedCodePlacementOptions.ToCommonModule;

        return null;
    }

}
