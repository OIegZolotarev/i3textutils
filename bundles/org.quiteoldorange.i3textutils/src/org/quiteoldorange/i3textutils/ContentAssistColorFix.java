/**
 *
 */
package org.quiteoldorange.i3textutils;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;
import org.quiteoldorange.i3textutils.preferences.PreferenceConstants;

/**
 * @author ozolotarev
 *
 */
public class ContentAssistColorFix
{

    /**
     *
     */
    public static void fixContentAssistColors()
    {
        IPreferenceStore store = i3TextUtilsPlugin.getDefault().getPreferenceStore();

        boolean forceHack = store.getBoolean(PreferenceConstants.FORCE_CONTENT_ASSIST_COLOR_HACK);
        String forcedColor = store.getString(PreferenceConstants.FORCE_CONTENT_ASSIST_COLOR_HACK_VALUE);

        String colorKey = "com._1c.g5.v8.dt.bsl.Bsl.syntaxColorer.tokenStyles.BSL_Keywords.color"; //$NON-NLS-1$
        var val = InstanceScope.INSTANCE.getNode("com._1c.g5.v8.dt.bsl.ui").get(colorKey, ""); //$NON-NLS-1$ //$NON-NLS-2$

        if (!val.isBlank())
            applyColorFix(val);
        else if (forceHack && !forcedColor.isBlank())
            applyColorFix(forcedColor);

    }

    private static void applyColorFix(String newColor)
    {

        try
        {
            Log.Debug("Starting hacking content assist colors..."); //$NON-NLS-1$

            Class<?> c = Class.forName("com._1c.g5.v8.dt.bsl.ui.contentassist.Messages"); //$NON-NLS-1$

            Log.Debug("Aquired \"com._1c.g5.v8.dt.bsl.ui.contentassist.Messages\""); //$NON-NLS-1$

            var field = c.getDeclaredField("ParametersHoverInfoControl_Description"); //$NON-NLS-1$
            field.setAccessible(true);

            Log.Debug("Got access to \"ParametersHoverInfoControl_Description\""); //$NON-NLS-1$

            String oldValue = (String)field.get(null);

            Log.Debug("Got it's value: %s", oldValue); //$NON-NLS-1$

            String hackColor = String.format("<style> a[style] { color: rgb(%s) !important;} </style>", //$NON-NLS-1$
                newColor);

            field.set(null, oldValue + hackColor);

            Log.Debug("Content assist hacked to: %s", newColor); //$NON-NLS-1$
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
