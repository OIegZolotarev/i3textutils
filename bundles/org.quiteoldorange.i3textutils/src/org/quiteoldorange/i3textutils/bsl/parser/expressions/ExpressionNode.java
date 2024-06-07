/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser.expressions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.quiteoldorange.i3textutils.Tuple;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.ExpectedClosingBracket;
import org.quiteoldorange.i3textutils.bsl.parser.ConstantNode;
import org.quiteoldorange.i3textutils.bsl.parser.IdentifierNode;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.OperationNode.Operator;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class ExpressionNode
    extends AbsractBSLElementNode
{
    private boolean mCompound;

    public ExpressionNode(Lexer stream, Set<Token.Type> endTokens) throws BSLParsingException
    {
        super(null);

        ParseExpressionNode(stream, endTokens);
        reduce(getChildren());
    }

    public ExpressionNode(Lexer stream, Token.Type endToken) throws BSLParsingException
    {
        super(null);

        Set<Token.Type> endingTokens = new HashSet<>();
        endingTokens.add(endToken);

        ParseExpressionNode(stream, endingTokens);
        reduce(getChildren());
    }

    /**
     * @param slice
     * @throws ExpectedClosingBracket
     */
    public ExpressionNode(List<AbsractBSLElementNode> slice) throws BSLParsingException
    {
        super(null);

        if (slice == null)
            return;

        var expressions = splitMultipleExpressions(slice);

        getChildren().clear();

        for (var expressionList : expressions)
        {
            reduce(expressionList);
            if (expressionList.size() > 1)
                throw new BSLParsingException.UnexpectedEndOfStream();

            if (expressionList.size() > 0)
                addChildren(expressionList.get(0));
            else
                addChildren(new ExpressionNode(null));
        }
    }

    /**
     * @param subList
     * @param b
     * @throws BSLParsingException
     */
    public ExpressionNode(List<AbsractBSLElementNode> subList, boolean compound) throws BSLParsingException
    {

        this(subList);

        mCompound = compound;
    }

    /**
     * @param node
     * @param children
     * @return
     * @throws ExpectedClosingBracket
     */
    private AbsractBSLElementNode findEndingBracket(AbsractBSLElementNode startNode, boolean reverse,
        List<AbsractBSLElementNode> children, Class<?> openingBracketType, Class<?> endingBracketType)
        throws ExpectedClosingBracket
    {
        int level = 0;

        //  + (reverse ? -1 : 1)
        var iterator = children.listIterator(children.indexOf(startNode));

        var endClass = (reverse ? openingBracketType : endingBracketType);
        var openingClass = (reverse ? endingBracketType : openingBracketType);

        while (reverse ? iterator.hasPrevious() : iterator.hasNext())
        {
            AbsractBSLElementNode node = reverse ? iterator.previous() : iterator.next();

            if (node.getClass() == endClass && level == 0)
                return node;
            else if (node.getClass() == endClass && level != 0)
                level--;

            if (node.getClass() == openingClass)
                level++;
        }

        throw new BSLParsingException.ExpectedClosingBracket();

    }

    public AbsractBSLElementNode findMostPrecdenceOperator(List<AbsractBSLElementNode> nodes)
    {
        AbsractBSLElementNode best = nodes.get(0);
        int bestPrecedence = 99999;

        var iterator = nodes.listIterator(nodes.size());

        while(iterator.hasPrevious())
        {
            var item = iterator.previous();

            if (item instanceof IOperationNode)
            {
                IOperationNode opNode = (IOperationNode)item;

                if (opNode.precedence() < bestPrecedence)
                {
                    bestPrecedence = opNode.precedence();
                    best = item;
                }
            }
        }

        return best;
    }

    @SuppressWarnings("unused")
    private Tuple<Integer, ExpressionNode> parseBracketsExpression(AbsractBSLElementNode node,
        List<AbsractBSLElementNode> children,
        Class<?> openingBracket, Class<?> closingBracket) throws BSLParsingException
    {
        boolean reverse = node instanceof ExpressionClosingBracket;

        AbsractBSLElementNode endingNode =
            findEndingBracket(node, reverse, children, openingBracket, closingBracket);

        int startIndex = 0;
        int endIndex = 0;

        if (reverse)
        {
            startIndex = children.indexOf(endingNode);
            endIndex = children.indexOf(node) + 1;
        }
        else
        {
            startIndex = children.indexOf(node);
            endIndex = children.indexOf(endingNode) + 1;
        }

        var slice = children.subList(startIndex, endIndex);
        ExpressionNode compoundExpression = new ExpressionNode(slice.subList(1, slice.size() - 1), true);
        slice.clear();

        //children.add(startIndex, compoundExpression);

        return new Tuple<>(startIndex, compoundExpression);
    }

    public AbsractBSLElementNode ParseExpressionNode(Lexer stream, Set<Token.Type> endingTokens) throws BSLParsingException
    {
        while (true)
        {
            Token t = stream.peekNext();

            if (t == null)
                break;

            if (endingTokens.contains(t.getType()))
            {

                // Костыль для того чтобы корректно отпарсить "КонецФункции" и подобное без точки с запятой
                if (t.getType() == Token.Type.ExpressionEnd || t.getType() == Token.Type.OperatorThen)
                    readTokenTracked(stream);

                break;
            }

            t = readTokenTracked(stream);

            if (t == null)
                break;

            switch (t.getType())
            {
            case EmptyLine:
                continue;
            case Identifier:
                addChildren(new IdentifierNode(stream));
                break;
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
            case KeywordOr:
                addChildren(new OperationNode(stream, Operator.LogicalOr));
                break;
            case KeywordAnd:
                addChildren(new OperationNode(stream, Operator.LogicalAnd));
                break;
            case KeywordNot:
                addChildren(new OperationNode(stream, Operator.LogicalNot));
                break;
            case OpeningBracket:
                addChildren(new ExpressionOpeningBracket(stream, 0));
                break;
            case ClosingBracket:
                addChildren(new ExpressionClosingBracket(stream, 0));
                break;
            case Dot:
                addChildren(new MemberAccessNode(stream));
                break;
            case Comma:
                addChildren(new MultipleExpressionsNode(stream));
                break;
            case OperatorNew:
                addChildren(new OperatorNewNode(stream));
                break;

            case GreaterSign:
                addChildren(new OperationNode(stream, Operator.Greater));
                break;
            case LessSign:
                addChildren(new OperationNode(stream, Operator.Less));
                break;

            case EqualsSign:
                addChildren(new OperationNode(stream, Operator.Equal));
                break;
            case OpeningSquareBracket:
                {
                    addChildren(new IndexAccessNode(stream));
                }
                break;

            default:
                throw new BSLParsingException.UnexpectedToken(stream, t);
            }

        }

        return null;
    }


    /**
     * @throws
     * @throws ExpectedClosingBracket
     */
    private void reduce(List<AbsractBSLElementNode> children) throws BSLParsingException
    {
        while (true)
        {
            if (children.size() < 2)
                break;

            int sizeBefore = children.size();

            AbsractBSLElementNode node = findMostPrecdenceOperator(children);

            if (node == null)
                break;

            if (node instanceof ExpressionOpeningBracket || node instanceof ExpressionClosingBracket)
            {
                Tuple<Integer, ExpressionNode> compoundExpression =
                    parseBracketsExpression(node, children, ExpressionOpeningBracket.class,
                    ExpressionClosingBracket.class);

                int startIndex = compoundExpression.getFirst();
                ExpressionNode expression = compoundExpression.getSecond();

                if (startIndex > 0)
                {
                   var prevNode = children.get(startIndex - 1);

                   if (prevNode instanceof IdentifierNode)
                   {

                       if (startIndex > 1)
                       {
                           var prevNode2 = children.get(startIndex - 2);

                           if (prevNode2 instanceof OperatorNewNode)
                           {
                               children.remove((startIndex - 1));
                               children.remove((startIndex - 2));

                               children.add(startIndex - 2,
                                   new OperatorNewExpression((IdentifierNode)prevNode, expression));
                           }
                           else
                           {
                               children.remove(startIndex - 1);
                               children.add(startIndex - 1, new MethodCallNode((IdentifierNode)prevNode, expression));
                           }
                       }
                       else
                       {
                           children.remove(startIndex - 1);
                           children.add(startIndex - 1, new MethodCallNode((IdentifierNode)prevNode, expression));
                       }
                   }
                   else if (prevNode instanceof OperatorNewNode)
                   {
                       children.remove(startIndex - 1);
                       children.add(startIndex - 1, new OperatorNewExpression(expression));
                   }
                   else
                       children.add(startIndex, expression);

                }
                else
                    children.add(startIndex, expression);

            }
            else if (node instanceof MemberAccessNode)
            {
                int nodeIndex = children.indexOf(node);

                if (nodeIndex == 0)
                    throw new BSLParsingException.UnexpectedMemberRead();

                if (nodeIndex == children.size() - 1)
                    throw new BSLParsingException.UnexpectedMemberRead();

                var leftNode = children.get(nodeIndex - 1);
                var rightNode = children.get(nodeIndex + 1);

                var memberAcessExpression = new MemberAccessExpression(null);

                memberAcessExpression.setLeftNode(leftNode);
                memberAcessExpression.setRightNode(rightNode);

                children.remove(nodeIndex + 1);
                children.remove(nodeIndex);
                children.remove(nodeIndex - 1);

                children.add(nodeIndex - 1, memberAcessExpression);

            }
            else if (node instanceof OperationNode)
            {
                OperationNode operator = (OperationNode)node;

                int startIndex = children.indexOf(node);

                var sliceLeft = children.subList(0, startIndex);
                var sliceRight = children.subList(startIndex + 1, children.size());

                operator.setLeftNode(new ExpressionNode(sliceLeft));
                operator.setRightNode(new ExpressionNode(sliceRight));


                children.clear();
                children.add(operator);
            }
            else if (node instanceof IndexAccessNode)
            {
                IndexAccessNode iaNode = (IndexAccessNode)node;

                int nodeIndex = children.indexOf(node);

                if (nodeIndex == 0)
                    throw new BSLParsingException.UnexpectedMemberRead();

                var sliceLeft = children.subList(0, nodeIndex);
                // var leftNode = children.get(nodeIndex - 1);

                iaNode.setCollection(new ExpressionNode(sliceLeft));

                children.clear();

                children.add(iaNode);

            }
            else if (node instanceof OperatorNewNode)
            {
                int nodeIndex = children.indexOf(node);

                if (nodeIndex + 1 >= children.size())
                    throw new BSLParsingException.UnexpectedMemberRead();

                var classId = children.get(nodeIndex + 1);

                children.remove(nodeIndex + 1);
                children.remove(nodeIndex);
                children.add(nodeIndex, new OperatorNewExpression((IdentifierNode)classId, null));

            }

            if (children.size() == sizeBefore)
                throw new BSLParsingException();

        }
    }

    @Override
    public String serialize(ScriptVariant scriptVariant) throws Exception
    {
        StringBuilder builder = new StringBuilder();

        for (AbsractBSLElementNode node : getChildren())
        {
            // TODO: проверка на необходимость лепить перенос
            String nodeValue = node.serialize(scriptVariant);
            builder.append(nodeValue);

            if (getChildren().indexOf(node) != getChildren().size() - 1)
                builder.append(", "); //$NON-NLS-1$
        }

        if (mCompound)
            return "(" + builder.toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        else
            return builder.toString();
    }

    private List<List<AbsractBSLElementNode>> splitMultipleExpressions(List<AbsractBSLElementNode> input)
    {
        List<List<AbsractBSLElementNode>> result = new LinkedList<>();

        List<AbsractBSLElementNode> temp = new LinkedList<>();

        var iterator = input.iterator();

        int level = 0;

        while (iterator.hasNext())
        {
            var node = iterator.next();

            if (node instanceof ExpressionOpeningBracket)
                level++;

            if (node instanceof ExpressionClosingBracket)
                level--;

            if (node instanceof MultipleExpressionsNode && level == 0)
            {
                result.add(temp);
                temp = new LinkedList<>();
                continue;
            }

            temp.add(node);
        }

        if (temp.size() > 0)
            result.add(temp);

        return result;
    }

    @Override
    public String toString()
    {
        if (getChildren().size() == 0)
            return "<Bad or empty>"; //$NON-NLS-1$

        String result = ""; //$NON-NLS-1$

        for (var item : getChildren())
        {
            result += item.toString() + ","; //$NON-NLS-1$
        }

        //if (mCompound)
        //  result = "(" + result + ")"; //$NON-NLS-1$ //$NON-NLS-2$

        if (getChildren().size() > 1)
            return "{" + result.substring(0, result.length() - 1) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        else
            return result.substring(0, result.length() - 1);
    }

}
