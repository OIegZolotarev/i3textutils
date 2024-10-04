/**
 *
 */
package org.quiteoldorange.i3textutils.qfix2023.addmethodpragma;

import org.eclipse.osgi.util.NLS;

/**
 * @author ozolotarev
 *
 */
public class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
    public static String AddMethodPragmaFix_AtClient;
    public static String AddMethodPragmaFix_AtServer;
    public static String AddMethodPragmaFix_AtServerWithoutContext;
    public static String AddMethodPragmaFix_PragmaDescriptionTemplate;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
