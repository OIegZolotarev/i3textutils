/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.eclipse.emf.common.util.EMap;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationSettings;
import org.quiteoldorange.i3textutils.formsdecompiler.P;

import com._1c.g5.v8.dt.form.model.FormGroup;
import com._1c.g5.v8.dt.form.model.FormItem;
import com._1c.g5.v8.dt.form.model.ManagedFormGroupType;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class FormGroupUnit
    extends FormItemUnit
{
    private boolean mEnabled = true;
    private ManagedFormGroupType mGroupType;
    private int mHeight = 0;

    private boolean mReadonly = false;
    private boolean mVisible = true;

    private int mWidth = 0;
    private boolean mEnableContentChange;
    private EMap<String, String> mToolTip;
    private boolean mHorizontalStretch = false;
    private boolean mVerticalStretch = false;

    /**
     * @param type
     */
    public FormGroupUnit(FormGroup group)
    {
        super(ItemTypes.FormGroup, group);
        mGroupType = group.getType();
        mTitles = group.getTitle();

        mEnabled = group.isEnabled();
        mReadonly = group.isReadOnly();
        mVisible = group.isVisible();
        mEnableContentChange = group.isEnableContentChange();
        mToolTip = group.getToolTip();

        mHeight = group.getHeight();
        mWidth = group.getWidth();

        // За каким-то чертом эти свойства Boolean вместо boolean
        // С одной стороны неплохо - можно понять что свойства не определены (==null)
        // С другой стороны не понятно почему с остальными так не сделали.
        if (group.getVerticalStretch() != null)
            mVerticalStretch = group.getVerticalStretch();

        if (group.getHorizontalStretch() != null)
            mHorizontalStretch = group.getHorizontalStretch();

        for (FormItem items : group.getItems())
        {
            addChildren(FormItemUnit.construct(items));
        }
    }


    interface StringPropertyWriter
    {
        void w(String property, String value);
    };

    interface BooleanPropertyWriter
    {
        void w(String property, boolean value);
    };

    interface IntPropertyWriter
    {
        void w(String property, int value);
    }

    @Override
    public void decompile(StringBuilder b, DecompilationContext context)
    {
        super.decompile(b, context);

        DecompilationSettings cfg = context.getDecompilationSettings();

        boolean outputDefaultValues = cfg.outputDefaultValues();
        boolean isRussian = cfg.scriptVariant() == ScriptVariant.RUSSIAN;

        String newItem = cfg.getNewItemTemplateName();

        StringPropertyWriter sp = (String p, String v) -> {
            String line = String.format("%s.%s = %s;\n", newItem, p, v); //$NON-NLS-1$
            b.append(line);
        };

        BooleanPropertyWriter bp = (String p, boolean v) -> {
            String line = String.format("%s.%s = %s;\n", newItem, p, cfg.serializeBoolean(v)); //$NON-NLS-1$
            b.append(line);
        };

        IntPropertyWriter ip = (String p, int v) -> {
            String line = String.format("%s.%s = %d;\n", newItem, p, v); //$NON-NLS-1$
            b.append(line);
        };

        sp.w(P.Type, serializeManagedGroupType(isRussian));

        // TODO: Придумать какие-то оболочки для свойствами 1С, или оставить все как есть?
        // Чтобы не писать такой огород, а сделать более красивый код? Или пофигу?
        if (!mVisible || outputDefaultValues)
            bp.w(P.Visible, mVisible);

        if (mHeight != 0 || outputDefaultValues)
            ip.w(P.Height, mHeight);

        if (mWidth != 0 || outputDefaultValues)
            ip.w(P.Width, mWidth);

        if (!mEnabled || outputDefaultValues)
            bp.w(P.Enabled, mEnabled);


        sp.w(P.Caption, serializeMultiLangualString(mTitles, cfg));
        sp.w(P.ToolTip, serializeMultiLangualString(mToolTip, cfg));

        // TODO: СтруктураКопируемыхСвойств.Вставить("ОтображениеПодсказки",ОтображениеПодсказки.Авто);

        bp.w(P.EnableContentChange, mEnableContentChange);
        bp.w(P.VerticalStretch, mVerticalStretch);
        bp.w(P.HorizontalStretch, mHorizontalStretch);
        bp.w(P.Readonly, mReadonly);

//        СтруктураКопируемыхСвойств.Вставить("ОтображениеПодсказки",ОтображениеПодсказки.Авто);//
//        СтруктураКопируемыхСвойств.Вставить("ЦветТекстаЗаголовка",Новый Цвет());
//        СтруктураКопируемыхСвойств.Вставить("ШрифтЗаголовка",Новый Шрифт());
//
//        Если ЭлементОбразец.Вид = ВидГруппыФормы.ОбычнаяГруппа Тогда
//            СтруктураКопируемыхСвойств.Вставить("Группировка",ГруппировкаПодчиненныхЭлементовФормы.Вертикальная);
//            СтруктураКопируемыхСвойств.Вставить("ОтображатьЗаголовок",Истина);
//            СтруктураКопируемыхСвойств.Вставить("Отображение",ОтображениеОбычнойГруппы.ОбычноеВыделение);
//            СтруктураКопируемыхСвойств.Вставить("Поведение",ПоведениеОбычнойГруппы.Обычное);
//            СтруктураКопируемыхСвойств.Вставить("ПутьКДаннымЗаголовка","");  //Определяется в "ДанныеЭлементов"
//        Иначе
//            //Другие виды
//        КонецЕсли;

        // .append(serializeManagedGroupType(false));
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

    @Override
    public String toString()
    {
        return String.format("Группа формы: %s", getName()); //$NON-NLS-1$
    }

}
