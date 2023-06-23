/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.lexer;

import java.util.Stack;

/**
 * @author ozolotarev
 *
 */
public class Lexer
{
    private int mOffset;
    private int mRow;
    private int mColumn;

    private String mSource;

    private Stack<Token> mTokensStack;

    public Lexer(String source)
    {
        mOffset = 0;
        mRow = 1;
        mColumn = 1;

        mSource = source;


        mTokensStack = new Stack<>();

    }

    private boolean isOneSymbolToken(Character sym)
    {
        String s = "\"\\/%()-=+;.,<>\"";
        return s.indexOf(sym) != -1;
    }

    public Token parseNext()
    {
        Token result = new Token(null,null, 0,0,0);
        String accumulator = ""; //$NON-NLS-1$
        int tokenStart = mOffset;

        boolean atComment = false;
        boolean atString = false;

        while (true)
        {
            if (mOffset == mSource.length() - 1)
                break;

            Character curChar = mSource.charAt(mOffset);
            Character nextChar = 0;

            try
            {
                nextChar = mSource.charAt(mOffset + 1);
            }
            catch (IndexOutOfBoundsException e)
            {
                nextChar = 0;
            }

            if (curChar == '\n')
            {
                mColumn = 1;
                mRow++;

                atComment = false;
            }

            if (curChar == '/' && nextChar == '/' && !atComment && !atString)
            {
                if (!accumulator.isEmpty())
                {
                    Token.Type type = Token.CalculateTokenType(accumulator);
                    mTokensStack.push(new Token(type, accumulator, tokenStart, mRow, mColumn));
                    return mTokensStack.peek();
                }

                atComment = true;
            }

            if (atComment)
                accumulator += curChar;
            else if (Character.isWhitespace(curChar) && !atString)
            {
                if (!accumulator.isEmpty())
                {
                    Token.Type type = Token.CalculateTokenType(accumulator);
                    mTokensStack.push(new Token(type, accumulator, tokenStart, mRow, mColumn));
                    return mTokensStack.peek();
                }
            }
            else if (isOneSymbolToken(curChar) && !atString)
            {
                if (!accumulator.isEmpty())
                {
                    Token.Type type = Token.CalculateTokenType(accumulator);
                    mTokensStack.push(new Token(type, accumulator, tokenStart, mRow, mColumn));
                    return mTokensStack.peek();
                }

                mOffset++;

                accumulator += curChar;

                Token.Type type = Token.CalculateTokenType(accumulator);
                mTokensStack.push(new Token(type, accumulator, tokenStart, mRow, mColumn));
                return mTokensStack.peek();
            }
            else if (curChar == '"')
            {
                if (atString)
                {
                    // 1С-овское экранирование кавычек - две подряд
                    if (nextChar == '"')
                    {
                        accumulator += "\"\"";
                        mOffset++;
                    }
                    else
                    {
                        Token.Type type = Token.Type.StringConstant;
                        mTokensStack.push(new Token(type, accumulator, tokenStart, mRow, mColumn));
                        return mTokensStack.peek();
                    }
                }
                else
                {
                    tokenStart = mOffset;
                    atString = true;
                    accumulator = ""; //$NON-NLS-1$
                }
            }
            else
            {
                accumulator += curChar;
            }

            mColumn++;
            mOffset++;

        }

        if (!accumulator.isEmpty())
        {
            Token.Type type = Token.CalculateTokenType(accumulator);
            mTokensStack.push(new Token(type, accumulator, tokenStart, mRow, mColumn));
            return mTokensStack.peek();
        }

        return null;
    }

    Token top()
    {
        return mTokensStack.peek();
    }

    Token pop()
    {
        Token r = mTokensStack.pop();
        mOffset = r.getOffset();

        mRow = r.getRow();
        mColumn = r.getColumn();

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
