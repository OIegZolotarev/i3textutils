/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.exceptions.BSLParsingException;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;

/**
 * @author ozolotarev
 *
 */
public class BSLRegionElement
    extends AbsractBSLElementNode
{
    private String mRegionName = null;

    public BSLRegionElement(Lexer stream) throws BSLParsingException
    {
        super(stream);

        var token = stream.parseNext();

        if (token == null)
            throw new BSLParsingException.UnexpectedEndOfStream();

        mRegionName = token.getValue();

        ParseUntilEndingToken(stream, Type.PreprocessorEndRegion);
    }
}
