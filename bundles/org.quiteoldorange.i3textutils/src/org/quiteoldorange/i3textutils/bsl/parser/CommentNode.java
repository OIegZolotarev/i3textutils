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
public class CommentNode
    extends AbsractBSLElementNode
{
    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        return mTokens.get(0).getValue();
    }

    CommentNode(Lexer stream, AbsractBSLElementNode parent)
    {
        super(stream);
    }
}
