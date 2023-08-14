/**
 *
 */
package org.quiteoldorange.i3textutils.preferences;

import org.eclipse.osgi.util.NLS;

/**
 * @author ozolotarev
 *
 */
public class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
    public static String Pref_ShowHintForObviousParameters;
    public static String Pref_ContentAssistForcedColorFix;
    public static String Pref_EnableCodeminings;
    public static String Pref_ForceContentAssistColorFix;
    public static String Pref_PluginSetup;
    public static String Pref_ShowHintFor1ParameterInvocation;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
