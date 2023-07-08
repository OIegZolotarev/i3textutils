/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.lexer;

import java.util.HashMap;

import org.quiteoldorange.i3textutils.StringUtils;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class Token
{
    public enum Type
    {
        Annotation,
        Identifier,
        NumericConstant,
        StringConstant,
        DateConstant,
        Comment,
        // Токены с конкретным строковым значением
        BeginProcedure,
        BeginFunction,
        EndProcedure,
        EndFunction,
        EqualsSign,
        OpeningBracket,
        ClosingBracket,
        Export,
        Comma,
        ExpressionEnd,
        PlusSign,
        MinusSign,
        MultiplicationSign,
        DivisionSign,
        Dot,
        BooleanConst,
        OperatorNew,
        OperatorIf,
        OperatorThen,
        OperatorElse,
        OperatorElseIf,
        OperatorEndIf,
        LessSign,
        GreateSign,
        OperatorFor,
        OperatorWhile,
        OperatorEndLoop,
        OperatorTry,
        OperatorEndTry,
        PreprocessorIf,
        PreprocessorThen,
        PreprocessorElseIf,
        PreprocessorElse,
        PreprocessorEndIf,
        PreprocessorInsert,
        PreprocessorEndInsert,
        PreprocessorRemove,
        PreprocessorEndRemove,
        PreprocessorRegion,
        PreprocessorEndRegion,
        KeywordAnd,
        KeywordOr,
        KeywordNot,
        KeywordVar,
        KeywordLoop,
        KeywordEach,
        KeywordVal,
        KeywordExcept,
        ModuloSign,
        KeywordGoto,
        Tilde,
        Colon,
        KeywordAsynch,
        KeywordWait,

    }

    private Type mType;

    private String mValue;
    private int mOffset;
    private int mRow;
    private int mColumn;

    public Token(Type type, String value, int offset, int row, int column)
    {
        mValue = value;
        mOffset = offset;
        mRow = row;
        mColumn = column;
        mType = type;
    }

    public Type getType()
    {
        return mType;
    }

    public String getValue()
    {
        return mValue;
    }

    public int getOffset()
    {
        return mOffset;
    }

    public int getRow()
    {
        return mRow;
    }

    public int getColumn()
    {
        return mColumn;
    }

    public String toString(ScriptVariant scriptLang)
    {
        var mapping = sTokenDictionary.get(mType);

        if (mapping != null)
        {
            return mapping.getValue(scriptLang);
        }

        return mValue;
    }

    final static class KeywordDictionaryEntry
    {
        private String mKeywordRU;
        private String mKeywordEN;

        /**
         *
         */
        public KeywordDictionaryEntry(String keywordRU, String keywordEN)
        {
            mKeywordRU = keywordRU;
            mKeywordEN = keywordEN;
        }

        String getValue(ScriptVariant scriptVariant)
        {
            switch (scriptVariant)
            {
            case ENGLISH:
                return mKeywordEN;
            case RUSSIAN:
                return mKeywordRU;
            default:
                return mKeywordRU;
            }
        }
    }


    public static Type CalculateTokenType(String tokenValue)
    {
        Type keywordType = sTokenMappings.get(tokenValue.toUpperCase());

        if (keywordType != null)
            return keywordType;

        if (tokenValue.length() > 2)
            if (tokenValue.substring(0, 2).equals("//")) //$NON-NLS-1$
                return Type.Comment;

        if (StringUtils.isNumeric(tokenValue))
            return Type.NumericConstant;

        return Type.Identifier;
    }

    static HashMap<Type, KeywordDictionaryEntry> sTokenDictionary = new HashMap<>();
    static HashMap<String, Type> sTokenMappings = new HashMap<>();

    public static String getKeywordValue(Type tokenType, ScriptVariant variant)
    {
        var dictEntry = sTokenDictionary.get(tokenType);

        if (dictEntry == null)
            return ""; //$NON-NLS-1$

        // TODO: Титульный регистр токенов
        switch (variant)
        {
        case ENGLISH:
            return dictEntry.mKeywordEN;
        case RUSSIAN:
            return dictEntry.mKeywordRU;
        default:
            return ""; //$NON-NLS-1$
        }
    }

    static
    {
        sTokenDictionary.put(Type.BeginProcedure, new KeywordDictionaryEntry("ПРОЦЕДУРА", "PROCEDURE")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.BeginFunction, new KeywordDictionaryEntry("ФУНКЦИЯ", "FUNCTION")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.EndProcedure, new KeywordDictionaryEntry("КОНЕЦПРОЦЕДУРЫ", "ENDPROCEDURE")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.EndFunction, new KeywordDictionaryEntry("КОНЕЦФУНКЦИИ", "ENDFUNCTION")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.EqualsSign, new KeywordDictionaryEntry("=", "=")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OpeningBracket, new KeywordDictionaryEntry("(", "(")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.ClosingBracket, new KeywordDictionaryEntry(")", ")")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.Export, new KeywordDictionaryEntry("ЭКСПОРТ", "EXPORT")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.Comma, new KeywordDictionaryEntry(",", ",")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.ExpressionEnd, new KeywordDictionaryEntry(";", ";")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PlusSign, new KeywordDictionaryEntry("+", "+")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.MinusSign, new KeywordDictionaryEntry("-", "-")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.MultiplicationSign, new KeywordDictionaryEntry("*", "*")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.DivisionSign, new KeywordDictionaryEntry("/", "/")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.Dot, new KeywordDictionaryEntry(".", ".")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorNew, new KeywordDictionaryEntry("НОВЫЙ", "NEW")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorIf, new KeywordDictionaryEntry("ЕСЛИ", "IF")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorThen, new KeywordDictionaryEntry("ТОГДА", "THEN")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorElse, new KeywordDictionaryEntry("ИНАЧЕ", "ELSE")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorElseIf, new KeywordDictionaryEntry("ИНАЧЕЕСЛИ", "ELSEIF")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorEndIf, new KeywordDictionaryEntry("КОНЕЦЕСЛИ", "ENDIF")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.LessSign, new KeywordDictionaryEntry("<", "<")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.GreateSign, new KeywordDictionaryEntry(">", ">")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorFor, new KeywordDictionaryEntry("ДЛЯ", "FOR")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorWhile, new KeywordDictionaryEntry("ПОКА", "WHILE")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorEndLoop, new KeywordDictionaryEntry("КОНЕЦЦИКЛА", "ENDLOOP")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorTry, new KeywordDictionaryEntry("ПОПЫТКА", "TRY")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorEndTry, new KeywordDictionaryEntry("КОНЕЦПОПЫТКИ", "ENDTRY")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorIf, new KeywordDictionaryEntry("#ЕСЛИ", "#IF")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorThen, new KeywordDictionaryEntry("#ТОГДА", "#THEN")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorElseIf, new KeywordDictionaryEntry("#ИНАЧЕЕСЛИ", "#ELSEIF")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorElse, new KeywordDictionaryEntry("#ИНАЧЕ", "#ELSE")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorEndIf, new KeywordDictionaryEntry("#КОНЕЦЕСЛИ", "#ENDIF")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorInsert, new KeywordDictionaryEntry("#ВСТАВКА", "#INSERT")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorEndInsert, new KeywordDictionaryEntry("#КОНЕЦВСТАВКИ", "#ENDINSERT")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorRemove, new KeywordDictionaryEntry("#УДАЛЕНИЕ", "#DELETE")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorEndRemove, new KeywordDictionaryEntry("#КОНЕЦУДАЛЕНИЯ", "#ENDDELETE")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorRegion, new KeywordDictionaryEntry("#ОБЛАСТЬ", "#REGION")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorEndRegion, new KeywordDictionaryEntry("#КОНЕЦОБЛАСТИ", "#ENDREGION")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordAnd, new KeywordDictionaryEntry("И", "AND")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordOr, new KeywordDictionaryEntry("ИЛИ", "OR")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordNot, new KeywordDictionaryEntry("НЕ", "NOT")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordVar, new KeywordDictionaryEntry("ПЕРЕМ", "VAR")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordLoop, new KeywordDictionaryEntry("ЦИКЛ", "LOOP")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordEach, new KeywordDictionaryEntry("КАЖДОГО", "EACH")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordVal, new KeywordDictionaryEntry("ЗНАЧ", "VAL")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordExcept, new KeywordDictionaryEntry("ИСКЛЮЧЕНИЕ", "EXCEPT")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.ModuloSign, new KeywordDictionaryEntry("%", "%")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.Tilde, new KeywordDictionaryEntry("~", "~")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordGoto, new KeywordDictionaryEntry("ПЕРЕЙТИ", "GOTO")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.Colon, new KeywordDictionaryEntry(":", ":")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordAsynch, new KeywordDictionaryEntry("АСИНХ", "ASYNCH")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordWait, new KeywordDictionaryEntry("ЖДАТЬ", "WAIT")); //$NON-NLS-1$ //$NON-NLS-2$

        for (var entry: sTokenDictionary.entrySet())
        {
            var dictionaryEntry = entry.getValue();
            sTokenMappings.put(dictionaryEntry.mKeywordRU, entry.getKey());
            sTokenMappings.put(dictionaryEntry.mKeywordEN, entry.getKey());
        }

        // Особые случаи для булевых констант
        sTokenMappings.put("TRUE", Type.BooleanConst); //$NON-NLS-1$
        sTokenMappings.put("FALSE", Type.BooleanConst); //$NON-NLS-1$

        sTokenMappings.put("ИСТИНА", Type.BooleanConst); //$NON-NLS-1$
        sTokenMappings.put("ЛОЖЬ", Type.BooleanConst); //$NON-NLS-1$

    };
}
