/**
 *
 */
package org.quiteoldorange.i3textutils.preferences.projectoptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

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

        HashMap<String, Composite> mOptionsGroups = new HashMap<>();

        for (IProjectOption option : mOptions)
        {
            String optionGroup = option.getGroupName();
            Composite targetComposite = null;

            if (optionGroup != null)
            {
                targetComposite = mOptionsGroups.get(optionGroup);

                if (targetComposite == null)
                {
                    Group group = new Group(composite, SWT.NONE);
                    group.setText(optionGroup);

                    GridLayout layout = new GridLayout(2, false);
                    group.setLayout(layout);
                    group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

                    targetComposite = group;


                    mOptionsGroups.put(optionGroup, targetComposite);
                }
            }
            else
            {
                targetComposite = composite;
            }

            option.createWidget(targetComposite);
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
