/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.ExpressionNode;

/**
 * @author ozolotarev
 *
 */
public class ForEachLoopNode
    extends AbsractBSLElementNode
{


    private ExpressionNode mIterator;
    private ExpressionNode mCollection;

    /**
     * @param stream
     * @param parent TODO
     * @throws BSLParsingException
     */
    public ForEachLoopNode(Lexer stream, AbsractBSLElementNode parent) throws BSLParsingException
    {
        super(stream);

        // For Each Iterator From Collection Loop <Statement> EndLoop

        checkTokenTracked(stream, Type.KeywordEach);

        mIterator = new ExpressionNode(stream, Type.KeywordFrom);

        checkTokenTracked(stream, Type.KeywordFrom);

        mCollection = new ExpressionNode(stream, Type.KeywordLoop);

        checkTokenTracked(stream, Type.KeywordLoop);

        ParseUntilEndingToken(stream, Type.OperatorEndLoop);

    }

}
