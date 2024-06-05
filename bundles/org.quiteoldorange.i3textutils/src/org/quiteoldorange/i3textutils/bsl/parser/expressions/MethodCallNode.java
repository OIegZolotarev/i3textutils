/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser.expressions;

import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;
import org.quiteoldorange.i3textutils.bsl.parser.IdentifierNode;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class MethodCallNode
    extends AbsractBSLElementNode
{

    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        return serialize(scriptVariant, false);
    }

    public String serialize(ScriptVariant scriptVariant, boolean putArgsOnNewLines) throws Exception
    {
        assert (mMethodName != null && mArgs != null);

        StringBuilder builder = new StringBuilder();

        for (AbsractBSLElementNode node : mArgs.getChildren())
        {
            // TODO: проверка на необходимость лепить перенос
            String nodeValue = node.serialize(scriptVariant);
            builder.append(nodeValue);

            boolean addComma = mArgs.getChildren().indexOf(node) != mArgs.getChildren().size() - 1;

            if (addComma)
                builder.append(", "); //$NON-NLS-1$

            if (putArgsOnNewLines && addComma)
                builder.append("\n"); //$NON-NLS-1$
        }

        return String.format("%s(%s)", mMethodName.serialize(scriptVariant), builder.toString()); //$NON-NLS-1$
    }

    @Override
    public String toString()
    {
        if (mMethodName != null && mArgs != null)
            return mMethodName.toString() + "(" + mArgs.toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        else
            return ""; //$NON-NLS-1$
    }

    IdentifierNode mMethodName = null;
    ExpressionNode mArgs = null;

    /**
     * @param prevNode
     * @param expression
     */
    public MethodCallNode(IdentifierNode methodNode, ExpressionNode expression)
    {
        super(null);

        mArgs = expression;
        mMethodName = methodNode;
    }

    /**
     * @return
     */
    public String methodName()
    {
        return mMethodName.value();
    }

    /**
     * @return
     */
    public ExpressionNode getArgumentsExpression()
    {
        return mArgs;
    }

}
