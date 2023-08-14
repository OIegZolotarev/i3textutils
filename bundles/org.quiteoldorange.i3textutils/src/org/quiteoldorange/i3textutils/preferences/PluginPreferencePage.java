package org.quiteoldorange.i3textutils.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class PluginPreferencePage
    extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage
{

    @Override
    public boolean performOk()
    {
        // TODO Auto-generated method stub
        return super.performOk();
    }

    public PluginPreferencePage()
    {
        super(GRID);
        setPreferenceStore(i3TextUtilsPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.Pref_PluginSetup);
    }

    /**
     * Creates the field editors. Field editors are abstractions of
     * the common GUI blocks needed to manipulate various types
     * of preferences. Each field editor knows how to save and
     * restore itself.
     */
    @Override
    public void createFieldEditors()
    {

        var p = getFieldEditorParent();


        addField(
            new BooleanFieldEditor(PreferenceConstants.FORCE_CONTENT_ASSIST_COLOR_HACK,
                Messages.Pref_ForceContentAssistColorFix, p));

        addField(new ColorFieldEditor(PreferenceConstants.FORCE_CONTENT_ASSIST_COLOR_HACK_VALUE,
            Messages.Pref_ContentAssistForcedColorFix, p));

        addField(new BooleanFieldEditor(PreferenceConstants.CODEMININGS_ENABLED,
            Messages.Pref_EnableCodeminings, p));

        addField(new BooleanFieldEditor(PreferenceConstants.CODEMININGS_SHOW_WHEN_ONE_PARAMETER,
            Messages.Pref_ShowHintFor1ParameterInvocation, p));

        addField(new BooleanFieldEditor(PreferenceConstants.CODEMININGS_SHOW_WHEN_INPUT_CONTAINS_PARAMETER_NAME,
            Messages.Pref_ShowHintForObviousParameters, p));

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench)
    {
        // empty for now
    }

}
