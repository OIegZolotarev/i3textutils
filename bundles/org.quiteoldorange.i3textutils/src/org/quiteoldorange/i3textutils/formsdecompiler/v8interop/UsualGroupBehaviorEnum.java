/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop;

import com._1c.g5.v8.dt.form.model.UsualGroupBehavior;

/**
 * @author ozolotarev
 *
 */
public class UsualGroupBehaviorEnum
    extends BuiltInEnum<UsualGroupBehavior>
{
    private UsualGroupBehaviorEnum()
    {
        super("ПоведениеОбычнойГруппы", "UsualGroupBehavior"); //$NON-NLS-1$ //$NON-NLS-2$

        addValue(UsualGroupBehavior.AUTO, "Авто", "Auto");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(UsualGroupBehavior.POP_UP, "Всплывающая", "PopUp");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(UsualGroupBehavior.USUAL, "Обычное", "Usual");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(UsualGroupBehavior.COLLAPSIBLE, "Свертываемая", "Collapsible");//$NON-NLS-1$ //$NON-NLS-2$

    }

    public static UsualGroupBehaviorEnum Instance = new UsualGroupBehaviorEnum();
}
