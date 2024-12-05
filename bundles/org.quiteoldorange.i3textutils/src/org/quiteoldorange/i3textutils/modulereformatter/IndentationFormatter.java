/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter;

import java.nio.CharBuffer;
import java.util.HashMap;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;

/**
 * @author ozolotarev
 *
 */
public class IndentationFormatter
{
    private boolean mUseSpace;
    private int mSymbolsPerLevel;

    static private HashMap<Token.Type, Integer> mLevels;
    private int mLevel;

    public IndentationFormatter(int symbolsPerLevel, boolean useSpaces)
    {
        mSymbolsPerLevel = symbolsPerLevel;
        mUseSpace = useSpaces;
    }

    public String format(String source)
    {
        StringBuilder r = new StringBuilder();

        mLevel = 0;

        String lines[] = source.split("\n"); //$NON-NLS-1$

        for (String line : lines)
        {
            String trimmed = line.trim();

            int specialDelta = calculateIndentation(line);

            r.append(indentation(mLevel + specialDelta));
            r.append(trimmed);
            r.append('\n');

        }

        return r.toString();
    }

    /**
     * @param line
     * @return
     */
    private int calculateIndentation(String line)
    {
        Lexer l = new Lexer(line);

        int specialDelta = 0;

        while (true)
        {
            Token t = l.parseNext();

            if (t == null)
                break;

            Integer level = mLevels.get(t.getType());

            if (level == null)
                continue;
            else if (level == 1)
            {
                // Начало блочного оператора, начало остается на предыдущем отступе, новые строки с отступом
                specialDelta--;
                mLevel++;
            }
            else if (level == 0)
            {
                // Продолжение блочного оператора (Иначе, ИначеЕсли и т.д.) - ключевое слово на отступ назад, остальное без изменений
                specialDelta--;
            }
            else if (level == -1)
            {
                // Конец блочного оператора
                mLevel--;
            }

        }

        return specialDelta;
    }

    /**
     * @param level
     * @return
     */
    private String indentation(int level)
    {
        if (level < 1)
            return ""; //$NON-NLS-1$

        if (mUseSpace)
        {
            return CharBuffer.allocate(mSymbolsPerLevel * (level)).toString().replace('\0', ' ');
        }
        else
            return CharBuffer.allocate(mSymbolsPerLevel * (level)).toString().replace('\0', '\t');
    }

    static
    {
        mLevels = new HashMap<>();
        mLevels.put(Token.Type.BeginFunction, 1);
        mLevels.put(Token.Type.BeginProcedure, 1);
        mLevels.put(Token.Type.OperatorWhile, 1);
        mLevels.put(Token.Type.OperatorIf, 1);
        mLevels.put(Token.Type.OperatorTry, 1);

        mLevels.put(Token.Type.KeywordExcept, 0);
        mLevels.put(Token.Type.OperatorElse, 0);
        mLevels.put(Token.Type.OperatorElseIf, 0);

        mLevels.put(Token.Type.EndFunction, -1);
        mLevels.put(Token.Type.EndProcedure, -1);
        mLevels.put(Token.Type.OperatorEndLoop, -1);
        mLevels.put(Token.Type.OperatorEndTry, -1);
        mLevels.put(Token.Type.OperatorEndIf, -1);

    }
}
