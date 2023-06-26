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

}
