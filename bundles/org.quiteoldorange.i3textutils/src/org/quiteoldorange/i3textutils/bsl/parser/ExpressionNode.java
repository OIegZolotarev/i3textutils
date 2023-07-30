/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.parser.OperationNode.Operator;

/**
 * @author ozolotarev
 *
 */
public class ExpressionNode
    extends AbsractBSLElementNode
{
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
    }
}
