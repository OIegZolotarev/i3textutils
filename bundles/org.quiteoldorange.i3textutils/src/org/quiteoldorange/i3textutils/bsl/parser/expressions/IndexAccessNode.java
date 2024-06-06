/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser.expressions;

import java.util.HashSet;
import java.util.Set;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException;

/**
 * @author ozolotarev
 *
 */
public class IndexAccessNode
    extends AbsractBSLElementNode
    implements IOperationNode
{


    private ExpressionNode mExpression;
    private AbsractBSLElementNode mCollection;

    /**
     * @param stream
     * @throws BSLParsingException
     */
    public IndexAccessNode(Lexer stream) throws BSLParsingException
    {
        super(stream);
        // TODO Auto-generated constructor stub

        Set<Token.Type> endTokens = new HashSet<>();
        endTokens.add(Token.Type.ClosingSquareBracket);

        mExpression = new ExpressionNode(stream, endTokens);
        checkTokenTracked(stream, Token.Type.ClosingSquareBracket);

    }

    @Override
    public int precedence()
    {
        // TODO Auto-generated method stub
        return 30;
    }

    @Override
    public String toString()
    { //
        if (mExpression == null || mCollection == null)
            return String.format("<ошибка разбора>"); //$NON-NLS-1$
        else
            return String.format("%s[%s]", mCollection.toString(), mExpression.toString()); //$NON-NLS-1$
    }

    /**
     * @param leftNode
     */
    public void setCollection(AbsractBSLElementNode leftNode)
    {
        mCollection = leftNode;
    }

}
