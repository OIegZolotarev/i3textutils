/**
 *
 */
package org.quiteoldorange.i3textutils.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.ui.wizard.DecompilationWizard;

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

        DecompilationContext cont = new DecompilationContext(formEditor);
        DecompilationWizard wizard = new DecompilationWizard(cont);

        WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
        wizardDialog.open();


        // TODO Auto-generated method stub
        return null;
    }

}
