/**
 *
 */
package org.quiteoldorange.i3textutils.preferences.projectoptions;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * @author ozolotarev
 *
 */
public abstract class ProjectPropertiesPage
    extends PropertyPage
{

    private ProjectOptionsSet mOptionsSet;

    /**
     *
     */
    public ProjectPropertiesPage()
    {
        super();
    }

    protected void setOptionsSet(ProjectOptionsSet set)
    {
        mOptionsSet = set;
    }

    protected IProject getProject()
    {
        return (IProject)getElement();
    }

    @Override
    protected void performDefaults()
    {
        for (IProjectOption option : mOptionsSet.getOptions())
        {
            option.setDefault();
            option.updateWidgetState();
        }
    }

    @Override
    public boolean performOk()
    {

        for (IProjectOption option : mOptionsSet.getOptions())
        {
            option.save(getProject(), mOptionsSet.getId());
        }

        return super.performOk();
    }

    /**
     * @param composite
     */
    protected void createOptionsWidgets(Composite composite)
    {
        mOptionsSet.createWidgets(composite);
    }
}
