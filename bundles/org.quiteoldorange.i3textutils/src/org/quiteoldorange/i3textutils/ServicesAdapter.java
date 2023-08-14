/**
 *
 */
package org.quiteoldorange.i3textutils;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.quiteoldorange.i3textutils.codemining.CodeminingsChangeListener;
import org.quiteoldorange.i3textutils.core.QuickFixAdapter;

import com._1c.g5.v8.dt.core.handle.V8ModelCore;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.form.service.item.IFormItemManagementService;
import com._1c.g5.v8.dt.form.service.item.IFormItemMovementService;
import com._1c.g5.v8.dt.form.service.item.IFormItemTypeManagementService;
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

    @Inject
    IBmModelManager bmModelManager;

    @Inject
    IFormItemTypeManagementService formTypeManagment;

    @Inject
    IFormItemManagementService formItemManagement;

    @Inject
    IFormItemMovementService formItemMovementService;

    CodeminingsChangeListener codeMiningChangeListener = new CodeminingsChangeListener();

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

        // Вызывается дважды - по каждому проекту видимо.

        try
        {
            QuickFixAdapter.bindQuickFixes();
            V8ModelCore.getV8Model().addElementChangeListener(codeMiningChangeListener);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void activate()
    {
        // Do nothing
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
        try
        {
            V8ModelCore.getV8Model().removeElementChangeListener(codeMiningChangeListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public boolean checkValid()
    {
        if (fixRepository == null)
        {
            MessageBox msgBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);

            msgBox.setText(Messages.ServicesAdapter_Error);
            msgBox.setMessage(Messages.ServicesAdapter_NullFixRepository);
            msgBox.open();

            return false;
        }

        if (checksRepository == null)
        {
            MessageBox msgBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);

            msgBox.setText(Messages.ServicesAdapter_Error);
            msgBox.setMessage(Messages.ServicesAdapter_NullCheckRepository);
            msgBox.open();

            return false;
        }

        if (markerManager == null)
        {
            MessageBox msgBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);

            msgBox.setText(Messages.ServicesAdapter_Error);
            msgBox.setMessage(Messages.ServicesAdapter_NullMarkerManager);
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

            msgBox.setText(Messages.ServicesAdapter_Error);
            msgBox.setMessage(Messages.ServicesAdapter_NullFixRepository);
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

            msgBox.setText(Messages.ServicesAdapter_Error);
            msgBox.setMessage(Messages.ServicesAdapter_NullMarkerManager);
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

            msgBox.setText(Messages.ServicesAdapter_Error);
            msgBox.setMessage(Messages.ServicesAdapter_NullChecksRepository);
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

    /**
     * @return the bmModelManager
     */
    public IBmModelManager getBmModelManager()
    {
        return bmModelManager;
    }

    /**
     * @return the formTypeManagment
     */
    public IFormItemTypeManagementService getFormTypeManagment()
    {
        return formTypeManagment;
    }

    /**
     * @return the formItemManagment
     */
    public IFormItemManagementService getFormItemManagement()
    {
        return formItemManagement;
    }

    /**
     * @return the formItemMovementService
     */
    public IFormItemMovementService getFormItemMovementService()
    {
        return formItemMovementService;
    }


}
