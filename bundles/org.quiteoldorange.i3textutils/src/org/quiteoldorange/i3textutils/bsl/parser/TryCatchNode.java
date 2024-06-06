/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;

/**
 * @author ozolotarev
 *
 */
public class TryCatchNode
    extends AbsractBSLElementNode
{

    private Block mTryBlock;
    private Block mCatchBlock;

    /**
     * @param stream
     * @throws BSLParsingException
     */
    public TryCatchNode(Lexer stream) throws BSLParsingException
    {
        super(stream);

        // For Each Iterator From Collection Loop <Statement> EndLoop
        // Try <Code> Catch Handler EndCathh;

        mTryBlock = new Block(stream, Type.KeywordExcept);
        mCatchBlock = new Block(stream, Type.OperatorEndTry);

    }

    private class Block
        extends AbsractBSLElementNode
    {

        /**
         * @param stream
         * @throws BSLParsingException
         */
        public Block(Lexer stream, Token.Type endingToken) throws BSLParsingException
        {
            super(stream);

            ParseUntilEndingToken(stream, endingToken);
        }

    }
}
