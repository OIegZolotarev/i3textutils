/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.LinkedList;

import org.quiteoldorange.i3textutils.bsl.exceptions.BSLParsingException;
import org.quiteoldorange.i3textutils.bsl.exceptions.BSLParsingException.UnexpectedEndOfStream;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;

/**
 * @author ozolotarev
 *
 */
public class MethodNode
    extends AbsractBSLElementNode
{
    LinkedList<ArgumentDefinition> mArguments = new LinkedList<>();
    boolean mExported = false;
    boolean mAsynch = false;
    String mMethodName = null;

    public enum MethodTypes
    {
        Function,
        Procedure
    }

    MethodTypes mType;

    public MethodNode(Lexer stream, MethodTypes type, boolean asynch) throws BSLParsingException
    {
        this(stream, type);
        mAsynch = true;
    }

    /**
     * @param stream
     * @throws UnexpectedTokenException
     * @throws UnexpectedEndOfStream
     */
    public MethodNode(Lexer stream, MethodTypes type) throws BSLParsingException
    {
        super(stream);
        mType = type;

        // Процедура -> ИмяПроцедуры -> Скобка -> <Параметры> -> Скобка -> [Экспорт]

        var token = readTokenTracked(stream);

        if (token.getType() != Type.Identifier)
            throw new BSLParsingException.UnexpectedToken(stream, token);

        mMethodName = token.getValue();

        checkTokenTracked(stream, Type.OpeningBracket);

        while (true)
        {
            token = readTokenTracked(stream);

            if (token == null)
                throw new BSLParsingException.UnexpectedEndOfStream();

            if (token.getType() == Type.ClosingBracket)
                break;

            if (token.getType() != Type.Identifier)
                throw new BSLParsingException.UnexpectedToken(stream, token, Type.Identifier);

            String argName = token.getValue();

            token = readTokenTracked(stream);

            if (stream.peekNext().getType() != Type.EqualsSign)
            {
                mArguments.add(new ArgumentDefinition(argName, null));
                continue;
            }

            token = readTokenTracked(stream); // Знак равно
            token = readTokenTracked(stream); // Значение аргумента

            if (token.getType() != Type.Identifier)
                throw new BSLParsingException.UnexpectedToken(stream, token, Type.Identifier);

            String defaultValue = token.getValue();

            mArguments.add(new ArgumentDefinition(argName, defaultValue));
        }

        token = readTokenTracked(stream);

        if (token.getType() == Type.Export)
            mExported = true;
        else
            stream.rollback();


        Token.Type finisher = null;

        switch (type)
        {
        case Function:
            finisher = Type.EndFunction;
            break;
        case Procedure:
            finisher = Type.EndProcedure;
            break;
        }

        ParseUntilEndingToken(stream, finisher);

    }

    /**
     * @return the asynch
     */
    public boolean isAsynch()
    {
        return mAsynch;
    }

    /**
     * @return the exported
     */
    public boolean isExported()
    {
        return mExported;
    }

    public class ArgumentDefinition
    {
        private String mName = null;
        private String mDefaultValue = null;

        /**
         * @param stream
         */
        public ArgumentDefinition(String name, String defaultValue)
        {
            mName = name;
            mDefaultValue = defaultValue;
        }

        public boolean hasDefaultValue()
        {
            return getDefaultValue() != null;
        }

        /**
         * @return the defaultValue
         */
        public String getDefaultValue()
        {
            return mDefaultValue;
        }

        /**
         * @return the name
         */
        public String getName()
        {
            return mName;
        }
    }

}
