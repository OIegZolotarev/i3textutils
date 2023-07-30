/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

/**
 * @author ozolotarev
 *
 */
public class IdentifierNode
    extends AbsractBSLElementNode
{

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
