/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

/**
 * @author ozolotarev
 *
 */
public class EmptyLineNode
    extends AbsractBSLElementNode
{

    @Override
    public String toString()
    {
        return "<Пустая строка>"; //$NON-NLS-1$
    }

    /**
     * @param stream
     * @param parent TODO
     */
    public EmptyLineNode(Lexer stream, AbsractBSLElementNode parent)
    {
        super(stream);
        // TODO Auto-generated constructor stub
    }

}
