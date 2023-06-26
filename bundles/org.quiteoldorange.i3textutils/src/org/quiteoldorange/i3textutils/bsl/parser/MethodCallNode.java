/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.UnexpectedToken;

/**
 * @author ozolotarev
 *
 */
public class MethodCallNode
    extends AbsractBSLElementNode
{
    String mMethodName;


    /**
     * @param stream
     * @throws UnexpectedToken
     */
    public MethodCallNode(Lexer stream) throws BSLParsingException
    {
        super(stream);
        // TODO Auto-generated constructor stub

        var token = readTokenTracked(stream);

        mMethodName = token.getValue();
        checkTokenTracked(stream, Type.OpeningBracket);

        while (true)
        {
            token = stream.peekNext();

            if (token.getType() == Type.ClosingBracket)
            {
                readTokenTracked(stream);
                break;
            }

            AbsractBSLElementNode newNode = ParseNode(stream);
            mChildren.add(newNode);
        }

    }

}
