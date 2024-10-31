/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums;

import com._1c.g5.v8.dt.form.model.FormChildrenGroup;

/**
 * @author ozolotarev
 *
 */
public class ChildFormItemsGroupEnum
    extends BuiltInEnum<FormChildrenGroup>
{
    private ChildFormItemsGroupEnum()
    {
        super("ГруппировкаПодчиненныхЭлементовФормы", "ChildFormItemsGroup"); //$NON-NLS-1$ //$NON-NLS-2$


        addValue(FormChildrenGroup.VERTICAL, "Вертикальная", "Vertical");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(FormChildrenGroup.HORIZONTAL, "Горизонтальная", "Horizontal");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(FormChildrenGroup.ALWAYS_HORIZONTAL, "ГоризонтальнаяВсегда", "AlwaysHorizontal");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(FormChildrenGroup.HORIZONTAL_IF_POSSIBLE, "ГоризонтальнаяЕслиВозможно", "HorizontalIfPossible");//$NON-NLS-1$ //$NON-NLS-2$
    }

    public static ChildFormItemsGroupEnum Instance = new ChildFormItemsGroupEnum();
}
