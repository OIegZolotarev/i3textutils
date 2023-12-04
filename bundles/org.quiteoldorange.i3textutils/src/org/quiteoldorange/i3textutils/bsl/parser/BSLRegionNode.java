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
    private int mIdealOrder;

    @Override
    public String serialize(ScriptVariant variant)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s %s\n", Token.getKeywordValue(Type.PreprocessorRegion, variant), mRegionName)); //$NON-NLS-1$

        for (AbsractBSLElementNode node : getChildren())
        {
            builder.append(node.serialize(variant) + "\n"); //$NON-NLS-1$
        }

        // TODO: проверить что у области нет комментария, иначе будет двоить
        //       проверить что впереди нет "EmptyLineNode"
        builder.append(
            String.format("%s // %s\n", Token.getKeywordValue(Type.PreprocessorEndRegion, variant), mRegionName)); //$NON-NLS-1$

        return builder.toString();
    }

    private String mRegionName = null;

    public BSLRegionNode(Lexer stream, AbsractBSLElementNode parent) throws BSLParsingException
    {
        super(stream);

        var token = stream.parseNext();

        if (token == null)
            throw new BSLParsingException.UnexpectedEndOfStream();

        mRegionName = token.getValue();

        ParseUntilEndingToken(stream, Type.PreprocessorEndRegion);
    }

    public int getIdealOrder()
    {
        return mIdealOrder;
    }

    public void setIdealOrder(int idealOrder)
    {
        mIdealOrder = idealOrder;
    }

    /**
     * @return
     */
    public String getName()
    {
        return mRegionName;
    }

    @Override
    public String toString()
    {

        return String.format("[Область: %s]", mRegionName);
    }

}
