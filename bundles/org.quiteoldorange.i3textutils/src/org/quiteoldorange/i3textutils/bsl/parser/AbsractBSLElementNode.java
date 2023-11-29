/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.UnexpectedToken;
import org.quiteoldorange.i3textutils.bsl.parser.MethodNode.MethodTypes;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.ExpressionNode;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class AbsractBSLElementNode
{
    AbsractBSLElementNode mParent = null;

    protected LinkedList<Token> mTokens = new LinkedList<>();
    private List<AbsractBSLElementNode> mChildren = new LinkedList<>();

    public List<AbsractBSLElementNode> getChildren()
    {
        return mChildren;
    }

    public void addChildren(AbsractBSLElementNode node)
    {
        mChildren.add(node);
        node.setParent(this);
    }

    public void removeChildren(AbsractBSLElementNode node)
    {
        if (!mChildren.remove(node))
            throw new NoSuchElementException();

        node.setParent(null);
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
                default:
                    stream.rollback();

                    Set<Token.Type> endingTokens = new HashSet<>();
                    endingTokens.add(Type.ExpressionEnd);
                    endingTokens.add(Type.OperatorElse);
                    endingTokens.add(Type.OperatorElseIf);
                    endingTokens.add(Type.OperatorEndIf);
                    endingTokens.add(Type.OperatorEndTry);
                    endingTokens.add(Type.OperatorEndLoop);
                    endingTokens.add(Type.EndProcedure);
                    endingTokens.add(Type.EndFunction);

                    return new ExpressionNode(stream, endingTokens);
                }

            case KeywordVar:
                return new VariableDeclNode(stream);
            case AnnotationAtClient:
            case AnnotationAtServer:
            case AnnotationAtServerNoContext:
            case AnnotationAtClientAtServerNoContext:
            case AnnotationAfter:
            case AnnotationBefore:
            case AnnotationAround:
                return new AnnotationNode(stream);
            case EmptyLine:
                return new EmptyLineNode(stream);
            case PreprocessorIf:
                return new PrepropcessorIfElseStatementNode(stream);
            case OperatorIf:
                return new IfElseStatementNode(stream);
            case OperatorFor:
                {
                    var nextToken = stream.peekNext();

                    switch (nextToken.getType())
                    {
                    case Identifier:
                        return new RangeForLoopNode(stream);
                    case KeywordEach:
                        return new ForEachLoopNode(stream);
                    }

                }
            // Какая-то нода недочитала до конца - пропускаем, чтобы упереться
            case ExpressionEnd:
                return null;
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
            if (newNode != null)
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

    /**
     * @param slice
     */
    public void setChildren(List<AbsractBSLElementNode> slice)
    {
        mChildren = slice;
    }

    public String serializeChildren(ScriptVariant variant, boolean addNewline)
    {
        StringBuilder builder = new StringBuilder();

        for (AbsractBSLElementNode node : getChildren())
        {
            // TODO: проверка на необходимость лепить перенос
            String nodeValue = node.serialize(variant);
            builder.append(nodeValue);

            if (addNewline)
                builder.append("\n"); //$NON-NLS-1$
        }

        return builder.toString();
    }

    public int getStartingOffset()
    {
        if (mChildren.size() > 0)
            return mChildren.get(0).getStartingOffset();

        if (mTokens.size() < 1)
        {
            return -1;
        }

        return mTokens.get(0).getOffset();
    }

    public int getEndOffset()
    {
        if (mChildren.size() > 0)
            return mChildren.get(mChildren.size() - 1).getEndOffset();

        if (mTokens.size() < 1)
        {
            return -1;
        }

        var endtoken = mTokens.get(mTokens.size() - 1);

        return endtoken.getOffset() + endtoken.getValue().length();
    }

    public int getLength()
    {
        return getEndOffset() - getStartingOffset();
    }

}
