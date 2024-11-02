package org.quiteoldorange.i3textutils.preferences.projectoptions.impl.formsdecompiler;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;
import org.quiteoldorange.i3textutils.preferences.projectoptions.ProjectOptionsManager;
import org.quiteoldorange.i3textutils.preferences.projectoptions.ProjectOptionsSet;
import org.quiteoldorange.i3textutils.preferences.projectoptions.ProjectPropertiesPage;

public class FormsDecompilerOptionsPage
    extends ProjectPropertiesPage
    implements IWorkbenchPropertyPage
{



    public FormsDecompilerOptionsPage()
    {
        super();


    }

    @Override
    protected Control createContents(Composite parent)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(composite);
        GridLayoutFactory.swtDefaults().numColumns(1).applyTo(composite);

        ProjectOptionsManager manager = i3TextUtilsPlugin.getProjectOptionsManager();
        ProjectOptionsSet optionsSet = manager.getProjectOptionsSet(getProject(), FormsDecompilerOptionSet.ID());

        setOptionsSet(optionsSet);

        createOptionsWidgets(composite);

        return composite;
    }


}
