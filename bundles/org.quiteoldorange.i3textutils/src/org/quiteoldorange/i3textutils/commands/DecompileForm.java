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

import com._1c.g5.v8.dt.core.platform.IV8Project;
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

        IV8Project v8Project = formEditor.getV8projectManager().getProject(f.bmGetEngine().getId());

        DecompilationContext cont = new DecompilationContext(f, v8Project);
        DecompilationWizard wizard = new DecompilationWizard(cont);

        WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
        wizardDialog.open();


        // TODO Auto-generated method stub
        return null;
    }

}
