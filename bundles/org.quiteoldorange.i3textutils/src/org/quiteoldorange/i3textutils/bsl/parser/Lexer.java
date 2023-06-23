/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.Stack;

/**
 * @author ozolotarev
 *
 */
public class Lexer
{
    private int mOffset;
    private Stack<Token> mTokensStack;

    public Lexer(String source)
    {
        mOffset = 0;
        mTokensStack = new Stack<>();
    }

    Token parseNext()
    {
        Token result = new Token(null,null, 0,0,0);

        mTokensStack.push(result);
        return result;
    }

    Token top()
    {
        return mTokensStack.peek();
    }

    Token pop()
    {
        Token r = mTokensStack.pop();
        mOffset = r.getOffset();
        return r;
    }

    public int getOffset()
    {
        return mOffset;
    }

    public void setOffset(int offset)
    {
        mOffset = offset;
    }
}
