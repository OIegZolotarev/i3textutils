/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums;

import com._1c.g5.v8.dt.form.model.FormElementTitleLocation;

/**
 * @author ozolotarev
 *
 */
public class FormElementTitleLocationEnum extends BuiltInEnum<FormElementTitleLocation>
{

    private FormElementTitleLocationEnum()
    {
        super("ПоложениеЗаголовкаЭлементаФормы", "FormItemTitleLocation"); //$NON-NLS-1$ //$NON-NLS-2$

        addValue(FormElementTitleLocation.AUTO, "Авто", "Auto"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(FormElementTitleLocation.TOP, "Верх", "Top"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(FormElementTitleLocation.LEFT, "Лево", "Left"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(FormElementTitleLocation.NONE, "Нет", "None"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(FormElementTitleLocation.BOTTOM, "Низ", "Bottom"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(FormElementTitleLocation.RIGHT, "Право", "Right"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static FormElementTitleLocationEnum Instance = new FormElementTitleLocationEnum();

}
