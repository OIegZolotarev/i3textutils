/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop;

import com._1c.g5.v8.dt.form.model.UsualGroupRepresentation;

/**
 * @author ozolotarev
 *
 */
public class UsualGroupRepresentationEnum
    extends BuiltInEnum<UsualGroupRepresentation>
{
    private UsualGroupRepresentationEnum()
    {
        super("ОтображениеОбычнойГруппы", "UsualGroupRepresentation"); //$NON-NLS-1$ //$NON-NLS-2$

        addValue(UsualGroupRepresentation.NONE, "Нет", "None"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(UsualGroupRepresentation.NORMAL_SEPARATION, "ОбычноеВыделение", "NormalSeparation"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(UsualGroupRepresentation.STRONG_SEPARATION, "СильноеВыделение", "StrongSeparation"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(UsualGroupRepresentation.WEAK_SEPARATION, "СлабоеВыделение", "None"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static UsualGroupRepresentationEnum Instance = new UsualGroupRepresentationEnum();
}
