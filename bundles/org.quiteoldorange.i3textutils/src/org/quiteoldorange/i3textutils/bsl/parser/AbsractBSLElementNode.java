/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.LinkedList;

import org.quiteoldorange.i3textutils.bsl.exceptions.BSLParsingException;
import org.quiteoldorange.i3textutils.bsl.exceptions.BSLParsingException.UnexpectedToken;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.MethodNode.MethodTypes;

/**
 * @author ozolotarev
 *
 */
public class AbsractBSLElementNode
{
    protected LinkedList<Token> mTokens = new LinkedList<>();
    protected LinkedList<AbsractBSLElementNode> mChildren = new LinkedList<>();

    public AbsractBSLElementNode(Lexer stream)
    {
        if (stream != null)
            mTokens.add(stream.current());
    }

    public AbsractBSLElementNode ParseNode(Lexer stream)
        throws BSLParsingException
    {
        while (true)
        {
            Token t = readTokenTracked(stream);

            if (t == null)
                break;

            switch (t.getType())
            {
            case BeginFunction:
                return new MethodNode(stream, MethodTypes.Function);
            case BeginProcedure:
                return new MethodNode(stream, MethodTypes.Procedure);
            case PreprocessorRegion:
                return new BSLRegionElement(stream);
            case Comment:
                return new CommentNode(stream);
            case KeywordAsynch:
                t = readTokenTracked(stream);

                if (t.getType() != Type.BeginFunction && t.getType() != Type.BeginProcedure)
                    throw new BSLParsingException.UnexpectedToken(stream, t, Type.BeginProcedure);

                if (t.getType() == Type.BeginFunction)
                    return new MethodNode(stream, MethodTypes.Function, true);

                if (t.getType() == Type.BeginProcedure)
                    return new MethodNode(stream, MethodTypes.Procedure, true);

            default:
                throw new BSLParsingException.UnexpectedToken(stream, t);
            }

        }

        return null;
    }

    public Token readTokenTracked(Lexer stream)
    {
        Token r = stream.parseNext();
        mTokens.add(r);
        return r;
    }

    public void checkTokenTracked(Lexer stream, Token.Type expectedType) throws UnexpectedToken
    {
        stream.parseAndCheckToken(expectedType);
        mTokens.add(stream.current());
    }

    public void ParseUntilEndingToken(Lexer stream, Token.Type type)
        throws BSLParsingException
    {
        while (true)
        {
            var token = stream.parseNext();

            if (token.getType() == type)
                break;

            // откатываем поток
            stream.rollback();

            AbsractBSLElementNode newNode = AbsractBSLElementNode.ParseNode(stream, this);
            mChildren.add(newNode);
        }
    }

    public static class UnexpectedTokenException
        extends Exception
    {
        Token mToken;

        /**
         *
         */
        public UnexpectedTokenException(Token token)
        {
            mToken = token;
        }

        /**
         * @return the token
         */
        public Token getToken()
        {
            return mToken;
        }
    }

}
