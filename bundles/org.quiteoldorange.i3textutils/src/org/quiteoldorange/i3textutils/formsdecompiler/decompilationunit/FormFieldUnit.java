/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.eclipse.emf.common.util.EMap;
import org.quiteoldorange.i3textutils.formsdecompiler.CodeGenerator;
import org.quiteoldorange.i3textutils.formsdecompiler.P;

import com._1c.g5.v8.dt.form.model.AbstractDataPath;
import com._1c.g5.v8.dt.form.model.FormElementTitleLocation;
import com._1c.g5.v8.dt.form.model.FormField;
import com._1c.g5.v8.dt.form.model.ItemHorizontalAlignment;
import com._1c.g5.v8.dt.form.model.ItemVerticalAlignment;
import com._1c.g5.v8.dt.form.model.ManagedFormFieldType;
import com._1c.g5.v8.dt.mcore.Color;
import com._1c.g5.v8.dt.mcore.Font;

/**
 * @author ozolotarev
 *
 */
public class FormFieldUnit
    extends FormItemUnit
{

    private ItemVerticalAlignment mVerticalAlign;
    private ItemHorizontalAlignment mHorizontalAlign;
    private ManagedFormFieldType mFormFieldType;
    private FormElementTitleLocation mTitleLocation;
    private EMap<String, String> mTooltip;
    private AbstractDataPath mDataPath;
    private AbstractDataPath mFooterDataPath;
    private Font mFooterFont;
    private Font mTitleFont;
    private Color mFooterBackColor;
    private Color mFooterTextColor;
    private Color mTitleBackColor;
    private Color mTitleTextColor;

    /**
     * @param formgroup
     * @param item
     */
    public FormFieldUnit(FormField item)
    {
        super(FormItemUnit.ItemTypes.FormField, item);

        mName = item.getName();
        mFormFieldType = item.getType();
        mVerticalAlign = item.getGroupVerticalAlign();
        mHorizontalAlign = item.getGroupHorizontalAlign();

        mTitles = item.getTitle();
        mTooltip = item.getToolTip();

        mTitleLocation = item.getTitleLocation();
        mDataPath = item.getDataPath();
        mFooterDataPath = item.getFooterDataPath();

        mTitleTextColor = item.getTitleTextColor();
        mFooterTextColor = item.getFooterTextColor();
        mTitleBackColor = item.getTitleBackColor();
        mFooterBackColor = item.getFooterBackColor();
        mTitleFont = item.getTitleFont();
        mFooterFont = item.getFooterFont();
    }

    @Override
    public void decompile(CodeGenerator b)
    {
        super.decompile(b);

        b.writeProperty(P.Type, mFormFieldType);
        b.writeProperty(P.VerticalAlign, mVerticalAlign);
        b.writeProperty(P.HorizontalAlign, mHorizontalAlign);

        b.writeMultilangString(P.Caption, mTitles);
        b.writeMultilangString(P.ToolTip, mTooltip);

        b.writeProperty(P.TitleLocation, mTitleLocation);

        b.writeProperty(P.DataPath, mDataPath);
        b.writeProperty(P.FooterDataPath, mFooterDataPath);

        b.writeProperty(P.TitleTextColor, mTitleTextColor);
        b.writeProperty(P.FooterTextColor, mFooterTextColor);
        b.writeProperty(P.TitleBackColor, mTitleBackColor);
        b.writeProperty(P.FooterBackColor, mFooterBackColor);

        b.writeProperty(P.TitleFont, mTitleFont);
        b.writeProperty(P.FooterFont, mFooterFont);

//
//        СтруктураОбработчиков.Вставить("ПриИзменении");
//        СтруктураОбработчиков.Вставить("НачалоВыбора");
//        СтруктураОбработчиков.Вставить("НачалоВыбораИзСписка");
//        СтруктураОбработчиков.Вставить("Очистка");
//        СтруктураОбработчиков.Вставить("Регулирование");
//        СтруктураОбработчиков.Вставить("Открытие");
//        СтруктураОбработчиков.Вставить("Создание");
//        СтруктураОбработчиков.Вставить("ОбработкаВыбора");
//        СтруктураОбработчиков.Вставить("ИзменениеТекстаРедактирования");
//        СтруктураОбработчиков.Вставить("АвтоПодбор");
//        СтруктураОбработчиков.Вставить("ОкончаниеВводаТекста");
//
//
//        Если ЭлементОбразец.Вид = ВидПоляФормы.ПолеВвода Тогда
//            СтруктураКопируемыхСвойств.Вставить("КнопкаВыбора",Неопределено);
//            СтруктураКопируемыхСвойств.Вставить("КнопкаВыпадающегоСписка",Неопределено);
//            СтруктураКопируемыхСвойств.Вставить("КнопкаОткрытия",Неопределено);
//            СтруктураКопируемыхСвойств.Вставить("КнопкаОчистки",Неопределено);
//            СтруктураКопируемыхСвойств.Вставить("КнопкаРегулирования",Неопределено);
//            СтруктураКопируемыхСвойств.Вставить("КнопкаСоздания",Неопределено);
//        Иначе
//            //Другие виды
//        КонецЕсли;

    }

}
