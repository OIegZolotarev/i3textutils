/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

/**
 * @author ozolotarev
 *
 */
public class PrepropcessorIfElseStatement
    extends AbstractIfElseStatement
{
    public PrepropcessorIfElseStatement(Lexer stream)
    {
        super(stream, AbstractIfElseStatement.sPreprocessorSet);

    }
}
