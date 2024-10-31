package org.quiteoldorange.i3textutils.preferences.projectoptions;

import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class FormsDecompilerOptions
    extends PropertyPage
    implements IWorkbenchPropertyPage
{

    public FormsDecompilerOptions()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Control createContents(Composite parent)
    {
        String labelAndValues[][] = new String[][] {
            { "Ничего не делать", "doNothing" }, //$NON-NLS-2$
            { "В \"ПриСозданииНаСервере\"", "putAtCreateOnServer" }, //$NON-NLS-2$
            { "В общий модуль", "putAtCommonModule" }, //$NON-NLS-2$
        };

        RadioGroupFieldEditor codeGeneratorMode =
            new RadioGroupFieldEditor("codeGeneratorMode", "Режим генерации кода", 1, labelAndValues, parent);

        return null;
    }

}
