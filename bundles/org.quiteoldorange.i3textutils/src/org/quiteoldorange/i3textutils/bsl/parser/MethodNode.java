/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.LinkedList;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.UnexpectedEndOfStream;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class MethodNode
    extends AbsractBSLElementNode
{
    @Override
    public String toString()
    {
        return "MethodNode [mMethodName=" + getMethodName() + ", mType=" + mType + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    String mLazySource;
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

    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        var iterator = getChildren().iterator();

        StringBuilder builder = new StringBuilder();

        while (true)
        {
            if (!iterator.hasNext())
                break;

            var node = iterator.next();

            if (!(node instanceof CommentsBlock || node instanceof AnnotationNode))
                break;

            builder.append(node.serialize(scriptVariant));
        }

        if (!getLazySource().isEmpty())
        {
            builder.append(getLazySource());
            return builder.toString();
        }

        return ""; //$NON-NLS-1$
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

        argParserLoop: while (true)
        {
            token = readTokenTracked(stream);

            if (token == null)
                throw new BSLParsingException.UnexpectedEndOfStream();

            // Упираемся тут если прочитали аргумент со значением по умолчанию
            if (token.getType() == Type.ClosingBracket)
                break;

            boolean byValue = false;
            // Если аргумент по значению
            if (token.getType() == Type.KeywordVal)
            {
                byValue = true;
                token = readTokenTracked(stream);
            }

            // Ожидаем имя аргумента и падаем если его нет
            if (token.getType() != Type.Identifier)
                throw new BSLParsingException.UnexpectedToken(stream, token, Type.Identifier);

            String argName = token.getValue();
            String defaultValue = ""; //$NON-NLS-1$

            // Смотрим следующий токен
            // а) запятая
            // б) знак равно
            // в) закрывающая скобка.
            token = readTokenTracked(stream);

            switch (token.getType())
            {
            case Comma:
                getArguments().add(new ArgumentDefinition(argName, null, byValue));
                continue;
            case EqualsSign:

                token = readTokenTracked(stream); // Значение аргумента


                switch (token.getType())
                {
                case Identifier:
                case NumericConstant:
                case BooleanConst:
                case StringConstant:
                case DateConstant:
                    defaultValue = token.getValue();
                    break;
                default:
                    throw new BSLParsingException.UnexpectedToken(stream, token, Type.Identifier);
                }


                getArguments().add(new ArgumentDefinition(argName, defaultValue, byValue));

                token = stream.peekNext();

                if (token.getType() == Type.Comma)
                    readTokenTracked(stream);

                continue;
            case ClosingBracket:
                getArguments().add(new ArgumentDefinition(argName, defaultValue, byValue));
                break argParserLoop;
            default:
                throw new BSLParsingException.UnexpectedToken(stream, token);
            }
        }

        token = stream.peekNext();

        if (token.getType() == Type.Export)
        {
            mExported = true;
            readTokenTracked(stream);
        }


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

        if (stream.isLazyMode())
        {
            while (true)
            {
                token = readTokenTracked(stream);

                if (token == null)
                    throw new UnexpectedEndOfStream();

                if (token.getType() == finisher)
                    break;
            }
        }
        else
            ParseUntilEndingToken(stream, finisher);

        mLazySource = stream.getTokensSource(mTokens);
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
        @SuppressWarnings("unused")
        private boolean mByValue;

        /**
         * @param stream
         */
        public ArgumentDefinition(String name, String defaultValue, boolean byValue)
        {
            mName = name;
            mByValue = byValue;
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

    /**
     * @param childNode
     */
    public void addAnnotation(AnnotationNode childNode)
    {
        childNode.setParent(this);
        getChildren().add(0, childNode);
    }

    /**
     * @param childNodeObject
     */
    public void addDocumentationBlock(CommentsBlock childNode)
    {
        childNode.setParent(this);
        getChildren().add(0, childNode);
    }

    /**
     * @return the methodName
     */
    public String getMethodName()
    {
        return mMethodName;
    }

    /**
     * @return the arguments
     */
    public LinkedList<ArgumentDefinition> getArguments()
    {
        return mArguments;
    }

    public String getLazySource()
    {
        return mLazySource;
    }

}
