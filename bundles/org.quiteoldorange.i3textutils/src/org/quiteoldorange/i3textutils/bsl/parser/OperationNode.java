/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

/**
 * @author ozolotarev
 *
 */
public class OperationNode
    extends AbsractBSLElementNode
{
    Operator mOperator;

    /**
     * @param stream
     */
    public OperationNode(Lexer stream, Operator op)
    {
        super(stream);

        mOperator = op;
    }

    public enum Operator
    {
        Addition,
        Substraction,
        Multiplication,
        Division,
        Modulo
    }
}
