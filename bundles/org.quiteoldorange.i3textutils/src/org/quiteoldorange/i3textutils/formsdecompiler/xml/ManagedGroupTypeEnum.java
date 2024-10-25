/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.xml;

import com._1c.g5.v8.dt.form.model.ManagedFormGroupType;

/**
 * @author ozolotarev
 *
 */
public class ManagedGroupTypeEnum
    extends BuiltInEnum<ManagedFormGroupType>
{

    /**
     * @param nameRu
     * @param nameEn
     */
    private ManagedGroupTypeEnum()
    {
        super("ВидГруппыФормы", "FormGroupType"); //$NON-NLS-1$//$NON-NLS-2$
        //

        addValue(ManagedFormGroupType.BUTTON_GROUP, "ГруппаКнопок", "ButtonGroup"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(ManagedFormGroupType.COLUMN_GROUP, "ГруппаКолонок", "ColumnGroup");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(ManagedFormGroupType.COMMAND_BAR, "КоманднаяПанель", "CommandBar");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(ManagedFormGroupType.PAGE, "Страница", "Page");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(ManagedFormGroupType.PAGES, "Страницы", "Pages");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(ManagedFormGroupType.POPUP, "Подменю", "Popup");//$NON-NLS-1$ //$NON-NLS-2$
        addValue(ManagedFormGroupType.USUAL_GROUP, "ОбычнаяГруппа", "UsualGroup");//$NON-NLS-1$ //$NON-NLS-2$
    }

    public static ManagedGroupTypeEnum Instance = new ManagedGroupTypeEnum();

}
