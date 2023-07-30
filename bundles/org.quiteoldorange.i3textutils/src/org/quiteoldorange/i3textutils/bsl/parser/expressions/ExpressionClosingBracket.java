/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser.expressions;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;

/**
 * @author ozolotarev
 *
 */
public class ExpressionClosingBracket
    extends AbsractBSLElementNode
    implements IOperationNode
{

    private int mLevel;

    /**
     * @param stream
     */
    public ExpressionClosingBracket(Lexer stream, int level)
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

    @Override
    public int precedence()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String toString()
    {
        return ")"; //$NON-NLS-1$
    }
}
