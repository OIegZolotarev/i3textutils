/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class Token
{
    enum Type
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
        KeywordVal

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

    public String toString(ScriptVariant language)
    {
        switch (mType)
        {
        case Annotation:
            break;
        case BeginFunction:
            break;
        case BeginProcedure:
            break;
        case BooleanConst:
            break;
        case ClosingBracket:
            break;
        case Comma:
            break;
        case Comment:
            break;
        case DateConstant:
            break;
        case DivisionSign:
            break;
        case Dot:
            break;
        case EndFunction:
            break;
        case EndProcedure:
            break;
        case EqualsSign:
            break;
        case Export:
            break;
        case ExpressionEnd:
            break;
        case GreateSign:
            break;
        case Identifier:
            break;
        case KeywordAnd:
            break;
        case KeywordEach:
            break;
        case KeywordLoop:
            break;
        case KeywordNot:
            break;
        case KeywordOr:
            break;
        case KeywordVal:
            break;
        case KeywordVar:
            break;
        case LessSign:
            break;
        case MinusSign:
            break;
        case MultiplicationSign:
            break;
        case NumericConstant:
            break;
        case OpeningBracket:
            break;
        case OperatorElse:
            break;
        case OperatorElseIf:
            break;
        case OperatorEndIf:
            break;
        case OperatorEndLoop:
            break;
        case OperatorEndTry:
            break;
        case OperatorFor:
            break;
        case OperatorIf:
            break;
        case OperatorNew:
            break;
        case OperatorThen:
            break;
        case OperatorTry:
            break;
        case OperatorWhile:
            break;
        case PlusSign:
            break;
        case PreprocessorElse:
            break;
        case PreprocessorElseIf:
            break;
        case PreprocessorEndIf:
            break;
        case PreprocessorEndInsert:
            break;
        case PreprocessorEndRegion:
            break;
        case PreprocessorEndRemove:
            break;
        case PreprocessorIf:
            break;
        case PreprocessorInsert:
            break;
        case PreprocessorRegion:
            break;
        case PreprocessorRemove:
            break;
        case PreprocessorThen:
            break;
        case StringConstant:
            break;
        default:
            break;
        }

        return mValue;
    }
}
