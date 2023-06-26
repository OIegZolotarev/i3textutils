/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.LinkedList;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;


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
                // TODO Auto-generated catch block
                e.printStackTrace();
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
