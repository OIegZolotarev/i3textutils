/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;

/**
 * @author ozolotarev
 *
 */
public class BSLParsingException
    extends Exception
{

    /**
     * @author ozolotarev
     *
     */
    public static class UnexpectedEndOfStream
        extends BSLParsingException
    {

    }

    /**
     * @author ozolotarev
     *
     */
    public static class UnexpectedToken
        extends BSLParsingException
    {
        @Override
        public String getMessage()
        {
            String result = null;

            if (mTypeExpected == null)
                result = String.format("Unexpected token \"%s\" at [%d,%d]", //$NON-NLS-1$
                    mToken.getValue(), mToken.getRow(), mToken.getColumn());
            else
                result = String.format("Unexpected token \"%s\" at [%d,%d], expected token type %s", //$NON-NLS-1$
                    mToken.getValue(), mToken.getRow(), mToken.getColumn(), mTypeExpected.toString());

            return result;
        }

        private Token mToken;
        private Token.Type mTypeExpected;
        private Lexer mLexer;

        public UnexpectedToken(Lexer lex, Token t)
        {
            mLexer = lex;
            mToken = t;

            mTypeExpected = null;
        }

        public UnexpectedToken(Lexer lex, Token t, Token.Type expectedToken)
        {
            mLexer = lex;
            mToken = t;

            mTypeExpected = expectedToken;
        }

        /**
         * @return the token
         */
        public Token getToken()
        {
            return mToken;
        }

        /**
         * @return the lexer
         */
        public Lexer getLexer()
        {
            return mLexer;
        }
    }

    public static class ExpectedClosingBracket
        extends BSLParsingException
    {

    }
}
