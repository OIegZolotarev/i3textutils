package org.quiteoldorange.i3textutils.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.Module;


public class AddMissingPragmas
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

        Module moduleModel = Utils.getModuleFromXTextDocument(doc);

        if (moduleModel == null)
        {
            return null;
        }


        var methods = moduleModel.allMethods();
        for (int i = methods.size() - 1; i >= 0; i--)
        {
            var method = methods.get(i);

            if (method.getPragmas().isEmpty())
            {
                var node = NodeModelUtils.findActualNodeFor(method);

                try
                {
                    doc.replace(node.getOffset(), 0, "&НаСервере\n"); //$NON-NLS-1$
                }
                catch (BadLocationException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;

    }

}
