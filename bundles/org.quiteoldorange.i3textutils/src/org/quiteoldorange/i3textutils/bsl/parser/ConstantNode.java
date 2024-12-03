/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token.Type;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class ConstantNode
    extends AbsractBSLElementNode
{

    /**
     * @param stream
     */
    public ConstantNode(Lexer stream)
    {
        super(stream);
    }

    public String getAsString()
    {
        assert (mTokens.size() > 0);

        var t = mTokens.get(0);

        if (t.getType() == Type.StringConstant)
        {

            StringBuilder b = new StringBuilder();

            b.append("\""); //$NON-NLS-1$

            String val = t.getValue();
            int l = val.length();

            for (int i = 0; i < l; i++)
            {
                char c = val.charAt(i);
                b.append(c);

                if (c == '\n')
                    b.append("|"); //$NON-NLS-1$
            }

            b.append("\""); //$NON-NLS-1$

//                return "\"" + t.getValue() + "\""; //$NON-NLS-1$//$NON-NLS-2$
        }
        else
            return t.getValue();
    }

    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        return getAsString();
    }

    @Override
    public String toString()
    {
        return getAsString();
    }

}
