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
import org.quiteoldorange.i3textutils.refactoring.Utils;

public class VerticalAlign
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

        //////////////////////////////
        var lines = text.split("\n");

        int maxOffset = 0;

        for (String line : lines)
        {
            maxOffset = Math.max(maxOffset, line.indexOf('='));
        }

        StringBuilder newLines = new StringBuilder();

        for (String line : lines)
        {
            int currentOffset = line.indexOf('=');

            if (currentOffset == -1)
            {
                newLines.append(line + "\n");
                continue;
            }

            int spacesToAdd = maxOffset - currentOffset;

            line = line.substring(0, currentOffset) + " ".repeat(spacesToAdd) + line.substring(currentOffset);
            newLines.append(line + "\n");
        }


        //////////////////////////////

        try
        {
            doc.replace(offset, length, newLines.toString());
        }
        catch (BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }



}
