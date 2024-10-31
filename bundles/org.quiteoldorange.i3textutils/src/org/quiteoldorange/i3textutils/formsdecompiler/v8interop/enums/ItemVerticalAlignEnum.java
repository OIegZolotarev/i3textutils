/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums;

import com._1c.g5.v8.dt.form.model.ItemVerticalAlignment;

/**
 * @author ozolotarev
 *
 */
public class ItemVerticalAlignEnum
    extends BuiltInEnum<ItemVerticalAlignment>
{
    /**
     *
     */
    private ItemVerticalAlignEnum()
    {
        super("ВертикальноеПоложениеЭлемента", "ItemVerticalAlign"); //$NON-NLS-1$//$NON-NLS-2$

        addValue(ItemVerticalAlignment.AUTO, "Авто", "Auto");  //$NON-NLS-1$//$NON-NLS-2$
        addValue(ItemVerticalAlignment.TOP, "Вверх", "Top");  //$NON-NLS-1$//$NON-NLS-2$
        addValue(ItemVerticalAlignment.BOTTOM, "Низ", "Bottom");  //$NON-NLS-1$//$NON-NLS-2$
        addValue(ItemVerticalAlignment.CENTER, "Центр", "Center");  //$NON-NLS-1$//$NON-NLS-2$
    }

    public static ItemVerticalAlignEnum Instance = new ItemVerticalAlignEnum();
}
