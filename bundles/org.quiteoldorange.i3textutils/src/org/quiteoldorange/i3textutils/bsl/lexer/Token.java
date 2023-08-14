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
        EmptyLine,
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
        GreaterSign,
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
        AnnotationAtClient,
        AnnotationAtServer,
        AnnotationAtClientAtServerNoContext,
        AnnotationAtServerNoContext,
        AnnotationBefore,
        AnnotationAfter,
        AnnotationAround,
        KeywordReturn

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

        if (tokenValue.charAt(0) == '&')
            return Type.Annotation;

        if (tokenValue.charAt(0) == '\n')
            return Type.EmptyLine;

        if (tokenValue.length() > 1)
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
        sTokenDictionary.put(Type.BeginProcedure, new KeywordDictionaryEntry("Процедура", "Procedure")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.BeginFunction, new KeywordDictionaryEntry("Функция", "Function")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.EndProcedure, new KeywordDictionaryEntry("КонецПроцедуры", "EndProcedure")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.EndFunction, new KeywordDictionaryEntry("КонецФункции", "EndFunction")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.EqualsSign, new KeywordDictionaryEntry("=", "=")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OpeningBracket, new KeywordDictionaryEntry("(", "(")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.ClosingBracket, new KeywordDictionaryEntry(")", ")")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.Export, new KeywordDictionaryEntry("Экспорт", "Export")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.Comma, new KeywordDictionaryEntry(",", ",")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.ExpressionEnd, new KeywordDictionaryEntry(";", ";")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PlusSign, new KeywordDictionaryEntry("+", "+")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.MinusSign, new KeywordDictionaryEntry("-", "-")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.MultiplicationSign, new KeywordDictionaryEntry("*", "*")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.DivisionSign, new KeywordDictionaryEntry("/", "/")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.Dot, new KeywordDictionaryEntry(".", ".")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorNew, new KeywordDictionaryEntry("Новый", "New")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorIf, new KeywordDictionaryEntry("Если", "If")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorThen, new KeywordDictionaryEntry("Тогда", "Then")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorElse, new KeywordDictionaryEntry("Иначе", "Else")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorElseIf, new KeywordDictionaryEntry("ИначеЕсли", "ElseIf")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorEndIf, new KeywordDictionaryEntry("КонецЕсли", "EndIf")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.LessSign, new KeywordDictionaryEntry("<", "<")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.GreaterSign, new KeywordDictionaryEntry(">", ">")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorFor, new KeywordDictionaryEntry("Для", "For")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorWhile, new KeywordDictionaryEntry("Пока", "While")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorEndLoop, new KeywordDictionaryEntry("КонецЦикла", "EndLoop")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorTry, new KeywordDictionaryEntry("Попытка", "Try")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.OperatorEndTry, new KeywordDictionaryEntry("КонецПопытка", "EndTry")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorIf, new KeywordDictionaryEntry("#Если", "#If")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorThen, new KeywordDictionaryEntry("#Тогда", "#Then")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorElseIf, new KeywordDictionaryEntry("#ИначеЕсли", "#ElseIf")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorElse, new KeywordDictionaryEntry("#Иначе", "#Else")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorEndIf, new KeywordDictionaryEntry("#КонецЕсли", "#EndIf")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorInsert, new KeywordDictionaryEntry("#Вставка", "#Insert")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorEndInsert, new KeywordDictionaryEntry("#КонецВставки", "#EndInsert")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorRemove, new KeywordDictionaryEntry("#Удаление", "#Delete")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorEndRemove, new KeywordDictionaryEntry("#КонецУдаления", "#EndDelete")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorRegion, new KeywordDictionaryEntry("#Область", "#Region")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.PreprocessorEndRegion, new KeywordDictionaryEntry("#КонецОбласти", "#EndRegion")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordAnd, new KeywordDictionaryEntry("И", "And")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordOr, new KeywordDictionaryEntry("Или", "Or")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordNot, new KeywordDictionaryEntry("Не", "Not")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordVar, new KeywordDictionaryEntry("Перем", "Var")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordLoop, new KeywordDictionaryEntry("Цикл", "Loop")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordEach, new KeywordDictionaryEntry("Каждого", "Each")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordVal, new KeywordDictionaryEntry("Знач", "Val")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordExcept, new KeywordDictionaryEntry("Исключение", "Except")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.ModuloSign, new KeywordDictionaryEntry("%", "%")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.Tilde, new KeywordDictionaryEntry("~", "~")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordGoto, new KeywordDictionaryEntry("Перейти", "Goto")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.Colon, new KeywordDictionaryEntry(":", ":")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordAsynch, new KeywordDictionaryEntry("Асинх", "Asynch")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordWait, new KeywordDictionaryEntry("Ждать", "Wait")); //$NON-NLS-1$ //$NON-NLS-2$

        sTokenDictionary.put(Type.AnnotationAtClient, new KeywordDictionaryEntry("&НаКлиенте", "&AtClient")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.AnnotationAtServer, new KeywordDictionaryEntry("&НаСервере", "&AtServer")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.AnnotationAtClientAtServerNoContext,
            new KeywordDictionaryEntry("&НаКлиентеНаСервереБезКонтекста", "&AtClientAtServerNoContext")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.AnnotationAtServerNoContext,
            new KeywordDictionaryEntry("&НаСервереБезКонтекста", "&AtServerNoContext")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.AnnotationBefore, new KeywordDictionaryEntry("&Перед", "&Before")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.AnnotationAfter, new KeywordDictionaryEntry("&После", "&After")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.AnnotationAround, new KeywordDictionaryEntry("&Вместо", "&Around")); //$NON-NLS-1$ //$NON-NLS-2$
        sTokenDictionary.put(Type.KeywordReturn, new KeywordDictionaryEntry("Возврат", "Return")); //$NON-NLS-1$ //$NON-NLS-2$

        for (var entry: sTokenDictionary.entrySet())
        {
            var dictionaryEntry = entry.getValue();
            sTokenMappings.put(dictionaryEntry.mKeywordRU.toUpperCase(), entry.getKey());
            sTokenMappings.put(dictionaryEntry.mKeywordEN.toUpperCase(), entry.getKey());
        }

        // Особые случаи для булевых констант
        sTokenMappings.put("TRUE", Type.BooleanConst); //$NON-NLS-1$
        sTokenMappings.put("FALSE", Type.BooleanConst); //$NON-NLS-1$

        sTokenMappings.put("ИСТИНА", Type.BooleanConst); //$NON-NLS-1$
        sTokenMappings.put("ЛОЖЬ", Type.BooleanConst); //$NON-NLS-1$

    };

    @Override
    public String toString()
    {
        return String.format("%s (%s)", mValue, mType.toString()); //$NON-NLS-1$
    }
}
