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
import org.quiteoldorange.i3textutils.bsl.parser.OperationNode.Operator;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class AbsractBSLElementNode
{
    AbsractBSLElementNode mParent = null;

    protected LinkedList<Token> mTokens = new LinkedList<>();
    private LinkedList<AbsractBSLElementNode> mChildren = new LinkedList<>();

    public LinkedList<AbsractBSLElementNode> getChildren()
    {
        return mChildren;
    }

    public void addChildren(AbsractBSLElementNode node)
    {
        mChildren.add(node);
        node.setParent(this);
    }

    public int getHierarchyLevel()
    {
        if (mParent == null)
            return 0;

        return mParent.getHierarchyLevel() + 1;
    }

    /**
     * @param absractBSLElementNode
     */
    public void setParent(AbsractBSLElementNode node)
    {
        mParent = node;
    }

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
                case OpeningBracket:
                    return new MethodCallNode(stream);
                case Dot:
                    return new MemberExpression(stream);
                }

                throw new BSLParsingException.UnexpectedToken(stream, t);

            case KeywordVar:
                return new VariableDeclNode(stream);
            case Annotation:
                return new AnnotationNode(stream);
                break;
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

            if (t.getType() == endingToken)
                break;

            switch (t.getType())
            {
            case Identifier:
                //    return new ExpressionNode(stream, endingToken);
            case NumericConstant:
            case StringConstant:
            case DateConstant:
            case BooleanConst:
                addChildren(new ConstantNode(stream));
                break;
            case PlusSign:
                addChildren(new OperationNode(stream, Operator.Addition));
                break;
            case MinusSign:
                addChildren(new OperationNode(stream, Operator.Substraction));
                break;
            case MultiplicationSign:
                addChildren(new OperationNode(stream, Operator.Multiplication));
                break;
            case DivisionSign:
                addChildren(new OperationNode(stream, Operator.Division));
                break;
            case ModuloSign:
                addChildren(new OperationNode(stream, Operator.Modulo));
                break;
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
            var token = stream.peekNext();

            if (token.getType() == type)
            {
                readTokenTracked(stream);
                break;
            }

            AbsractBSLElementNode newNode = ParseNode(stream);
            addChildren(newNode);
        }
    }

    /**
     * @return
     */
    public String serialize(ScriptVariant scriptVariant)
    {
        return ""; //$NON-NLS-1$
    }


}
