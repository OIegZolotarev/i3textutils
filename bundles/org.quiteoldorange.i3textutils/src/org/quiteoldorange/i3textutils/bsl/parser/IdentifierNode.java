/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class IdentifierNode
    extends AbsractBSLElementNode
{

    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        return mTokens.get(0).getValue();
    }

    /**
     * @param stream
     */
    public IdentifierNode(Lexer stream)
    {
        super(stream);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString()
    {
        return mTokens.get(0).getValue();
    }
}
