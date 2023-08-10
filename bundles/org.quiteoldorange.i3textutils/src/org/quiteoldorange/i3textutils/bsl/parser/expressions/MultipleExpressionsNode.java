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
public class MultipleExpressionsNode
    extends AbsractBSLElementNode
{

    @Override
    public String toString()
    {
        return "<,>";
    }

    /**
     * @param stream
     */
    public MultipleExpressionsNode(Lexer stream)
    {
        super(stream);
        // TODO Auto-generated constructor stub
    }

}
