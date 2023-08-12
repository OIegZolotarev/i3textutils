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
public class MemberAccessExpression
    extends AbsractBSLElementNode
{

    @Override
    public int getStartingOffset()
    {
        if (mLeftNode != null)
            return mLeftNode.getStartingOffset();

        return super.getStartingOffset();
    }

    private AbsractBSLElementNode mLeftNode = null;
    private AbsractBSLElementNode mRightNode = null;

    @Override
    public String toString()
    {
        if (mLeftNode != null && mRightNode != null)
        {
            return mLeftNode.toString() + "." + mRightNode.toString();
        }

        //
        return "."; //$NON-NLS-1$
    }

    /**
     * @param stream
     */
    public MemberAccessExpression(Lexer stream)
    {
        super(stream);
    }

    /**
     * @param leftNode the leftNode to set
     */
    public void setLeftNode(AbsractBSLElementNode leftNode)
    {
        mLeftNode = leftNode;
    }

    /**
     * @param rightNode the rightNode to set
     */
    public void setRightNode(AbsractBSLElementNode rightNode)
    {
        mRightNode = rightNode;
    }

}
