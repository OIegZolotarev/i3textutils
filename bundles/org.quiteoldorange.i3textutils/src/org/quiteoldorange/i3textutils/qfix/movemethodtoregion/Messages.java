/**
 * 
 */
package org.quiteoldorange.i3textutils.qfix.movemethodtoregion;

import org.eclipse.osgi.util.NLS;

/**
 * @author ozolotarev
 *
 */
public class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
    public static String BadRegionIssueResolver_ChooseRegionIntoWhichToMoveMethod;
    public static String ModuleRegionQuickFixProvider_Description;
    public static String ModuleRegionQuickFixProvider_MoveMethodToOtherRegion;
    public static String ModuleRegionQuickFixProvider_MoveMethodToRegion;
    public static String ModuleRegionQuickFixProvider_Private;
    public static String RegionChooserDialog_ModuleRegions;
    public static String RegionChooserDialog_MoveMethodDialogTitle;
    public static String RegionChooserDialog_RegionIsNotExistingInModuleAndWillBeCreated;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
