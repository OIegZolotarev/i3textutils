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
public class AnnotationNode
    extends AbsractBSLElementNode
{

    enum Types
    {
        AtClient,
        AtServer,
        AtClientAtServerNoContext,
        AtServerNoContext,
        Before,
        After,
        Around
    }

    Types mType;
    String mExtendedMethodName;

    /**
     * @param stream
     * @throws UnexpectedToken
     */
    public AnnotationNode(Lexer stream) throws UnexpectedToken
    {
        super(stream);

        switch (mTokens.get(0).getType())
        {
        case AnnotationAfter:
            mType = Types.After;
            ParseMethodName(stream);
            break;
        case AnnotationAround:
            mType = Types.Around;
            ParseMethodName(stream);
            break;
        case AnnotationAtClient:
            mType = Types.AtClient;
            break;
        case AnnotationBefore:
            mType = Types.Before;
            ParseMethodName(stream);
            break;
        case AnnotationAtClientAtServerNoContext:
            mType = Types.AtClientAtServerNoContext;
            break;
        case AnnotationAtServer:
            mType = Types.AtServer;
            break;
        case AnnotationAtServerNoContext:
            mType = Types.AtServerNoContext;
            break;
        default:
            break;
        }

    }

    /**
     * @param stream
     * @throws UnexpectedToken
     */
    private void ParseMethodName(Lexer stream) throws UnexpectedToken
    {
        checkTokenTracked(stream, Type.OpeningBracket);

        var token = readTokenTracked(stream);
        mExtendedMethodName = token.getValue();

        checkTokenTracked(stream, Type.ClosingBracket);


    }

}
