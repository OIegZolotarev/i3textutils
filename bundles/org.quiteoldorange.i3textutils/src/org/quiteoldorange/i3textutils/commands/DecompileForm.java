/**
 *
 */
package org.quiteoldorange.i3textutils.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.ui.FormsDecompilerDialog;

import com._1c.g5.v8.dt.form.model.Form;
import com._1c.g5.v8.dt.form.ui.editor.FormEditor;
import com._1c.g5.v8.dt.form.ui.editor.IFormEditor;

/**
 * @author ozolotarev
 *
 */
public class DecompileForm
    extends AbstractHandler
{

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IWorkbenchPart part = HandlerUtil.getActivePart(event);

        if (!(part instanceof IFormEditor))
            return null;

        FormEditor formEditor = (FormEditor)part;
        Form f = formEditor.getForm();

        DecompilationContext cont = new DecompilationContext(f);

        FormsDecompilerDialog dlg = new FormsDecompilerDialog(Display.getCurrent().getActiveShell());

        dlg.setBlockOnOpen(true);
        dlg.open();

        // Реквизиты, пример:
        // f.getAttributes().get(0).getValueType().getTypes()

        // TODO Auto-generated method stub
        return null;
    }

}
