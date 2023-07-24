/**
 *
 */
package org.quiteoldorange.i3textutils;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.quiteoldorange.i3textutils.core.QuickFixAdapter;

import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.lifecycle.LifecycleParticipant;
import com._1c.g5.v8.dt.lifecycle.LifecyclePhase;
import com._1c.g5.v8.dt.lifecycle.LifecycleService;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;
import com._1c.g5.v8.dt.validation.marker.IMarkerManager;
import com._1c.g5.wiring.IManagedService;
import com.e1c.g5.v8.dt.check.qfix.IFixRepository;
import com.e1c.g5.v8.dt.check.settings.ICheckRepository;
import com.google.inject.Inject;

/**
 * @author ozolotarev
 *
 */

@LifecycleService(name = "i3textutils")
public class ServicesAdapter
    implements IManagedService
{


    @Inject
    private IFixRepository fixRepository;

    @Inject
    private ICheckRepository checksRepository;

    @Inject
    private IMarkerManager markerManager;

    @Inject
    private IV8ProjectManager v8ProjectManager;


    private static ServicesAdapter sInstance;

    public ServicesAdapter()
    {
        sInstance = this;
    }

    @LifecycleParticipant(phase = LifecyclePhase.POST_RESOURCE_LOADING)
    public void postResourceLoading()
    {
        // Здесь надо быть предельно осторожным - любое исключение роняет проект и вызывает полную перегрузку
        // что очень долго

        try
        {
            QuickFixAdapter.bindQuickFixes();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void activate()
    {

    }

    public ScriptVariant getProjectScriptVariant(IProject proj)
    {
        if (v8ProjectManager == null)
            return null;

        IV8Project iv8proj = v8ProjectManager.getProject(proj);
        return iv8proj.getScriptVariant();
    }

    @Override
    public void deactivate()
    {
        // TODO Auto-generated method stub

    }

    public boolean checkValid()
    {
        if (fixRepository == null)
        {
            MessageBox msgBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);

            msgBox.setText("Ошибка");
            msgBox.setMessage("Произошла ошибка при инциализации плагина - fixRepository == null");
            msgBox.open();

            return false;
        }

        if (checksRepository == null)
        {
            MessageBox msgBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);

            msgBox.setText("Ошибка");
            msgBox.setMessage("Произошла ошибка при инциализации плагина - checksRepository == null");
            msgBox.open();

            return false;
        }

        if (markerManager == null)
        {
            MessageBox msgBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);

            msgBox.setText("Ошибка");
            msgBox.setMessage("Произошла ошибка при инциализации плагина - markerManager == null");
            msgBox.open();

            return false;
        }

        return true;
    }

    /**
     * @return the fixRepository
     */
    public IFixRepository getFixRepository()
    {
        if (fixRepository == null)
        {
            MessageBox msgBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);

            msgBox.setText("Ошибка");
            msgBox.setMessage("Произошла ошибка при инциализации плагина - fixRepository == null");
            msgBox.open();

            return null;
        }

        return fixRepository;
    }

    /**
     * @return the markerManager
     */
    public IMarkerManager getMarkerManager()
    {
        if (markerManager == null)
        {
            MessageBox msgBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);

            msgBox.setText("Ошибка");
            msgBox.setMessage("Произошла ошибка при инциализации плагина - markerManager == null");
            msgBox.open();

            return null;
        }

        return markerManager;
    }

    /**
     * @return the sInstance
     */
    public static ServicesAdapter instance()
    {
        return sInstance;
    }

    /**
     * @return the checksRepository
     */
    public ICheckRepository getChecksRepository()
    {
        if (checksRepository == null)
        {
            MessageBox msgBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);

            msgBox.setText("Ошибка");
            msgBox.setMessage("Произошла ошибка при инциализации плагина - checksRepository == null");
            msgBox.open();

            return null;
        }

        return checksRepository;
    }

    /**
     * @return the v8ProjectManager
     */
    public IV8ProjectManager getV8ProjectManager()
    {
        return v8ProjectManager;
    }


}
