/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums;

import com._1c.g5.v8.dt.form.model.ItemHorizontalAlignment;

/**
 * @author ozolotarev
 *
 */
public class ItemHorizontalAlignEnum
    extends BuiltInEnum<ItemHorizontalAlignment>
{
    /**
     *
     */
    private ItemHorizontalAlignEnum()
    {
        super("ГоризонтальноеПоложениеЭлемента", "ItemHorizontalAlign"); //$NON-NLS-1$//$NON-NLS-2$

        addValue(ItemHorizontalAlignment.AUTO, "Авто", "Auto");  //$NON-NLS-1$//$NON-NLS-2$
        addValue(ItemHorizontalAlignment.LEFT, "Лево", "Left");  //$NON-NLS-1$//$NON-NLS-2$
        addValue(ItemHorizontalAlignment.CENTER, "Центр", "Center");  //$NON-NLS-1$//$NON-NLS-2$
        addValue(ItemHorizontalAlignment.RIGHT, "Право", "Right");  //$NON-NLS-1$//$NON-NLS-2$
    }

    public static ItemHorizontalAlignEnum Instance = new ItemHorizontalAlignEnum();
}
