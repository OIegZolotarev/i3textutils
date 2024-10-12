/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;

import com._1c.g5.v8.dt.form.model.FormGroup;
import com._1c.g5.v8.dt.form.model.FormItem;
import com._1c.g5.v8.dt.form.model.ManagedFormGroupType;

/**
 * @author ozolotarev
 *
 */
public class FormGroupUnit
    extends FormItemUnit
{

    @Override
    public void decompile(StringBuilder output, DecompilationContext context)
    {
        // TODO Auto-generated method stub
        super.decompile(output, context);

        output.append(serializeManagedGroupType(false));
    }

    private ManagedFormGroupType mGroupType;

    /**
     * @param type
     */
    public FormGroupUnit(FormGroup group)
    {
        super(ItemTypes.FormGroup, group);
        mGroupType = group.getType();
        mTitles = group.getTitle();

        for (FormItem items : group.getItems())
        {
            addChildren(FormItemUnit.construct(items));
        }
    }

    private String serializeManagedGroupType(boolean isRussian)
    {
        if (isRussian)
        {
            switch (mGroupType)
            {
            case BUTTON_GROUP:
                return "ВидГруппыФормы.ГруппаКнопок"; //$NON-NLS-1$
            case COLUMN_GROUP:
                return "ВидГруппыФормы.ГруппаКолонок"; //$NON-NLS-1$
            case COMMAND_BAR:
                return "ВидГруппыФормы.КоманднаяПанель"; //$NON-NLS-1$
            case PAGE:
                return "ВидГруппыФормы.Страница"; //$NON-NLS-1$
            case PAGES:
                return "ВидГруппыФормы.Страницы"; //$NON-NLS-1$
            case POPUP:
                return "ВидГруппыФормы.Подменю"; //$NON-NLS-1$
            case USUAL_GROUP:
                return "ВидГруппыФормы.ОбычнаяГруппа"; //$NON-NLS-1$
            }
        }
        else
        {
            switch (mGroupType)
            {
            case BUTTON_GROUP:
                return "FormGroupType.ButtonGroup"; //$NON-NLS-1$
            case COLUMN_GROUP:
                return "FormGroupType.ColumnGroup"; //$NON-NLS-1$
            case COMMAND_BAR:
                return "FormGroupType.CommandBar"; //$NON-NLS-1$
            case PAGE:
                return "FormGroupType.Page"; //$NON-NLS-1$
            case PAGES:
                return "FormGroupType.Pages"; //$NON-NLS-1$
            case POPUP:
                return "FormGroupType.Popup"; //$NON-NLS-1$
            case USUAL_GROUP:
                return "FormGroupType.UsualGroup"; //$NON-NLS-1$
            }
        }

        return null;
    }

}
