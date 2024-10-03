/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.xml;

import java.util.HashMap;

/**
 * @author ozolotarev
 *
 */
public class MultipleLanguageString
{
    private HashMap<String, String> mValues;

    public String getValue(String languangeKey)
    {
        if (mValues.containsKey(languangeKey))
            return mValues.get(languangeKey);
        else
            return ""; //$NON-NLS-1$
    }
}
