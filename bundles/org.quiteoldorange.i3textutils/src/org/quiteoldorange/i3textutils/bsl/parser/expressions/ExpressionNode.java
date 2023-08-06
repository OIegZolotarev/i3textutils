/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser.expressions;

import java.util.LinkedList;
import java.util.List;

import org.quiteoldorange.i3textutils.Tuple;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.ExpectedClosingBracket;
import org.quiteoldorange.i3textutils.bsl.parser.ConstantNode;
import org.quiteoldorange.i3textutils.bsl.parser.IdentifierNode;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.OperationNode.Operator;

/**
 * @author ozolotarev
 *
 */
public class ExpressionNode
    extends AbsractBSLElementNode
{
    private boolean mCompound;

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
            default:
                throw new BSLParsingException.UnexpectedToken(stream, t);
            }

        }

        return null;
    }

    public ExpressionNode(Lexer stream, Token.Type endToken) throws BSLParsingException
    {
        super(stream);

        ParseExpressionNode(stream, endToken);
        reduce();
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

    /**
     * @throws
     * @throws ExpectedClosingBracket
     */
    private void reduce() throws BSLParsingException
    {
        var children = getChildren();

        while (true)
        {
            if (children.size() < 2)
                break;

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

                //          if (startIndex > 0)
                //        {
                //                var prevNode = children.get(startIndex - 1);

                //              }
//                else
                    children.add(startIndex, expression);

            }
            else if (node instanceof MemberAccessNode)
            {
                MemberAccessNode memberNode = (MemberAccessNode)node;
                int nodeIndex = children.indexOf(node);

                if (nodeIndex == 0)
                    throw new BSLParsingException.UnexpectedMemberRead();

                if (nodeIndex == children.size() - 1)
                    throw new BSLParsingException.UnexpectedMemberRead();

                var leftNode = children.get(nodeIndex - 1);
                var rightNode = children.get(nodeIndex + 1);

                // TODO: MemberAccessExpression

                memberNode.setLeftNode(leftNode);
                memberNode.setRightNode(rightNode);

                children.remove(nodeIndex + 1);
                children.remove(nodeIndex);
                children.remove(nodeIndex - 1);

            }
            else if (node instanceof OperationNode)
            {
                OperationNode operator = (OperationNode)node;

                int startIndex = children.indexOf(node);

                var sliceLeft = children.subList(0, startIndex);
                var sliceRight = children.subList(startIndex + 1, children.size());

                operator.setLeftNode(new ExpressionNode(sliceLeft));
                operator.setRightNode(new ExpressionNode(sliceRight));

                operator.toString();
                children.clear();
                children.add(operator);
            }

        }
    }

    @Override
    public String toString()
    {
        if (getChildren().size() == 0)
            return "<Bad expression>"; //$NON-NLS-1$


        var node = getChildren().get(0);
        String result = node.toString();

        //if (mCompound)
        //  result = "(" + result + ")"; //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    /**
     * @param slice
     * @throws ExpectedClosingBracket
     */
    public ExpressionNode(List<AbsractBSLElementNode> slice) throws BSLParsingException
    {
        super(null);

        setChildren(new LinkedList<>(slice));
        reduce();
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

        var iterator = children.listIterator(children.indexOf(startNode) + (reverse ? -1 : 1));

        while (reverse ? iterator.hasPrevious() : iterator.hasNext())
        {
            AbsractBSLElementNode node = reverse ? iterator.previous() : iterator.next();

            if (node.getClass() == (reverse ? openingBracketType : endingBracketType) && level == 0)
                return node;
            else if (node.getClass() == endingBracketType && level != 0)
                level--;

            if (node.getClass() == openingBracketType)
                level++;
        }

        throw new BSLParsingException.ExpectedClosingBracket();

    }

    public AbsractBSLElementNode findMostPrecdenceOperator(List<AbsractBSLElementNode> nodes)
    {
        AbsractBSLElementNode best = null;
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
}
