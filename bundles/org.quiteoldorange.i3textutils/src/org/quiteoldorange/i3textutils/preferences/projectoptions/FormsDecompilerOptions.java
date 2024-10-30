package org.quiteoldorange.i3textutils.preferences.projectoptions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
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

        Text label = new Text(parent, SWT.NONE);
        label.setText("Привет мир");

        return null;
    }

}
