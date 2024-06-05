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
    Set<Token.Type> mNodeEndingTokens = new HashSet<>();


    protected LinkedList<Token> mTokens = new LinkedList<>();
    private List<AbsractBSLElementNode> mChildren = new LinkedList<>();

    public AbsractBSLElementNode(Lexer stream)
    {
        if (stream != null)
            mTokens.add(stream.current());
    }

    public void addChildren(AbsractBSLElementNode node)
    {
        mChildren.add(node);
        node.setParent(this);
    }

    public void checkTokenTracked(Lexer stream, Token.Type expectedType) throws UnexpectedToken
    {
        stream.parseAndCheckToken(expectedType);
        mTokens.add(stream.current());
    }

    public List<AbsractBSLElementNode> getChildren()
    {
        return mChildren;
    }

    public int getEndOffset()
    {
//        if (mChildren.size() > 0)
//            return mChildren.get(mChildren.size() - 1).getEndOffset();

        if (mTokens.size() < 1)
        {
            return -1;
        }

        var endtoken = mTokens.get(mTokens.size() - 1);

        return endtoken.getOffset() + endtoken.getValue().length();
    }

    public int getHierarchyLevel()
    {
        if (mParent == null)
            return 0;

        return mParent.getHierarchyLevel() + 1;
    }

    public int getLength()
    {
        return getEndOffset() - getStartingOffset();
    }

    public int getStartingOffset()
    {
        // TODO: не требуется?
//        if (mChildren.size() > 0)
//            return mChildren.get(0).getStartingOffset();

        if (mTokens.size() < 1)
        {
            return -1;
        }

        return mTokens.get(0).getOffset();
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
                return new MethodNode(stream, MethodTypes.Function, this);
            case BeginProcedure:
                return new MethodNode(stream, MethodTypes.Procedure, this);
            case PreprocessorRegion:
                return new BSLRegionNode(stream, this);
            case Comment:
                return new CommentNode(stream, this);
            case KeywordAsynch:
                t = readTokenTracked(stream);

                if (t.getType() != Type.BeginFunction && t.getType() != Type.BeginProcedure)
                    throw new BSLParsingException.UnexpectedToken(stream, t, Type.BeginProcedure);

                if (t.getType() == Type.BeginFunction)
                    return new MethodNode(stream, MethodTypes.Function, true, this);

                if (t.getType() == Type.BeginProcedure)
                    return new MethodNode(stream, MethodTypes.Procedure, true, this);
            case Identifier:

                Token next = stream.peekNext();

                switch (next.getType())
                {
                case EqualsSign:
                    return new AssigmentExpression(stream, this);
                default:
                    stream.rollback();
                    return new ExpressionNode(stream, validExpressionEndTokensForThisNode());
                }

            case KeywordVar:
                return new VariableDeclNode(stream, this);
            case AnnotationAtClient:
            case AnnotationAtServer:
            case AnnotationAtServerNoContext:
            case AnnotationAtClientAtServerNoContext:
            case AnnotationAfter:
            case AnnotationBefore:
            case AnnotationChangeAndValidate:
            case AnnotationAround:
                return new AnnotationNode(stream, this);
            case EmptyLine:
                return new EmptyLineNode(stream, this);
            case PreprocessorIf:
                return new PrepropcessorIfElseStatementNode(stream, this);
            case OperatorIf:
                return new IfElseStatementNode(stream, this);
            case KeywordReturn:
                return new ReturnStatement(stream, this);
            case OperatorFor:
                {
                    var nextToken = stream.peekNext();

                    switch (nextToken.getType())
                    {
                    case Identifier:
                        return new RangeForLoopNode(stream, this);
                    case KeywordEach:
                        return new ForEachLoopNode(stream, this);
                    }

                }
            case OperatorWhile:
                return new WhileLoopNode(stream, this);
            // Какая-то нода недочитала до конца - пропускаем, чтобы упереться
            case ExpressionEnd:
                return null;
            default:
                throw new BSLParsingException.UnexpectedToken(stream, t);
            }

        }

        return null;
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

    public Token readTokenTracked(Lexer stream)
    {
        Token r = stream.parseNext();
        mTokens.add(r);
        return r;
    }

    public void removeChildren(AbsractBSLElementNode node)
    {
        if (!mChildren.remove(node))
            throw new NoSuchElementException();

        node.setParent(null);
    }

    /**
     * @return
     */
    public String serialize(ScriptVariant scriptVariant) throws Exception
    {
        return ""; //$NON-NLS-1$
    }

    public String serializeChildren(ScriptVariant variant, boolean addNewline) throws Exception
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

    /**
     * @param slice
     */
    public void setChildren(List<AbsractBSLElementNode> slice)
    {
        mChildren = slice;
    }

    /**
     * @param absractBSLElementNode
     */
    public void setParent(AbsractBSLElementNode node)
    {
        mParent = node;
    }

    public AbsractBSLElementNode getParent()
    {
        return mParent;
    }

    /**
     * В силу гениальной (сарказм) особенности языка - выражение может заканчиваться как точкой с запятой
     * так и концом блоком, приходится городить такой огород.
     *
     * @return допустимый в этой ноде конец выражения
     */
    public Set<Token.Type> validExpressionEndTokensForThisNode()
    {
        Set<Token.Type> result = new HashSet<>();

        for (var key: mNodeEndingTokens)
        {
            result.add(key);
        }

        result.add(Type.ExpressionEnd);

        return result;

    }

    /**
     * @param finisher
     */
    protected void addNodeEndingToken(Type finisher)
    {
        mNodeEndingTokens.add(finisher);
    }

    /**
     * @param b
     * @return
     */
    public int indexOfChild(AbsractBSLElementNode b)
    {
        return mChildren.indexOf(b);
    }

    /**
     * @param aIndex
     * @param b
     */
    public void insertChildren(int aIndex, AbsractBSLElementNode b)
    {
        mChildren.add(aIndex, b);
        b.setParent(this);
    }

}
