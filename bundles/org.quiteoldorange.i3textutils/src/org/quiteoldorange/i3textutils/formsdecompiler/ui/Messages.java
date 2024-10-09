/**
 * 
 */
package org.quiteoldorange.i3textutils.formsdecompiler.ui;

import org.eclipse.osgi.util.NLS;

/**
 * @author ozolotarev
 *
 */
public class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
    public static String FormsDecompilerDialog_DecompilationPageTitle;
    public static String FormsDecompilerDialog_DialogTitle;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
