/**
 *
 */
package org.quiteoldorange.i3textutils;

import com._1c.g5.wiring.IManagedService;
import com.e1c.g5.v8.dt.check.qfix.IFix;
import com.e1c.g5.v8.dt.check.qfix.IFixContext;
import com.e1c.g5.v8.dt.check.qfix.IFixRepository;
import com.google.inject.Inject;

/**
 * @author ozolotarev
 *
 */
public class QuickFixRegistrator
    implements IManagedService
{
    @Inject
    private IFixRepository fixRepository;

    @Override
    public void activate()
    {
        IFix<? extends IFixContext> instance = new MissingPragmaQFix2();

        fixRepository.registerFix(instance);
    }

    @Override
    public void deactivate()
    {
        // TODO Auto-generated method stub

    }
}
