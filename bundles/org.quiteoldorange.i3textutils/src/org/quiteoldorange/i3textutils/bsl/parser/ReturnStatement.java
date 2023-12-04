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
public class ReturnStatement
    extends AbsractBSLElementNode
{

    private ExpressionNode mReturnExpression;

    /**
     * @param stream
     * @param parent TODO
     * @throws BSLParsingException
     */
    public ReturnStatement(Lexer stream, AbsractBSLElementNode parent) throws BSLParsingException
    {
        super(stream);

        mReturnExpression = new ExpressionNode(stream, parent.validExpressionEndTokensForThisNode());
    }

}
