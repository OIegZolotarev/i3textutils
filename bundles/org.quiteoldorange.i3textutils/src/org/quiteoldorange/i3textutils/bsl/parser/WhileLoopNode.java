/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.ExpressionNode;

/**
 * @author ozolotarev
 *
 */
public class WhileLoopNode
    extends AbsractBSLElementNode
{
    private ExpressionNode mConditionExpression;

    public WhileLoopNode(Lexer stream, AbsractBSLElementNode parent) throws BSLParsingException
    {
        super(stream);

        mConditionExpression = new ExpressionNode(stream, Type.KeywordLoop);
        checkTokenTracked(stream, Type.KeywordLoop);
        ParseUntilEndingToken(stream, Type.OperatorEndLoop);

    }
}
