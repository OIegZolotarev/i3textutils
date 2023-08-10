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

    public String serialize(ScriptVariant scriptVariant, boolean putArgsOnNewLines)
    {
        assert (mMethod != null && mArgs != null);

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

        return String.format("%s(%s)", mMethod.serialize(scriptVariant), builder.toString());
    }

    @Override
    public String toString()
    {
        if (mMethod != null && mArgs != null)
            return mMethod.toString() + " " + mArgs.toString(); //$NON-NLS-1$
        else
            return ""; //$NON-NLS-1$
    }

    IdentifierNode mMethod = null;
    ExpressionNode mArgs = null;

    /**
     * @param prevNode
     * @param expression
     */
    public MethodCallNode(IdentifierNode methodNode, ExpressionNode expression)
    {
        super(null);

        mArgs = expression;
        mMethod = methodNode;
    }


}
