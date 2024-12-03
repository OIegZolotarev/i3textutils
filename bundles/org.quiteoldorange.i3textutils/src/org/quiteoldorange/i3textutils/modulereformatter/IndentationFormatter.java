/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter;

import java.nio.CharBuffer;

/**
 * @author ozolotarev
 *
 */
public class IndentationFormatter
{
    private boolean mUseSpace;
    private int mSymbolsPerLevel;

    public IndentationFormatter(int symbolsPerLevel, boolean useSpaces)
    {
        mSymbolsPerLevel = symbolsPerLevel;
        mUseSpace = useSpaces;
    }

    public String format(String source)
    {
        StringBuilder r = new StringBuilder();

        int level = 0;

        String lines[] = source.split("\n");

        for (String line : lines)
        {
            String trimmed = line.trim();

            r.append(indentation(level));
            r.append(trimmed);

        }

        return r.toString();
    }

    /**
     * @param level
     * @return
     */
    private String indentation(int level)
    {
        if (mUseSpace)
        {
            return CharBuffer.allocate(mSymbolsPerLevel * (level + 1)).toString().replace('\0', ' ');
        }
        else
            return CharBuffer.allocate(mSymbolsPerLevel * (level + 1)).toString().replace('\0', '\t');
    }

}
