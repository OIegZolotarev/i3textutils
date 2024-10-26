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


        addValue(FormChildrenGroup.VERTICAL, "Вертикальная", "Vertical");
        addValue(FormChildrenGroup.HORIZONTAL, "Горизонтальная", "Horizontal");
        addValue(FormChildrenGroup.ALWAYS_HORIZONTAL, "ГоризонтальнаяВсегда", "AlwaysHorizontal");
        addValue(FormChildrenGroup.HORIZONTAL_IF_POSSIBLE, "ГоризонтальнаяЕслиВозможно", "HorizontalIfPossible");
    }

    public static ChildFormItemsGroupEnum Instance = new ChildFormItemsGroupEnum();
}
