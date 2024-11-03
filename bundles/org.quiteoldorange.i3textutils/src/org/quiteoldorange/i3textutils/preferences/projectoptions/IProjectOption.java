/**
 *
 */
package org.quiteoldorange.i3textutils.preferences.projectoptions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.widgets.Composite;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;

/**
 * @author ozolotarev
 *
 */
public abstract class IProjectOption
{
    private String mDefaultValue;
    private String mDescription;

    private String mKey;

    private String mGroupName;
    private String mValue;

    protected IProjectOption(String key, String descritption, String defaultValue)
    {
        mKey = key;
        mDescription = descritption;
        mDefaultValue = defaultValue;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue()
    {
        return mDefaultValue;
    }
    /**
     * @return the description
     */
    public String getDescription()
    {
        return mDescription;
    }

    /**
     * @return the key
     */
    public String getKey()
    {
        return mKey;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return mValue;
    }

    /**
     * @param data
     */
    protected void setValue(String value)
    {
        mValue = value;
    }

    /**
     *
     */
    public void setDefault()
    {
        mValue = mDefaultValue;
    }

    /**
     * @param project
     * @param categoryId
     */
    public boolean save(IProject project, String categoryId)
    {
        String qualifier = i3TextUtilsPlugin.PLUGIN_ID + "." + categoryId; //$NON-NLS-1$

        ProjectScope scope = new ProjectScope(project);
        IEclipsePreferences node = scope.getNode(qualifier);
        node.put(mKey, mValue);

        try
        {
            node.flush();
        }
        catch (org.osgi.service.prefs.BackingStoreException e)
        {
            i3TextUtilsPlugin.logError(e);
            return false;
        }

        return true;

    }

    /**
     * @param project
     * @param id
     */
    public void load(IProject project, String id)
    {
        String qualifier = i3TextUtilsPlugin.PLUGIN_ID + "." + id; //$NON-NLS-1$

        ProjectScope scope = new ProjectScope(project);
        IEclipsePreferences node = scope.getNode(qualifier);
        mValue = node.get(mKey, mDefaultValue);
    }

    public void setGroupName(String groupName)
    {
        mGroupName = groupName;
    }

    abstract public void createWidget(Composite parent);
    abstract public void updateWidgetState();

    /**
     * @return
     */
    public String getGroupName()
    {
        return mGroupName;
    }
}
