/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

/**
 * @author ozolotarev
 *
 */
public class IfElseStatement
    extends AbstractIfElseStatement
{

    /**
     * @param stream
     * @param kind
     */
    public IfElseStatement(Lexer stream)
    {
        super(stream, AbstractIfElseStatement.sBSLCodeSet);
        // TODO Auto-generated constructor stub
    }

}
