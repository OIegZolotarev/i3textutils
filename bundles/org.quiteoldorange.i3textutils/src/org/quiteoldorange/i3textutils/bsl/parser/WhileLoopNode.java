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
public class WhileLoopNode
    extends AbsractBSLElementNode
{
    @Override
    public String serialize(ScriptVariant scriptVariant) throws Exception
    {
        StringBuilder b = new StringBuilder();

        String prologue = String.format("%s %s %s\n", Token.getKeywordValue(Type.OperatorWhile, scriptVariant), //$NON-NLS-1$
            mConditionExpression.serialize(scriptVariant), Token.getKeywordValue(Type.KeywordLoop, scriptVariant));

        b.append(prologue);
        b.append(serializeChildren(scriptVariant, false));

        String epilogue = String.format("%s", Token.getKeywordValue(Type.OperatorEndLoop, scriptVariant)); //$NON-NLS-1$

        b.append(epilogue);

        return b.toString();
    }

    private ExpressionNode mConditionExpression;

    public WhileLoopNode(Lexer stream, AbsractBSLElementNode parent) throws BSLParsingException
    {
        super(stream);

        mConditionExpression = new ExpressionNode(stream, Type.KeywordLoop);
        checkTokenTracked(stream, Type.KeywordLoop);
        ParseUntilEndingToken(stream, Type.OperatorEndLoop);

    }
}
