/**
 *
 */
package org.quiteoldorange.i3textutils;

/**
 * @author ozolotarev
 *
 */
public class StringUtils
{
    public static boolean isNumeric(String strNum)
    {
        if (strNum == null)
        {
            return false;
        }
        try
        {
            @SuppressWarnings("unused")
            double d = Double.parseDouble(strNum);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}
