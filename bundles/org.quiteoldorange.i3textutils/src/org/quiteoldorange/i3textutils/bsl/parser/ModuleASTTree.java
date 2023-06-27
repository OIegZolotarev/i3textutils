/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.LinkedList;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;


/**
 * @author ozolotarev
 *
 */
public class ModuleASTTree
    extends AbsractBSLElementNode
{

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

                mChildren.add(node);
            }
            catch (BSLParsingException e)
            {
                i3TextUtilsPlugin.logError(e);
            }
        }
    }

    /**
     * @return the rootNodes
     */
    public LinkedList<AbsractBSLElementNode> getRootNodes()
    {
        return mChildren;
    }
}
