/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;

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
     */
    public AssigmentExpression(Lexer stream) throws BSLParsingException
    {
        super(stream);

        // Первый токен - имя переменной

        mVariableName = mTokens.get(0).getValue();

        checkTokenTracked(stream, Type.EqualsSign);

        mChildren.add(new ExpressionNode(stream, Type.ExpressionEnd));
    }

}
