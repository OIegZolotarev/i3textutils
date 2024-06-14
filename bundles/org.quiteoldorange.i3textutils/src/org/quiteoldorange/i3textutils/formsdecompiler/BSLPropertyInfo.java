/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler;

import java.util.HashMap;

import org.quiteoldorange.i3textutils.formsdecompiler.ElementProperty.FieldDataTypes;
import org.quiteoldorange.i3textutils.formsdecompiler.ElementProperty.Kind;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

public class BSLPropertyInfo
{
    String propertyNameRU;
    String propertyNameEN;
    FieldDataTypes fieldDataType;

    private String getBSLExpressionBoolean(String xmlValue, ScriptVariant scriptVariant)
    {
        switch (scriptVariant)
        {
        case ENGLISH:
            return xmlValue.equals("true") ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        case RUSSIAN:
        default:
            return xmlValue.equals("true") ? "Истина" : "Ложь"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        }
    }

    public BSLPropertyInfo(String propRU, String propEN, FieldDataTypes type)
    {
        propertyNameEN = propEN;
        propertyNameRU = propRU;
        fieldDataType = type;
    }

    public String getBSLExpression(String xmlValue, ScriptVariant scriptVariant)
    {
        switch (fieldDataType)
        {
        case Boolean:
            return getBSLExpression(xmlValue, scriptVariant);
        case Enum:
            return null;
        case String:
            return xmlValue;
        }

        return null;
    }

    public static HashMap<Kind, BSLPropertyInfo> sInfos;

    static
    {
        sInfos = new HashMap<>();
        sInfos.put(Kind.Name, new BSLPropertyInfo("Имя", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.Visible, new BSLPropertyInfo("Видимость", "Visible", FieldDataTypes.Boolean)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.Enabled, new BSLPropertyInfo("Доступность", "Enabled", FieldDataTypes.Boolean)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.ReadOnly, new BSLPropertyInfo("ТолькоПросмотр", "ReadOnly", FieldDataTypes.Boolean)); //$NON-NLS-1$ //$NON-NLS-2$

        // TODO: доделать.
        sInfos.put(Kind.InputFieldType, new BSLPropertyInfo("Тип", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.FormGroupType, new BSLPropertyInfo("Тип", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.DataPath, new BSLPropertyInfo("ПутьКДанным", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.EditMode, new BSLPropertyInfo("РежимРедактирования", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.ShowInFooter, new BSLPropertyInfo("ОтображатьВПодвале", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.AutoMaxWidth, new BSLPropertyInfo("АвтоМаксимальнаяШирина", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.AutoMaxHeight, new BSLPropertyInfo("АвтоМаксимальнаяВысота", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.Width, new BSLPropertyInfo("Ширина", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.Heigth, new BSLPropertyInfo("Высота", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.HorizontalStretch, new BSLPropertyInfo("РастягиватьПоШирине", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.VerticalStretch, new BSLPropertyInfo("Растягивать", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.DropListButton, new BSLPropertyInfo("КнопкаВыбораИзСписка", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.ListChoiceMode, new BSLPropertyInfo("РежимВыбораИзСписка", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.TypeDomainEnabled, new BSLPropertyInfo("ВыборТипа", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.TextEdit, new BSLPropertyInfo("РедактированиеТекста", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$
        sInfos.put(Kind.PlacementArea, new BSLPropertyInfo("Расположение", "Name", FieldDataTypes.String)); //$NON-NLS-1$ //$NON-NLS-2$

    };
}