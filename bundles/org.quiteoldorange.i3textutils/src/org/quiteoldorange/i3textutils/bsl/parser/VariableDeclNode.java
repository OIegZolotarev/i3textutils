/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.LinkedList;
import java.util.List;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException.UnexpectedToken;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class VariableDeclNode
    extends AbsractBSLElementNode
{

    class VariableDefinition
    {

        /**
         * @return the name
         */
        public String getName()
        {
            return mName;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name)
        {
            mName = name;
        }

        /**
         * @return the exported
         */
        public boolean isExported()
        {
            return mExported;
        }

        private String mName = null;
        private boolean mExported = false;

        /**
         *
         */
        public VariableDefinition()
        {

        }

        public String serialize(ScriptVariant scriptVariant)
        {
            String result = mName;

            if (mExported)
                result = result + " " + Token.getKeywordValue(Type.Export, scriptVariant); //$NON-NLS-1$

            return result;
        }

        /**
         * @param b
         */
        public void setExported(boolean b)
        {
            mExported = b;
        }

    }

    List<VariableDefinition> mDefinitions = new LinkedList<>();

    /**
     * @param stream
     * @param parent TODO
     * @throws UnexpectedToken
     */
    public VariableDeclNode(Lexer stream, AbsractBSLElementNode parent) throws UnexpectedToken
    {
        super(stream);

        loop: while (true)
        {
            var token = readTokenTracked(stream);

            if (token.getType() != Type.Identifier)
                throw new BSLParsingException.UnexpectedToken(stream, token, Type.Identifier);

            VariableDefinition def = new VariableDefinition();
            def.setName(token.getValue());
            token = readTokenTracked(stream);


            switch(token.getType())
            {
            case Comma:
                mDefinitions.add(def);
                continue;
            case Export:
                def.setExported(true);
                mDefinitions.add(def);
                continue;
            case ExpressionEnd:

                mDefinitions.add(def);
                break loop;
            default:
                throw new BSLParsingException.UnexpectedToken(stream, token);
            }

        }

    }

    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        String result = Token.getKeywordValue(Type.KeywordVar, scriptVariant);

        for (var item : mDefinitions)
        {
            result = result + " " + item.serialize(scriptVariant) + ","; //$NON-NLS-1$//$NON-NLS-2$
        }

        result = result.substring(0, result.length() - 1) + ";\n"; //$NON-NLS-1$
        return result;
    }

}
