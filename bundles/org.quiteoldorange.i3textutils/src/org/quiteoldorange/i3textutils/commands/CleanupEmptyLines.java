package org.quiteoldorange.i3textutils.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.modulereformatter.ModuleReformatterContext;
import org.quiteoldorange.i3textutils.refactoring.Utils;

public class CleanupEmptyLines
    extends AbstractHandler
{

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IXtextDocument doc = Utils.getXTextDocumentFromEvent(event);

        if (doc == null)
        {
            return null;
        }

        String newSource = ModuleReformatterContext.cleanupConsecutiveBlankLines(doc.get());
        try
        {
            doc.replace(0, doc.getLength(), newSource);
        }
        catch (BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
