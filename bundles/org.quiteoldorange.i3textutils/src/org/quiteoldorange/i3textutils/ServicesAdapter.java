/**
 *
 */
package org.quiteoldorange.i3textutils;

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

    }

    @Override
    public void deactivate()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @return the fixRepository
     */
    public IFixRepository getFixRepository()
    {
        return fixRepository;
    }

    /**
     * @return the markerManager
     */
    public IMarkerManager getMarkerManager()
    {
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
        return checksRepository;
    }
}
