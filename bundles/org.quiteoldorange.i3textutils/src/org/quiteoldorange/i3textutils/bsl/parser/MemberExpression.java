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
public class MemberExpression
    extends AbsractBSLElementNode
{

    String mMemberName;

    /**
     * @param stream
     * @throws BSLParsingException
     */
    public MemberExpression(Lexer stream) throws BSLParsingException
    {
        super(stream);

        mMemberName = mTokens.get(0).getValue();

        var token = stream.peekNext();

        if (token.getType() == Type.Dot)
        {
            readTokenTracked(stream);
            addChildren(ParseNode(stream));
        }
    }

}
