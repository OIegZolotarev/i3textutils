/**
 *
 */
package org.quiteoldorange.i3textutils.commands;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.lexer.Token;
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

        //ReformatFileDialog dlg = new ReformatFileDialog(Display.getCurrent().getActiveShell());
        //dlg.open();

        IXtextDocument doc = Utils.getXTextDocumentFromEvent(event);

        if (doc == null)
            return null;

        Lexer l = new Lexer(doc.get());

        ArrayList<Token> test = new ArrayList<>();

        while (true)
        {
            Token t = l.parseNext();

            if (t == null)
                break;

            //i3TextUtilsPlugin.createWarningStatus(t.getValue());
            test.add(t);

        }

        var elements = ModuleElement.collectFromModule(doc);

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
