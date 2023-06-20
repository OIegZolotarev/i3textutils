/**
 *
 */
package org.quiteoldorange.i3textutils;

import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;
import org.quiteoldorange.i3textutils.qfix.MissingPragmaQFix2;
import org.quiteoldorange.i3textutils.qfix.MissingPragmaQFix3;

import com._1c.g5.wiring.IManagedService;
import com.e1c.g5.v8.dt.check.qfix.IFix;
import com.e1c.g5.v8.dt.check.qfix.IFixContext;
import com.e1c.g5.v8.dt.check.qfix.IFixRepository;
import com.e1c.g5.v8.dt.check.settings.CheckUid;
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

        CheckUid id = new CheckUid("form-module-missing-pragma", i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE);

        IFix<? extends IFixContext> instance2 =
            new MissingPragmaQFix3(id, MissingPragmaQFix3.AddAtServer, "&НаСервере", "&AtServer");
        fixRepository.registerFix(instance2);

        IFix<? extends IFixContext> instance3 =
            new MissingPragmaQFix3(id, MissingPragmaQFix3.AddAtServer, "&НаКлиенте", "&AtClient");
        fixRepository.registerFix(instance3);
    }

    @Override
    public void deactivate()
    {
        // TODO Auto-generated method stub

    }
}
