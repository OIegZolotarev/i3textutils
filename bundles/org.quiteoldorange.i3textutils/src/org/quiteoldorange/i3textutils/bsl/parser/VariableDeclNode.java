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
public class VariableDeclNode
    extends AbsractBSLElementNode
{

    String mName = null;

    /**
     * @param stream
     * @throws UnexpectedToken
     */
    public VariableDeclNode(Lexer stream) throws UnexpectedToken
    {
        super(stream);

        var token = readTokenTracked(stream);

        if (token.getType() != Type.Identifier)
            throw new BSLParsingException.UnexpectedToken(stream, token, Type.Identifier);

        mName = token.getValue();

        // TODO: несколько переменных, поддержка "экспорт"

        checkTokenTracked(stream, Type.ExpressionEnd);
    }

}
