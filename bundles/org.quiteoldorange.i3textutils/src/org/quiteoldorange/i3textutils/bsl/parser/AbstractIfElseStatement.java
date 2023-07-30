/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

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

        public boolean isBlockStatementEndtoken(Token t)
        {
            return t.getType() == mEndIfToken || t.getType() == mElseIftoken || t.getType() == mElsetoken;
        }

        /**
         * @param scriptVariant
         * @return
         */
        public String serializeEndIfToken(ScriptVariant scriptVariant)
        {
            return Token.getKeywordValue(mEndIfToken, scriptVariant);
        }

        /**
         * @param scriptVariant
         * @return
         */
        public String serializeIfToken(ScriptVariant scriptVariant)
        {
            return Token.getKeywordValue(mIfToken, scriptVariant);
        }

        /**
         * @param scriptVariant
         * @return
         */
        public String serializeElseIfToken(ScriptVariant scriptVariant)
        {
            return Token.getKeywordValue(mElseIftoken, scriptVariant);
        }

        /**
         * @param scriptVariant
         * @return
         */
        public String serializeElseToken(ScriptVariant scriptVariant)
        {
            return Token.getKeywordValue(mElsetoken, scriptVariant);
        }

        /**
         * @param scriptVariant
         * @return
         */
        public String serializeThenToken(ScriptVariant scriptVariant)
        {
            return Token.getKeywordValue(Type.OperatorThen, scriptVariant);
        }
    }

    static TokenSet sPreprocessorSet =
        new TokenSet(Type.PreprocessorIf, Type.PreprocessorElse, Type.PreprocessorElseIf, Type.PreprocessorEndIf);

    static TokenSet sBSLCodeSet =
        new TokenSet(Type.OperatorIf, Type.OperatorElse, Type.OperatorElseIf, Type.OperatorEndIf);

    class ConditionNode
        extends AbsractBSLElementNode
    {

        public String serialize(ScriptVariant scriptVariant, int index)
        {
            StringBuilder r = new StringBuilder();

            if (index == 0)
            {
                r.append(String.format("%s %s %s", mKind.serializeIfToken(scriptVariant), //$NON-NLS-1$
                    mConditionalExpression.serialize(scriptVariant),
                    mKind.serializeThenToken(scriptVariant)));
            }
            else if (mConditionalExpression != null)
            {
                r.append(String.format("%s %s %s", mKind.serializeElseIfToken(scriptVariant), //$NON-NLS-1$
                        mConditionalExpression.serialize(scriptVariant),
                        mKind.serializeThenToken(scriptVariant)));
            }
            else
            {
                r.append(String.format("%s", mKind.serializeElseToken(scriptVariant)));
            }

            r.append("\n");
            r.append(serializeChildren(scriptVariant, true));

            return r.toString();
        }

        ExpressionNode mConditionalExpression;

        public ConditionNode(Lexer stream, ExpressionNode conditionExpression, TokenSet kind) throws BSLParsingException
        {
            super(stream);

            mConditionalExpression = conditionExpression;

            while (true)
            {
                Token token = stream.peekNext();

                if (kind.isBlockStatementEndtoken(token))
                {
                    readTokenTracked(stream);
                    break;
                }

                AbsractBSLElementNode node = ParseNode(stream);
                addChildren(node);
            }

        }

    }

    private TokenSet mKind;

    /**
     * @param stream
     * @throws BSLParsingException
     */
    public AbstractIfElseStatement(Lexer stream, TokenSet kind) throws BSLParsingException
    {
        super(stream);

        mKind = kind;

        // #Если Выражение Тогда
        // #Иначе | #ИначеЕсли | #КонецЕсли

        while (true)
        {
            var token_type = stream.current().getType();

            if (token_type == kind.getIfToken() || token_type == kind.getElseIftoken())
            {
                AbsractBSLElementNode expression = new ExpressionNode(stream, Token.Type.OperatorThen);

                ConditionNode conditionNode = new ConditionNode(stream, (ExpressionNode)expression, kind);
                addChildren(conditionNode);
            }
            else if (token_type == kind.getEndIfToken())
            {
                readTokenTracked(stream);
                break;
            }
            else
            {
                ConditionNode conditionNode = new ConditionNode(stream, null, kind);
                addChildren(conditionNode);
            }

        }
    }

    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        assert getChildren().size() > 1;

        StringBuilder result = new StringBuilder();

        var iterator = getChildren().iterator();

        int index = 0;
        result.append(((ConditionNode)iterator.next()).serialize(scriptVariant, index++));

        while (iterator.hasNext())
        {
            result.append(((ConditionNode)iterator.next()).serialize(scriptVariant, index++));
        }


        result.append(String.format("%s", mKind.serializeEndIfToken(scriptVariant))); //$NON-NLS-1$

        return result.toString();
    }

    /**
     * @return the kind
     */
    protected TokenSet getKind()
    {
        return mKind;
    }
}
