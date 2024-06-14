/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler;

import javax.management.openmbean.InvalidKeyException;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class ElementProperty
{
    public static enum Kind
    {
        Name,
        Visible,
        Enabled,
        ReadOnly,
        InputFieldType,
        FormGroupType,
        DataPath,
        EditMode,
        ShowInFooter,
        AutoMaxWidth,
        AutoMaxHeight,
        Width,
        Heigth,
        HorizontalStretch,
        VerticalStretch,
        DropListButton, // Кнопка выбора из списка
        ListChoiceMode, // Выбор из списка
        TypeDomainEnabled, // Выбор типа
        TextEdit,
        PlacementArea
    };

    static enum FieldDataTypes
    {
        Boolean,
        String,
        Enum
    };

    private Kind mKind;
    private String mValue;

    public ElementProperty(Kind kind, String xmlValue)
    {
        mKind = kind;
        mValue = xmlValue;
    }

    public String generateBSLCode(String variableName, ScriptVariant scriptVariant)
    {
        BSLPropertyInfo info = BSLPropertyInfo.sInfos.get(mKind);

        if (info == null)
            throw new InvalidKeyException("No information for property type implemented"); //$NON-NLS-1$

        String propertyKey = null;

        switch (scriptVariant)
        {
        case ENGLISH:
            propertyKey = info.propertyNameEN;
            break;
        case RUSSIAN:
        default:
            propertyKey = info.propertyNameRU;
            break;
        }

        String value = info.getBSLExpression(mValue, scriptVariant);

        return String.format("%s.%s = %s;", variableName, propertyKey, value); //$NON-NLS-1$
    }

}

