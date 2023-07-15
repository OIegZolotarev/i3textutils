/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class BSLRegionNode
    extends AbsractBSLElementNode
{
    @Override
    public String serialize(ScriptVariant variant)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s %s\n", Token.getKeywordValue(Type.PreprocessorRegion, variant), mRegionName)); //$NON-NLS-1$

        for (AbsractBSLElementNode node : getChildren())
        {
            builder.append(node.serialize(variant) + "\n"); //$NON-NLS-1$
        }

        builder.append(
            String.format("%s // %s\n", Token.getKeywordValue(Type.PreprocessorEndRegion, variant), mRegionName));

        return builder.toString();
    }

    private String mRegionName = null;

    public BSLRegionNode(Lexer stream) throws BSLParsingException
    {
        super(stream);

        var token = stream.parseNext();

        if (token == null)
            throw new BSLParsingException.UnexpectedEndOfStream();

        mRegionName = token.getValue();

        ParseUntilEndingToken(stream, Type.PreprocessorEndRegion);
    }
}
