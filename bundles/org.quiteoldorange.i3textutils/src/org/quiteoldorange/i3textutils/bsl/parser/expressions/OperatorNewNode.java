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
public class OperatorNewNode
    extends AbsractBSLElementNode
{

    @Override
    public String toString()
    {
        return "Новый"; //$NON-NLS-1$
    }

    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        // TODO Auto-generated method stub
        return Token.getKeywordValue(Type.OperatorNew, scriptVariant);
    }

    /**
     * @param stream
     */
    public OperatorNewNode(Lexer stream)
    {
        super(stream);
        // TODO Auto-generated constructor stub
    }

}
