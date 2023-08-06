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
public class MemberAccessNode
    extends AbsractBSLElementNode
    implements IOperationNode
{

    private AbsractBSLElementNode mLeftNode = null;
    private AbsractBSLElementNode mRightNode = null;

    @Override
    public String toString()
    {
        if (mLeftNode != null && mRightNode != null)
        {
            return mLeftNode.toString() + "ТЧК" + mRightNode.toString();
        }

        //
        return "."; //$NON-NLS-1$
    }

    /**
     * @param stream
     */
    public MemberAccessNode(Lexer stream)
    {
        super(stream);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int precedence()
    {
        // TODO Auto-generated method stub
        return 1;
    }

    public void setLeftNode(AbsractBSLElementNode node)
    {
        mLeftNode = node;
    }

    public void setRightNode(AbsractBSLElementNode node)
    {
        mRightNode = node;
    }
}
