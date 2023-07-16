/**
 *
 */
package org.quiteoldorange.i3textutils;

import java.util.Arrays;

import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;

/**
 * @author ozolotarev
 *
 */
public class Log
{
    public static void Debug(String... strings)
    {
        if (strings.length < 1)
            return;

        i3TextUtilsPlugin.getDefault()
            .getLog()
            .info(String.format(strings[0], (Object[])Arrays.copyOfRange(strings, 1, strings.length)));
    }
}
