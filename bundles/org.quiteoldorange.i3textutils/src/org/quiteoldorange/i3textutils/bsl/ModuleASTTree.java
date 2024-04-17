/**
 *
 */
package org.quiteoldorange.i3textutils.bsl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;
import org.quiteoldorange.i3textutils.bsl.parser.AnnotationNode;
import org.quiteoldorange.i3textutils.bsl.parser.BSLParsingException;
import org.quiteoldorange.i3textutils.bsl.parser.BSLRegionNode;
import org.quiteoldorange.i3textutils.bsl.parser.CommentNode;
import org.quiteoldorange.i3textutils.bsl.parser.CommentsBlock;
import org.quiteoldorange.i3textutils.bsl.parser.MethodNode;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.MethodCallNode;
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

        mergeCommentNodes(this);
        mergeMethodAnnotationsAndDocs(this);

    }

    /**
     * @param text
     */
    public ModuleASTTree(String text)
    {
        this(new Lexer(text));
    }

    /**
     * @param moduleASTTree
     */
    private void mergeMethodAnnotationsAndDocs(AbsractBSLElementNode node)
    {
        LinkedList<AbsractBSLElementNode> newNodes = new LinkedList<>();

        var children = node.getChildren();
        MethodNode methodNode = null;

        for (var iterator = children.listIterator(children.size()); iterator.hasPrevious();)
        {
            var childNode = iterator.previous();

            if (childNode instanceof MethodNode)
            {
                methodNode = (MethodNode)childNode;
                newNodes.add(methodNode);
            }
            else if (childNode instanceof BSLRegionNode)
            {
                mergeMethodAnnotationsAndDocs(childNode);
                newNodes.add(childNode);
            }
            else if (childNode instanceof AnnotationNode)
            {
                if (methodNode != null)
                    methodNode.addAnnotation((AnnotationNode)childNode);
            }
            else if (childNode instanceof CommentsBlock)
            {
                if (methodNode != null)
                    methodNode.addDocumentationBlock((CommentsBlock)childNode);
            }
            else
            {
                methodNode = null;
                newNodes.add(childNode);
            }

        }

        Collections.reverse(newNodes);
        node.setChildren(newNodes);
    }

    /**
     * @param moduleASTTree
     */
    private void mergeCommentNodes(AbsractBSLElementNode node)
    {
        LinkedList<CommentNode> commentNodes = new LinkedList<>();
        LinkedList<AbsractBSLElementNode> newNodes = new LinkedList<>();

        for (var iterator = node.getChildren().iterator(); iterator.hasNext();)
        {
            var child = iterator.next();

            if (child instanceof BSLRegionNode)
            {
                newNodes.add(child);
                mergeCommentNodes(child);
            }
            else if (child instanceof CommentNode)
            {
                commentNodes.add((CommentNode)child);
            }
            else
            {
                if (commentNodes.size() > 0)
                {
                    var commentBlock = new CommentsBlock(commentNodes);
                    newNodes.add(commentBlock);
                    commentNodes.clear();
                }

                newNodes.add(child);
            }

        }

        if (commentNodes.size() > 0)
        {
            int index = node.getChildren().indexOf(commentNodes.get(0));

            var commentBlock = new CommentsBlock(commentNodes);

            node.getChildren().add(index, commentBlock);
            commentBlock.setParent(node);
            commentNodes.clear();
        }

        node.setChildren(newNodes);
    }

    /**
     * @param name
     * @return
     */
    public BSLRegionNode findRegion(String name)
    {
        for (var item : getChildren())
        {
            if (!(item instanceof BSLRegionNode))
                continue;

            BSLRegionNode node = (BSLRegionNode)item;
            if (name.equals(node.getName()))
                return node;
        }

        return null;
    }

    private MethodNode findMethodDefinitionRecursive(MethodCallNode methodCallNode, AbsractBSLElementNode node)
    {
        for (var item : node.getChildren())
        {
            if (item instanceof MethodNode)
            {
                MethodNode methodNode = (MethodNode)item;
                if (methodCallNode.methodName().equals(methodNode.getMethodName()))
                {
                    return methodNode;
                }
            }

            MethodNode nextItem = findMethodDefinitionRecursive(methodCallNode, item);

            if (nextItem != null)
                return nextItem;
        }

        return null;
    }

    /**
     * @param methodCallNode
     * @return
     */
    public MethodNode findMethodDefinition(MethodCallNode methodCallNode)
    {
        return findMethodDefinitionRecursive(methodCallNode, this);
    }

    /**
     * @param offset
     * @return
     */
    public MethodNode findMethodDefinition(int offset)
    {
        return findMethodDefinition(offset, getChildren());
    }

    private MethodNode findMethodDefinition(int offset, List<AbsractBSLElementNode> items)
    {
        for (var child : items)
        {
            if (!(child.getStartingOffset() <= offset && child.getEndOffset() > offset))
                continue;

            if (child instanceof MethodNode)
            {
                return (MethodNode)child;
            }
            else if (child.getChildren().size() > 0)
                return findMethodDefinition(offset, child.getChildren());

        }
        return null;
    }

    /**
     * @return
     */
    public List<BSLRegionNode> dumpTopRegions()
    {
        List<BSLRegionNode> result = new LinkedList<>();

        for(AbsractBSLElementNode child: getChildren())
        {
            if (child instanceof BSLRegionNode)
                result.add((BSLRegionNode)child);
            else
                dumpTopRegionsRecursive(result, child, 1);
        }
        return result;

    }

    public void swapNodes(AbsractBSLElementNode a, AbsractBSLElementNode b)
    {
        AbsractBSLElementNode parentOfA = a.getParent();
        AbsractBSLElementNode parentOfB = b.getParent();

        if (parentOfA == null || parentOfB == null)
        {
            // throw exception
            return;
        }

        int aIndex = parentOfA.indexOfChild(a);
        int bIndex = parentOfB.indexOfChild(b);

        parentOfA.removeChildren(a);
        parentOfB.removeChildren(b);

        parentOfA.insertChildren(aIndex, b);
        parentOfA.insertChildren(bIndex, a);

    }

    private void dumpTopRegionsRecursive(List<BSLRegionNode> dest, AbsractBSLElementNode node, int level)
    {
        for (AbsractBSLElementNode it : node.getChildren())
        {
            if (it instanceof BSLRegionNode)
                dest.add((BSLRegionNode)it);

            else if (level > 0)
                dumpTopRegionsRecursive(dest, it, level - 1);
        }
    }

}
