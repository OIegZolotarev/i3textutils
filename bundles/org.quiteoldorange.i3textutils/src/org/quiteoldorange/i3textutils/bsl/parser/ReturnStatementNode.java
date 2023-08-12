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
public class ReturnStatementNode
    extends AbsractBSLElementNode
{

    /**
     * @param stream
     * @throws BSLParsingException
     */
    public ReturnStatementNode(Lexer stream) throws BSLParsingException
    {
        super(stream);

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
