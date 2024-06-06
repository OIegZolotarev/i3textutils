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
public class IndexAccessNode
    extends AbsractBSLElementNode
    implements IOperationNode
{

    /**
     * @param stream
     */
    public IndexAccessNode(Lexer stream)
    {
        super(stream);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int precedence()
    {
        // TODO Auto-generated method stub
        return 30;
    }

}
