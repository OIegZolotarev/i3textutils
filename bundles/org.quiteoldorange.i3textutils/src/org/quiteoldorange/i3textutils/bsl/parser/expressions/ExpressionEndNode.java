/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser.expressions;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class ExpressionEndNode
    extends AbsractBSLElementNode
{

    @Override
    public String serialize(ScriptVariant scriptVariant) throws Exception
    {
        return ";"; //$NON-NLS-1$
    }

    /**
     * @param stream
     */
    public ExpressionEndNode(Lexer stream)
    {
        super(stream);
        // TODO Auto-generated constructor stub
    }

}
