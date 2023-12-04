/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.HashSet;
import java.util.Set;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
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
     * @param parent TODO
     */
    public AssigmentExpression(Lexer stream, AbsractBSLElementNode parent) throws BSLParsingException
    {
        super(stream);

        // Первый токен - имя переменной

        mVariableName = mTokens.get(0).getValue();

        checkTokenTracked(stream, Type.EqualsSign);

        Set<Token.Type> endingTokens = new HashSet<>();
        endingTokens.add(Type.ExpressionEnd);
        endingTokens.add(Type.OperatorElse);
        endingTokens.add(Type.OperatorElseIf);
        endingTokens.add(Type.OperatorEndIf);
        endingTokens.add(Type.OperatorEndTry);
        endingTokens.add(Type.OperatorEndLoop);
        endingTokens.add(Type.EndProcedure);
        endingTokens.add(Type.EndFunction);

        addChildren(new ExpressionNode(stream, endingTokens));
    }

}
