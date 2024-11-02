/**
 *
 */
package org.quiteoldorange.i3textutils.preferences.projectoptions;

import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.quiteoldorange.i3textutils.preferences.projectoptions.impl.formsdecompiler.FormsDecompilerOptionSet;

/**
 * @author ozolotarev
 *
 */
public class ProjectOptionsManager
{
    private HashMap<IProject, HashMap<String, ProjectOptionsSet>> mProjectsOptions = new HashMap<>();

    public HashMap<String, ProjectOptionsSet> getProjectOptions(IProject project)
    {
        HashMap<String, ProjectOptionsSet> result = mProjectsOptions.get(project);

        if (result == null)
        {
            result = initializeProjectOptions(project);
            mProjectsOptions.put(project, result);
        }

        return result;
    }

    /**
     * @param project
     * @return
     */
    private HashMap<String, ProjectOptionsSet> initializeProjectOptions(IProject project)
    {
        HashMap<String, ProjectOptionsSet> result = new HashMap<>();

        addOptionsSet(result, new FormsDecompilerOptionSet(project));

        for (Entry<String, ProjectOptionsSet> kv : result.entrySet())
        {
            kv.getValue().load();
        }

        return result;
    }

    /**
     * @param result
     * @param formsDecompilerOptionSet
     */
    private void addOptionsSet(HashMap<String, ProjectOptionsSet> result, ProjectOptionsSet optionsSet)
    {
        result.put(optionsSet.getId(), optionsSet);
    }

    /**
     * @param project
     * @param id
     * @return
     */
    public ProjectOptionsSet getProjectOptionsSet(IProject project, String id)
    {
        HashMap<String, ProjectOptionsSet> projectOptions = getProjectOptions(project);
        return projectOptions.get(id);
    }
}
