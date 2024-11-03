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
import org.quiteoldorange.i3textutils.preferences.projectoptions.impl.StringProjectOption;

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

    private static final String NEW_ATTRIBUTES_ARRAY_NAME = "templateNameNewAttributesArray"; //$NON-NLS-1$
    private static final String NEW_ATTRIBUTE_NAME = "templateNameNewAttribute"; //$NON-NLS-1$
    private static final String NEW_COMMAND_NAME = "templateNameNewCommand"; //$NON-NLS-1$
    private static final String NEW_ELEMENT_NAME = "templateNameNewElement"; //$NON-NLS-1$

    private static final String PLACE_TO_FORM_MODULE = "placeInFormModule"; //$NON-NLS-1$
    private static final String PLACE_TO_COMMON_MODULE = "placeToCommonModule"; //$NON-NLS-1$
    private static final String DO_NOTHING = "doNothing"; //$NON-NLS-1$

    private static final String ID = "formsdecompiler"; //$NON-NLS-1$

    public static enum GeneratedCodePlacementOptions
    {
        DoNothing,
        ToFormModule,
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

        // Шаблоны имен объектов

        IProjectOption templateNames[] =
            { new StringProjectOption(NEW_ATTRIBUTES_ARRAY_NAME, "Массив новых реквизитов", "НовыеРеквизиты"),
                new StringProjectOption(NEW_ATTRIBUTE_NAME, "Нового реквизита", "НовыйРеквизит"),
                new StringProjectOption(NEW_COMMAND_NAME, "Новой команды", "НоваяКоманда"),
                new StringProjectOption(NEW_ELEMENT_NAME, "Нового элемента формы", "НовыйЭлемент") };

        for (IProjectOption opt : templateNames)
        {
            opt.setGroupName("Имена по умолчанию");
            addOption(opt);
        }
    }

    public static String ID()
    {
        return ID;
    }

    public static class GeneratedCodePlacement
        extends EnumProjectOption
    {
        /**
         * @param key
         * @param descritption
         * @param defaultValue
         */
        public GeneratedCodePlacement()
        {
            super(GENERATED_CODE_PLACEMENT, Messages.FormsDecompilerOptionSet_GeneratedCodePlacement, DO_NOTHING);

            setWidgetType(WidgetType.RadioGroup);

            addValue(Messages.FormsDecompilerOptionSet_Manual, DO_NOTHING);
            addValue(Messages.FormsDecompilerOptionSet_ToFormModule, PLACE_TO_FORM_MODULE);
            addValue(Messages.FormsDecompilerOptionSet_ToCommonModule, PLACE_TO_COMMON_MODULE);
        }

        /**
         * @param generateCodePlacement
         */
        public void setValue(GeneratedCodePlacementOptions generateCodePlacement)
        {
            switch (generateCodePlacement)
            {
            case DoNothing:
                setValue(DO_NOTHING);
                break;
            case ToCommonModule:
                setValue(PLACE_TO_COMMON_MODULE);
                break;
            case ToFormModule:
                setValue(PLACE_TO_FORM_MODULE);
                break;
            default:
                break;

            }

            updateWidgetState();
        }

        public GeneratedCodePlacementOptions generatedCodePlacement()
        {
            if (getValue().equals(DO_NOTHING))
                return GeneratedCodePlacementOptions.DoNothing;
            else if (getValue().equals(PLACE_TO_FORM_MODULE))
                return GeneratedCodePlacementOptions.ToFormModule;
            else if (getValue().equals(PLACE_TO_COMMON_MODULE))
                return GeneratedCodePlacementOptions.ToCommonModule;

            return null;
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

        GeneratedCodePlacement optCasted = (GeneratedCodePlacement)opt;
        return optCasted.generatedCodePlacement();
    }

}
