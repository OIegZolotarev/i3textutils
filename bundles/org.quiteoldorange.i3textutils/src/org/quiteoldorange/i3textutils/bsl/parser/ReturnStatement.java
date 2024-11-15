/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.ExpressionNode;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class ReturnStatement
    extends AbsractBSLElementNode
{

    @Override
    public String serialize(ScriptVariant scriptVariant) throws Exception
    {
        if (mReturnExpression != null)
            return String.format("%s %s", Token.getKeywordValue(Type.KeywordReturn, scriptVariant),
                mReturnExpression.serialize(scriptVariant));
        else
            return String.format("%s", Token.getKeywordValue(Type.KeywordReturn, scriptVariant));
    }

    private ExpressionNode mReturnExpression;

    /**
     * @param stream
     * @param parent TODO
     * @throws BSLParsingException
     */
    public ReturnStatement(Lexer stream, AbsractBSLElementNode parent) throws BSLParsingException
    {
        super(stream);

        mReturnExpression = new ExpressionNode(stream, parent.validExpressionEndTokensForThisNode());
    }

}
