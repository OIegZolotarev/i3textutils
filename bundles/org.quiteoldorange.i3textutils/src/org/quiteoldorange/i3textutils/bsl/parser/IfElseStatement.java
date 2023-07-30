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
     * @throws BSLParsingException
     */
    public IfElseStatement(Lexer stream) throws BSLParsingException
    {
        super(stream, AbstractIfElseStatement.sBSLCodeSet);
        // TODO Auto-generated constructor stub
    }

}
