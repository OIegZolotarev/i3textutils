/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.LinkedList;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.UnexpectedToken;
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

    @SuppressWarnings("incomplete-switch")
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
                return new BSLRegionNode(stream);
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
            case Identifier:

                Token next = stream.peekNext();

                switch (next.getType())
                {
                case EqualsSign:
                    return new AssigmentExpression(stream);
                }

            default:
                throw new BSLParsingException.UnexpectedToken(stream, t);
            }

        }

        return null;
    }

    public AbsractBSLElementNode ParseExpressionNode(Lexer stream, Token.Type endingToken) throws BSLParsingException
    {
        while (true)
        {
            Token t = readTokenTracked(stream);

            if (t == null)
                break;

            switch (t.getType())
            {
            case Identifier:
                return new ExpressionNode(stream, endingToken);
            case NumericConstant:
            case StringConstant:
            case DateConstant:
            case BooleanConst:
                return new ConstantNode(stream);
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

            AbsractBSLElementNode newNode = ParseNode(stream);
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
