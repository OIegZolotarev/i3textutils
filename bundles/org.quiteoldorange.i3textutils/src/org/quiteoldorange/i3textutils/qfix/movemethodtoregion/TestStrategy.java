/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.movemethodtoregion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

class TestStrategy
{
    private String mRegex;
    private int mTargetGroup;
    private Pattern mPattern = null;
    private ScriptVariant mScriptVariant;

    /**
     *
     */
    public TestStrategy(String regex, int targetGroup, ScriptVariant variant)
    {
        mRegex = regex;
        mTargetGroup = targetGroup;
        mScriptVariant = variant;
    }

    public String testMessage(String msg)
    {
        if (mPattern == null)
            mPattern = Pattern.compile(mRegex);

        Matcher m = mPattern.matcher(msg);

        if (m.find())
        {
            return m.group(mTargetGroup);
        }

        return null;
    }

    /**
     * @return the scriptVariant
     */
    public ScriptVariant getScriptVariant()
    {
        return mScriptVariant;
    }
}