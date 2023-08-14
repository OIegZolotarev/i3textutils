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


    /**
     * @param text
     * @return
     */
    public static String verticalAlignSimple(String text, char anchor)
    {
        //////////////////////////////
        var lines = text.split("\n"); //$NON-NLS-1$

        int maxOffset = 0;

        for (String line : lines)
        {
            maxOffset = Math.max(maxOffset, line.indexOf(anchor));
        }

        StringBuilder newLines = new StringBuilder();

        for (String line : lines)
        {
            int currentOffset = line.indexOf(anchor);

            if (currentOffset == -1)
            {
                newLines.append(line + "\n"); //$NON-NLS-1$
                continue;
            }

            if (currentOffset == maxOffset)
            {
                newLines.append(line + "\n"); //$NON-NLS-1$
                continue;
            }

            int spacesToAdd = maxOffset - currentOffset;

            line = line.substring(0, currentOffset) + " ".repeat(spacesToAdd) + line.substring(currentOffset); //$NON-NLS-1$
            newLines.append(line + "\n"); //$NON-NLS-1$
        }

        //////////////////////////////

        String result = newLines.toString();
        result = result.substring(0, result.length() - 1);
        return result;
    }
}
