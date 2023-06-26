/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.UnexpectedToken;

/**
 * @author ozolotarev
 *
 */
public class AssigmentExpression
    extends AbsractBSLElementNode
{
    String mVariableName;

    /**
     * @param stream
     * @throws UnexpectedToken
     */
    public AssigmentExpression(Lexer stream) throws UnexpectedToken
    {
        super(stream);

        var token = readTokenTracked(stream);
        mVariableName = token.getValue();

        checkTokenTracked(stream, Type.EqualsSign);

        mChildren.add(new ExpressionNode(stream, Type.ExpressionEnd));

    }

}
