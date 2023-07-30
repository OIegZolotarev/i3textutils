/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

/**
 * @author ozolotarev
 *
 */
public class ExpressionOpeningBracket
    extends AbsractBSLElementNode
{

    private int mLevel;

    /**
     * @param stream
     */
    public ExpressionOpeningBracket(Lexer stream, int level)
    {
        super(stream);

        setLevel(level);
    }

    /**
     * @return the level
     */
    public int getLevel()
    {
        return mLevel;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level)
    {
        mLevel = level;
    }

}
