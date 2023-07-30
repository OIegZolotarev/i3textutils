/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

/**
 * @author ozolotarev
 *
 */
public class PrepropcessorIfElseStatementNode
    extends AbstractIfElseStatement
{
    public PrepropcessorIfElseStatementNode(Lexer stream) throws BSLParsingException
    {
        super(stream, AbstractIfElseStatement.sPreprocessorSet);

    }
}
