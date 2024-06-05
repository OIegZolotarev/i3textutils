/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser.expressions;

import java.util.LinkedList;
import java.util.List;

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
    public enum Operator
    {
        Addition,
        Division,
        LogicalAnd,
        LogicalNot,
        LogicalOr,
        Modulo,
        Multiplication,
        Substraction,
        Greater,
        Less,
        GreaterOr,
        LessOr,
        NotEqual,
        Equal
    }

    private AbsractBSLElementNode mLeftNode;

    Operator mOperator;
    private AbsractBSLElementNode mRightNode;
    /**
     * @param stream
     */
    public OperationNode(Lexer stream, Operator op)
    {
        super(stream);

        mOperator = op;
    }

    @Override
    public List<AbsractBSLElementNode> getChildren()
    {
        List<AbsractBSLElementNode> result = new LinkedList<>();

        result.add(mLeftNode);
        result.add(mRightNode);

        return result;
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
        case Equal:
        case Greater:
        case GreaterOr:
        case Less:
        case LessOr:
        case NotEqual:
            return 15;
        case Addition:
        case Substraction:
            return 20;
        case Division:
        case Modulo:
        case Multiplication:
            return 30;

        default:
            break;

        }

        return 0;
    }

    @Override
    public String serialize(ScriptVariant scriptVariant) throws Exception
    {
        String opRepresentation = ""; //$NON-NLS-1$

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

        return String.format("%s %s %s", mLeftNode.serialize(scriptVariant), opRepresentation, //$NON-NLS-1$
            mRightNode.serialize(scriptVariant));
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
        case Equal:
            opRepresentation = "=";//$NON-NLS-1$
            break;
        case Greater:
            opRepresentation = ">";//$NON-NLS-1$
            break;
        case GreaterOr:
            opRepresentation = ">=";//$NON-NLS-1$
            break;
        case Less:
            opRepresentation = "<";//$NON-NLS-1$
            break;
        case LessOr:
            opRepresentation = "<=";//$NON-NLS-1$
            break;
        case NotEqual:
            opRepresentation = "<>";//$NON-NLS-1$
            break;
        default:
            break;

        }

        if (mLeftNode != null && mRightNode != null)
        {
            return String.format("(%s %s %s)", mLeftNode.toString(), opRepresentation, mRightNode.toString()); //$NON-NLS-1$
        }
        else return opRepresentation;
    }
}
