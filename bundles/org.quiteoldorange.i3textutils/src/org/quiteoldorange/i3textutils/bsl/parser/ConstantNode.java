/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

/**
 * @author ozolotarev
 *
 */
public class ConstantNode
    extends AbsractBSLElementNode
{

    /**
     * @param stream
     */
    public ConstantNode(Lexer stream)
    {
        super(stream);
    }

    public String getAsString()
    {
        assert (mTokens.size() > 0);

        return mTokens.get(0).getValue();
    }

}
