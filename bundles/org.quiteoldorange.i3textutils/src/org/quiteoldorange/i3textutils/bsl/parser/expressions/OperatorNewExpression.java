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
public class OperatorNewExpression
    extends AbsractBSLElementNode
{

    @Override
    public String serialize(ScriptVariant scriptVariant) throws Exception
    {
        // TODO Auto-generated method stub
        return super.serialize(scriptVariant);
    }

    @Override
    public String toString()
    { //
        if (mClass == null)
            return String.format("Новый %s", mArgs.toString()); //$NON-NLS-1$
        else
            return String.format("Новый %s(%s)", mClass.toString(), mArgs.toString()); //$NON-NLS-1$
    }

    private ExpressionNode mArgs = null;
    private IdentifierNode mClass = null;

    /**
     * @param stream
     */
    public OperatorNewExpression(ExpressionNode args)
    {
        super(null);
        mArgs = args;
    }

    /**
     * @param stream
     */
    public OperatorNewExpression(IdentifierNode classId, ExpressionNode args)
    {
        super(null);
        mArgs = args;
        mClass = classId;
    }

}
