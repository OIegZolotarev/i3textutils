/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.UnexpectedToken;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

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

    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        switch (mType)
        {
        case After:
            return String.format("%s(\"%s\")\n", Token.getKeywordValue(Type.AnnotationAfter, scriptVariant), //$NON-NLS-1$
                mExtendedMethodName);
        case Around:
            return String.format("%s(\"%s\")\n", Token.getKeywordValue(Type.AnnotationAround, scriptVariant), //$NON-NLS-1$
                mExtendedMethodName);
        case AtClient:
            return String.format("%s\n", Token.getKeywordValue(Type.AnnotationAtClient, scriptVariant)); //$NON-NLS-1$
        case AtClientAtServerNoContext:
            return String.format("%s\n", //$NON-NLS-1$
                Token.getKeywordValue(Type.AnnotationAtClientAtServerNoContext, scriptVariant));
        case AtServer:
            return String.format("%s\n", Token.getKeywordValue(Type.AnnotationAtServer, scriptVariant)); //$NON-NLS-1$
        case AtServerNoContext:
            return String.format("%s\n", Token.getKeywordValue(Type.AnnotationAtServerNoContext, scriptVariant)); //$NON-NLS-1$
        case Before:
            return String.format("%s(\"%s\")\n", Token.getKeywordValue(Type.AnnotationBefore, scriptVariant), //$NON-NLS-1$
                mExtendedMethodName);
        default:
            return ""; //$NON-NLS-1$
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
