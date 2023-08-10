/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser.expressions;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class OperationNode
    extends AbsractBSLElementNode
    implements IOperationNode
{
    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        String opRepresentation = "";

        switch (mOperator)
        {
        case LogicalAnd:
            opRepresentation = Token.getKeywordValue(Type.KeywordAnd, scriptVariant);
            break;
        case LogicalNot:
            opRepresentation = Token.getKeywordValue(Type.KeywordNot, scriptVariant);
            break;
        case LogicalOr:
            opRepresentation = Token.getKeywordValue(Type.KeywordOr, scriptVariant);
            break;
        case Addition:
            opRepresentation = "+";//$NON-NLS-1$
            break;
        case Substraction:
            opRepresentation = "-";//$NON-NLS-1$
            break;
        case Division:
            opRepresentation = "/";//$NON-NLS-1$
            break;
        case Modulo:
            opRepresentation = "%";//$NON-NLS-1$
            break;
        case Multiplication:
            opRepresentation = "*";//$NON-NLS-1$
            break;
        default:
            return "Unknown op"; //$NON-NLS-1$
        }

        return String.format("%s %s %s", mLeftNode.serialize(scriptVariant), opRepresentation,
            mRightNode.serialize(scriptVariant));
    }

    Operator mOperator;
    private AbsractBSLElementNode mLeftNode;
    private AbsractBSLElementNode mRightNode;

    /**
     * @param stream
     */
    public OperationNode(Lexer stream, Operator op)
    {
        super(stream);

        mOperator = op;
    }

    public enum Operator
    {
        Addition,
        Substraction,
        Multiplication,
        Division,
        Modulo,
        LogicalOr,
        LogicalAnd,
        LogicalNot
    }

    @Override
    public int precedence()
    {

        switch (mOperator)
        {
        case LogicalAnd:
        case LogicalNot:
        case LogicalOr:
            return 10;
        case Addition:
        case Substraction:
            return 15;
        case Division:
        case Modulo:
        case Multiplication:
            return 20;
        default:
            break;
        }

        return 0;
    }

    public void setLeftNode(AbsractBSLElementNode node)
    {
        mLeftNode = node;
    }

    public void setRightNode(AbsractBSLElementNode node)
    {
        mRightNode = node;
    }

    @Override
    public String toString()
    {
        String result = "";
        String opRepresentation = null;

        switch (mOperator)
        {
        case LogicalAnd:
            opRepresentation = "AND";//$NON-NLS-1$
            break;
        case LogicalNot:
            opRepresentation = "NOT";//$NON-NLS-1$
            break;
        case LogicalOr:
            opRepresentation = "OR";//$NON-NLS-1$
            break;
        case Addition:
            opRepresentation = "+";//$NON-NLS-1$
            break;
        case Substraction:
            opRepresentation = "-";//$NON-NLS-1$
            break;
        case Division:
            opRepresentation = "/";//$NON-NLS-1$
            break;
        case Modulo:
            opRepresentation = "%";//$NON-NLS-1$
            break;
        case Multiplication:
            opRepresentation = "*";//$NON-NLS-1$
            break;
        default:
            return "Unknown op"; //$NON-NLS-1$
        }

        if (mLeftNode != null && mRightNode != null)
        {
            return String.format("(%s %s %s)", mLeftNode.toString(), opRepresentation, mRightNode.toString()); //$NON-NLS-1$
        }
        else return opRepresentation;
    }
}
