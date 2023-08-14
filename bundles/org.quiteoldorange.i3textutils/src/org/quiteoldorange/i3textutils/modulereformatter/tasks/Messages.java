/**
 * 
 */
package org.quiteoldorange.i3textutils.modulereformatter.tasks;

import org.eclipse.osgi.util.NLS;

/**
 * @author ozolotarev
 *
 */
public class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
    public static String AddRegionTask_CreateNewRegion;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
