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
public class AssigmentExpression
    extends AbsractBSLElementNode
{
    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return String.format("%s = %s", mVariableName, getChildren().get(0).toString()); //$NON-NLS-1$
    }

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

        addChildren(new ExpressionNode(stream, Type.ExpressionEnd));
    }

}
