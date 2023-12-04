/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;

/**
 * @author ozolotarev
 *
 */
public class IfElseStatementNode
    extends AbstractIfElseStatement
{

    /**
     * @param stream
     * @param parent TODO
     * @param kind
     * @throws BSLParsingException
     */
    public IfElseStatementNode(Lexer stream, AbsractBSLElementNode parent) throws BSLParsingException
    {
        super(stream, AbstractIfElseStatement.sBSLCodeSet);
        // TODO Auto-generated constructor stub

        addNodeEndingToken(Type.OperatorEndIf);
    }

}
