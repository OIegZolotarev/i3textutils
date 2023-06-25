/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

/**
 * @author ozolotarev
 *
 */
public class CommentNode
    extends AbsractBSLElementNode
{
    CommentNode(Lexer stream)
    {
        super(stream);
    }
}
