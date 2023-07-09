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

    public static String parseMethodFromURIToProblem(String value)
    {
        // #RU#/Заказы/src/Documents/ЗаказКлиента/Forms/ФормаВыбора/\MBS/_method/ПриСозданииНаСервере/0

        String methodMarker = "_method/"; //$NON-NLS-1$

        int offset = value.indexOf(methodMarker);

        if (offset == -1)
            return null;

        offset += methodMarker.length();

        String result = new String();

        while (offset < value.length() - 1)
        {
            char c = value.charAt(offset);

            if (c == '/')
                return result;

            result += c;
            offset++;
        }

        return result;
    }

}
