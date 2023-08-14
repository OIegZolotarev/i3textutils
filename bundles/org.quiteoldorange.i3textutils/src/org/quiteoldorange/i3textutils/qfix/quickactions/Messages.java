/**
 * 
 */
package org.quiteoldorange.i3textutils.qfix.quickactions;

import org.eclipse.osgi.util.NLS;

/**
 * @author ozolotarev
 *
 */
public class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
    public static String MethodQuickActions_ConvertToFunction;
    public static String MethodQuickActions_ConvertToProcedure;
    public static String MethodQuickActionsMarkerGenerator_ChooseQuickAction;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
