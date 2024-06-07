/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.ExpressionNode;

/**
 * @author ozolotarev
 *
 */
public class RaiseStatement
    extends AbsractBSLElementNode
{
    private ExpressionNode mRaiseException;

    /**
     * @param stream
     * @param parent TODO
     * @throws BSLParsingException
     */
    public RaiseStatement(Lexer stream, AbsractBSLElementNode parent) throws BSLParsingException
    {
        super(stream);

        mRaiseException = new ExpressionNode(stream, parent.validExpressionEndTokensForThisNode());
    }
}
