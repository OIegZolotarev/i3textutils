/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;

/**
 * @author ozolotarev
 *
 */
public class AbstractIfElseStatement
    extends AbsractBSLElementNode
{

    public static class TokenSet
    {
        private Type mIfToken;
        private Type mElsetoken;
        private Type mElseIftoken;
        private Type mEndIfToken;

        TokenSet(Token.Type ifToken, Token.Type elseToken, Token.Type elseIfToken, Token.Type endIfToken)
        {
            mIfToken = ifToken;
            mElsetoken = elseToken;
            mElseIftoken = elseIfToken;
            mEndIfToken = endIfToken;
        }

        /**
         * @return the ifToken
         */
        public Type getIfToken()
        {
            return mIfToken;
        }

        /**
         * @return the elsetoken
         */
        public Type getElsetoken()
        {
            return mElsetoken;
        }

        /**
         * @return the elseIftoken
         */
        public Type getElseIftoken()
        {
            return mElseIftoken;
        }

        /**
         * @return the endIfToken
         */
        public Type getEndIfToken()
        {
            return mEndIfToken;
        }
    }

    static TokenSet sPreprocessorSet =
        new TokenSet(Type.PreprocessorIf, Type.PreprocessorElse, Type.PreprocessorElseIf, Type.PreprocessorEndIf);

    static TokenSet sBSLCodeSet =
        new TokenSet(Type.OperatorIf, Type.OperatorElse, Type.OperatorElseIf, Type.OperatorEndIf);

    class ConditionNode
        extends AbsractBSLElementNode
    {
        ExpressionNode mConditionalExpression;

        public ConditionNode(Lexer stream, ExpressionNode node)
        {
            super(stream);
        }
    }

    /**
     * @param stream
     */
    public AbstractIfElseStatement(Lexer stream, TokenSet kind)
    {
        super(stream);
        // TODO Auto-generated constructor stub
    }
}
