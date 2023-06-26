/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;

/**
 * @author ozolotarev
 *
 */
public class ExpressionNode
    extends AbsractBSLElementNode
{
    public ExpressionNode(Lexer stream, Token.Type endToken)
    {
        super(stream);


    }
}
