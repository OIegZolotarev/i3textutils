/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class InjectionNode
    extends AbsractBSLElementNode
{

    private String mInjectedCode;

    @Override
    public String serialize(ScriptVariant scriptVariant) throws Exception
    {
        return mInjectedCode;
    }

    /**
     * @param stream
     */
    private InjectionNode(Lexer stream)
    {
        super(stream);
    }

    /**
     * @param stream
     */
    public InjectionNode(String code)
    {
        super(null);
        mInjectedCode = code;
    }

}
