/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.UnexpectedToken;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.ExpressionNode;

/**
 * @author ozolotarev
 *
 */
public class RangeForLoopNode
    extends AbsractBSLElementNode
{


    private ExpressionNode mIndex;
    private ExpressionNode mStartingExpression;
    private ExpressionNode mEndingExpression;

    /**
     * @param stream
     * @throws UnexpectedToken
     */
    public RangeForLoopNode(Lexer stream) throws BSLParsingException
    {
        super(stream);

        // For index = start To End Loop <Statement> EndLoop

        Token token = null;

        mIndex = new ExpressionNode(stream, Type.EqualsSign);

        checkTokenTracked(stream, Type.EqualsSign);

        mStartingExpression = new ExpressionNode(stream, Type.KeywordTo);

        checkTokenTracked(stream, Type.KeywordTo);

        mEndingExpression = new ExpressionNode(stream, Type.KeywordLoop);

        checkTokenTracked(stream, Type.KeywordLoop);

        ParseUntilEndingToken(stream, Type.OperatorEndLoop);

    }

}
