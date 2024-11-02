/**
 *
 */
package org.quiteoldorange.i3textutils.preferences.projectoptions;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;

/**
 * @author ozolotarev
 *
 */
public class ProjectOptionsSet
{
    private List<IProjectOption> mOptions = new LinkedList<>();
    private String mId;
    private IProject mProject;

    public ProjectOptionsSet(String id, IProject project)
    {
        mId = id;
        mProject = project;
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return mId;
    }

    /**
     * @return the options
     */
    public List<IProjectOption> getOptions()
    {
        return mOptions;
    }

    protected void addOption(IProjectOption option)
    {
        mOptions.add(option);
    }

    public void load()
    {
        for (IProjectOption option : mOptions)
        {
            option.load(mProject, mId);
        }
    }

    public void save()
    {
        for (IProjectOption option : mOptions)
        {
            option.save(mProject, mId);
        }
    }

    public void setDefault()
    {
        for (IProjectOption option : mOptions)
        {
            option.setDefault();
        }
    }

    /**
     * @param composite
     */
    public void createWidgets(Composite composite)
    {
        // TODO: было бы неплохо заиметь категории опций

        for (IProjectOption option : mOptions)
        {
            option.createWidget(composite);
        }
    }

    /**
     * @param useRegions
     * @return
     */
    public IProjectOption getOption(String key)
    {

        for (IProjectOption option : mOptions)
        {
            if (option.getKey().equals(key))
                return option;
        }

        return null;
    }

}
