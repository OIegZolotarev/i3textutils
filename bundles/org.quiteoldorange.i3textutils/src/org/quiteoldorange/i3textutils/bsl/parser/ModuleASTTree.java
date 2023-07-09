/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;


/**
 * @author ozolotarev
 *
 */
public class ModuleASTTree
    extends AbsractBSLElementNode
{

    @Override
    public String serialize(ScriptVariant variant)
    {
        StringBuilder builder = new StringBuilder();

        for (AbsractBSLElementNode node : getChildren())
        {
            builder.append(node.serialize(variant) + "\n"); //$NON-NLS-1$
        }

        return builder.toString();
    }

    public ModuleASTTree(Lexer lex)
    {
        super(null);

        while (true)
        {
            try
            {
                var node = ParseNode(lex);

                if (node == null)
                    break;

                addChildren(node);
            }
            catch (BSLParsingException e)
            {
                i3TextUtilsPlugin.logError(e);
                break;
            }
        }

        int a = 1;
    }

}
