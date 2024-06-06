/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.lexer;

import java.util.LinkedList;
import java.util.Stack;

import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.UnexpectedToken;

/**
 * @author ozolotarev
 *
 */
public class Lexer
{

    private boolean mLazyMode = false;

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
        String s = "\"\\/%()-=+;.,<>\"[]"; //$NON-NLS-1$
        return s.indexOf(sym) != -1;
    }

    public Token parseNext()
    {
        String accumulator = ""; //$NON-NLS-1$
        int tokenStart = mOffset;
        int startingRow = mRow;

        boolean atComment = false;
        boolean atString = false;

        while (true)
        {
            if (mOffset >= mSource.length())
            {
                break;
            }

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
                if (atString)
                {
                    do
                    {
                        mOffset++;
                        curChar = mSource.charAt(mOffset);
                    }
                    while (Character.isWhitespace(curChar));

                    if (curChar == '|')
                    {
                        mOffset++;
                        accumulator += '\n';
                        continue;
                    }

                }
                else
                {
                    mOffset++;
                    mRow++;
                    mColumn = 1;

                    if (accumulator.isEmpty())
                    {
                        accumulator += curChar;
                        Token.Type type = Token.CalculateTokenType(accumulator);
                        mTokensStack.push(new Token(type, accumulator, tokenStart, startingRow, mColumn));
                        return mTokensStack.peek();
                    }

                    atComment = false;
                }
            }

            if (curChar == '/' && nextChar == '/' && !atComment && !atString)
            {
                if (!accumulator.isEmpty())
                {
                    Token.Type type = Token.CalculateTokenType(accumulator);
                    mTokensStack.push(new Token(type, accumulator, tokenStart, startingRow, mColumn));
                    return mTokensStack.peek();
                }

                atComment = true;
            }

            if (atComment)
            {
                accumulator += curChar;
            }
            else if (Character.isWhitespace(curChar) && !atString)
            {
                if (!accumulator.isEmpty())
                {
                    Token.Type type = Token.CalculateTokenType(accumulator);
                    mTokensStack.push(new Token(type, accumulator, tokenStart, startingRow, mColumn));
                    return mTokensStack.peek();
                }
                else
                    tokenStart++;
            }
            else if (curChar == '"')
            {
                if (atString)
                {
                    mOffset++;

                    // 1С-овское экранирование кавычек - две подряд
                    if (nextChar == '"')
                    {
                        accumulator += "\"\""; //$NON-NLS-1$
                    }
                    else
                    {
                        Token.Type type = Token.Type.StringConstant;
                        mTokensStack.push(new Token(type, accumulator, tokenStart, startingRow, mColumn));
                        return mTokensStack.peek();
                    }
                }
                else
                {
                    tokenStart = mOffset;
                    startingRow = mRow;
                    atString = true;
                    accumulator = ""; //$NON-NLS-1$
                }
            }
            else if (isOneSymbolToken(curChar) && !atString)
            {

                if (!accumulator.isEmpty())
                {
                    Token.Type type = Token.CalculateTokenType(accumulator);
                    mTokensStack.push(new Token(type, accumulator, tokenStart, startingRow, mColumn));
                    return mTokensStack.peek();
                }

                mOffset++;

                accumulator += curChar;

                Token.Type type = Token.CalculateTokenType(accumulator);
                mTokensStack.push(new Token(type, accumulator, tokenStart, startingRow, mColumn));
                return mTokensStack.peek();
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
            mTokensStack.push(new Token(type, accumulator, tokenStart, startingRow, mColumn));
            return mTokensStack.peek();
        }

        return null;
    }

    public Token current()
    {
        if (mTokensStack.empty())
            return null;
        return mTokensStack.peek();
    }

    public Token peekNext()
    {
        Token result = parseNext();
        rollback();
        return result;
    }

    public Token rollback()
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

    public void parseAndCheckToken(Token.Type expectedType) throws UnexpectedToken
    {
        Token next = parseNext();

        if (next.getType() != expectedType)
            throw new BSLParsingException.UnexpectedToken(this, next, expectedType);
    }

    /**
     * @param tokens
     * @return
     */
    public String getTokensSource(LinkedList<Token> tokens)
    {
        Token first = tokens.getFirst();
        Token last = tokens.getLast();

        if (first == null || last == null)
            return null;

        return mSource.substring(first.getOffset(), last.getOffset() + last.getValue().length());
    }

    /**
     * @return the lazyMode
     */
    public boolean isLazyMode()
    {
        return mLazyMode;
    }

    /**
     * @param lazyMode the lazyMode to set
     */
    public void setLazyMode(boolean lazyMode)
    {
        mLazyMode = lazyMode;
    }
}
