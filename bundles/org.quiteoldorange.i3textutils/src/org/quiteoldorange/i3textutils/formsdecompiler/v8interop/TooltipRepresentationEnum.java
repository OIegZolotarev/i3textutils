/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop;

import com._1c.g5.v8.dt.form.model.TooltipRepresentation;

/**
 * @author ozolotarev
 *
 */
public class TooltipRepresentationEnum
    extends BuiltInEnum<TooltipRepresentation>
{
    private TooltipRepresentationEnum()
    {
        super("ОтображениеПодсказки", "ToolTipRepresentation"); //$NON-NLS-1$ //$NON-NLS-2$

        addValue(TooltipRepresentation.AUTO, "Авто", "Auto"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(TooltipRepresentation.BALLOON, "Всплывающая", "Balloon");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(TooltipRepresentation.BUTTON, "Кнопка", "Button");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(TooltipRepresentation.NONE, "Нет", "None");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(TooltipRepresentation.SHOW_AUTO, "ОтображатьАвто", "ShowAuto");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(TooltipRepresentation.SHOW_TOP, "ОтображатьСверу", "ShowTop");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(TooltipRepresentation.SHOW_LEFT, "ОтображатьСлева", "ShowLeft");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(TooltipRepresentation.SHOW_BOTTOM, "ОтображатьСнизу", "ShowBottom");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(TooltipRepresentation.SHOW_RIGHT, "ОтображатьСправа", "ShowRight");//$NON-NLS-1$ //$NON-NLS-2$
    }

    public static TooltipRepresentationEnum Instance = new TooltipRepresentationEnum();
}
