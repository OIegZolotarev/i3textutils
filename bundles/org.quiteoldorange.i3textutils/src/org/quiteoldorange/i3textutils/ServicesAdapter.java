/**
 *
 */
package org.quiteoldorange.i3textutils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.quiteoldorange.i3textutils.core.QuickFixAdapter;

import com._1c.g5.v8.dt.validation.marker.IMarkerManager;
import com._1c.g5.wiring.IManagedService;
import com.e1c.g5.v8.dt.check.qfix.IFixRepository;
import com.e1c.g5.v8.dt.check.settings.ICheckRepository;
import com.google.inject.Inject;

/**
 * @author ozolotarev
 *
 */
public class ServicesAdapter
    implements IManagedService
{
    @Inject
    private IFixRepository fixRepository;

    @Inject
    private ICheckRepository checksRepository;

    @Inject
    private IMarkerManager markerManager;


    private static ServicesAdapter sInstance;

    ServicesAdapter()
    {
        sInstance = this;
    }

    @Override
    public void activate()
    {
        QuickFixAdapter.bindQuickFixes();
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
}
