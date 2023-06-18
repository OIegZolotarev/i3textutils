/**
 *
 */
package org.quiteoldorange.i3textutils.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.refactoring.ModuleElement;
import org.quiteoldorange.i3textutils.refactoring.Utils;

/**
 * @author ozolotarev
 *
 */
public class ReformatModule
    extends AbstractHandler
{

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {

        IXtextDocument doc = Utils.getXTextDocumentFromEvent(event);

        if (doc == null)
            return null;

        var elements = ModuleElement.CollectFromModule(doc);

        elements.sort((ModuleElement a, ModuleElement b) -> {
            return a.getName().compareTo(b.getName());
        });

        StringBuilder newDocString = new StringBuilder();

        for (ModuleElement el : elements)
        {
            newDocString.append(el.getSourceText() + "\n\n"); //$NON-NLS-1$
        }

        try
        {
            doc.replace(0, doc.getLength(), newDocString.toString());
        }
        catch (BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }

}
