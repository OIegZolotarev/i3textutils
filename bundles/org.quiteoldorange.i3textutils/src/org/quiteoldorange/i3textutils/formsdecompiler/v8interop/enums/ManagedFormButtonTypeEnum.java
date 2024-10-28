/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums;

import com._1c.g5.v8.dt.form.model.ManagedFormButtonType;

/**
 * @author ozolotarev
 *
 */
public class ManagedFormButtonTypeEnum
    extends BuiltInEnum<ManagedFormButtonType>
{
    private ManagedFormButtonTypeEnum()
    {
        super("ВидКнопкиФормы", "FormButtonType"); //$NON-NLS-1$ //$NON-NLS-2$

        addValue(ManagedFormButtonType.HYPERLINK, "Гиперссылка", "Hyperlink"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(ManagedFormButtonType.COMMAND_BAR_HYPERLINK, "ГиперссылкаКоманднойПанели", "CommandBarHyperlink"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(ManagedFormButtonType.COMMAND_BAR_BUTTON, "КнопкаКоманднойПанели", "CommandBarButton"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(ManagedFormButtonType.USUAL_BUTTON, "ОбычнаяКнопка", "UsualButton"); //$NON-NLS-1$ //$NON-NLS-2$

    }

    public static ManagedFormButtonTypeEnum Instance = new ManagedFormButtonTypeEnum();
}
