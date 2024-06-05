package org.quiteoldorange.i3textutils.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.MethodCallNode;
import org.quiteoldorange.i3textutils.refactoring.Utils;

public class MoveArgumentsOnSeparateLines
    extends AbstractHandler

{

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IXtextDocument doc = Utils.getXTextDocumentFromEvent(event);

        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        XtextEditor target = part.getAdapter(XtextEditor.class);

        if (doc == null)
            return null;

        var sel = (ITextSelection)target.getSelectionProvider().getSelection();

        String text = sel.getText();
        int offset = sel.getOffset();
        int length = sel.getLength();
        int startOffset = 0;
        try
        {
            startOffset = doc.getLineOffset(sel.getStartLine());
        }
        catch (BadLocationException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        ModuleASTTree tree = new ModuleASTTree(text);

        AbsractBSLElementNode node = pullMeaningfullNode(tree);

        if (node instanceof MethodCallNode)
        {
            try
            {
                MethodCallNode methodCallNode = (MethodCallNode)node;
                String s = methodCallNode.serialize(Utils.getDocScriptVariant(doc), true);
                s = verticalAlignMethodCall(s, (offset - startOffset) + 1);

                doc.replace(offset, length, s);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return null;
    }

    private AbsractBSLElementNode pullMeaningfullNode(AbsractBSLElementNode tree)
    {
        if (tree.getChildren().size() == 1)
            return pullMeaningfullNode(tree.getChildren().get(0));

        else
            return tree;
    }

    /**
     * @param text
     * @return
     */
    public static String verticalAlignMethodCall(String text, int bias)
    {
        //////////////////////////////
        var lines = text.split("\n"); //$NON-NLS-1$

        if (lines.length < 1)
            return text;

        int maxOffset = 0;

        maxOffset = lines[0].indexOf('(');


        StringBuilder newLines = new StringBuilder();

        int lineIndex = 0;

        for (String line : lines)
        {
            int currentOffset = 0;

            if (lineIndex == 0)
                currentOffset = maxOffset;

            lineIndex++;

            var usedLine = new String(line);

            if (currentOffset == -1)
            {
                currentOffset = 0;
                usedLine = usedLine.trim();
            }

            if (currentOffset == maxOffset)
            {
                newLines.append(usedLine + "\n"); //$NON-NLS-1$
                continue;
            }

            int spacesToAdd = (maxOffset - currentOffset) + bias;

            line = line.substring(0, currentOffset) + " ".repeat(spacesToAdd) + line.substring(currentOffset); //$NON-NLS-1$
            newLines.append(line + "\n"); //$NON-NLS-1$
        }

        //////////////////////////////

        String result = newLines.toString();
        result = result.substring(0, result.length() - 1);
        return result;
    }

}
