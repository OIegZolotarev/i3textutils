/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler;

import java.util.ListIterator;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EMap;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.V8Color;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.V8Font;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums.ChildFormItemsGroupEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums.FormElementTitleLocationEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums.FormFieldTypeEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums.ItemHorizontalAlignEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums.ItemVerticalAlignEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums.ManagedGroupTypeEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums.TooltipRepresentationEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums.UsualGroupBehaviorEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums.UsualGroupRepresentationEnum;

import com._1c.g5.v8.dt.form.model.AbstractDataPath;
import com._1c.g5.v8.dt.form.model.EventHandler;
import com._1c.g5.v8.dt.form.model.FormChildrenGroup;
import com._1c.g5.v8.dt.form.model.FormElementTitleLocation;
import com._1c.g5.v8.dt.form.model.ItemHorizontalAlignment;
import com._1c.g5.v8.dt.form.model.ItemVerticalAlignment;
import com._1c.g5.v8.dt.form.model.ManagedFormFieldType;
import com._1c.g5.v8.dt.form.model.ManagedFormGroupType;
import com._1c.g5.v8.dt.form.model.TooltipRepresentation;
import com._1c.g5.v8.dt.form.model.UsualGroupBehavior;
import com._1c.g5.v8.dt.form.model.UsualGroupRepresentation;
import com._1c.g5.v8.dt.mcore.Color;
import com._1c.g5.v8.dt.mcore.Font;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class CodeGenerator
{
    private DecompilationSettings mDecompilationConfig;
    private StringBuilder mStringBuilder;
    private String mObjectName;
    private ScriptVariant mScriptVariant;
    private boolean mOutputDefaultValues;

    public void beginNewFormItem()
    {
        mObjectName = mDecompilationConfig.getNewItemTemplateName();
    }

    public void setOutputDefaultValues(boolean flag)
    {
        mOutputDefaultValues = flag;
    }

    public CodeGenerator(DecompilationSettings config)
    {
        mDecompilationConfig = config;
        mScriptVariant = config.scriptVariant();
        mStringBuilder = new StringBuilder();
    }


    /**
     * @param modifiesStoredData
     * @return
     */
    public String serializeBoolean(boolean value)
    {

        if (!value)
        {
            switch (mScriptVariant)
            {
            case ENGLISH:
                return "False"; //$NON-NLS-1$
            case RUSSIAN:
                return "Ложь"; //$NON-NLS-1$
            default:
                return "Ложь"; //$NON-NLS-1$
            }
        }
        else
        {
            switch (mScriptVariant)
            {
            case ENGLISH:
                return "True"; //$NON-NLS-1$
            case RUSSIAN:
                return "Истина"; //$NON-NLS-1$
            default:
                return "Истина"; //$NON-NLS-1$
            }
        }
    }

    public <T> void writeProperty(String propertyName, T value)
    {
        if (value == null)
            return;

        if (int.class.isInstance(value))
        {
            int iValue = (int)value;

            if (mOutputDefaultValues || iValue != 0)
            {
                String line = String.format("%s.%s = %d;\n", mObjectName, propertyName, iValue); //$NON-NLS-1$
                append(line);
            }
        }
        else if (String.class.isInstance(value))
        {
            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, value); //$NON-NLS-1$
            append(line);
        }
        else if (boolean.class.isInstance(value) || Boolean.class.isInstance(value))
        {
            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, serializeBoolean((boolean)value)); //$NON-NLS-1$
            append(line);
        }
        else if (ManagedFormGroupType.class.isInstance(value))
        {
            ManagedFormGroupType type = (ManagedFormGroupType)value;

            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, //$NON-NLS-1$
                ManagedGroupTypeEnum.Instance.serialize(type, mScriptVariant));
            append(line);
        }
        else if (TooltipRepresentation.class.isInstance(value))
        {
            TooltipRepresentation repr = (TooltipRepresentation)value;

            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, //$NON-NLS-1$
                TooltipRepresentationEnum.Instance.serialize(repr, mScriptVariant));
            append(line);
        }
        else if (Color.class.isInstance(value))
        {
            Color col = (Color)value;

            String line =
                String.format("%s.%s = %s;\n", mObjectName, propertyName, V8Color.serialize(col, mDecompilationConfig)); //$NON-NLS-1$

            append(line);
        }
        else if (Font.class.isInstance(value))
        {
            Font font = (Font)value;

            String line =
                String.format("%s.%s = %s;\n", mObjectName, propertyName, V8Font.serialize(font, mDecompilationConfig)); //$NON-NLS-1$

            append(line);

        }
        else if (FormChildrenGroup.class.isInstance(value))
        {
            FormChildrenGroup group = (FormChildrenGroup)value;

            String line =
                String.format("%s.%s = %s;\n", mObjectName, propertyName, //$NON-NLS-1$
                    ChildFormItemsGroupEnum.Instance.serialize(group, mScriptVariant));

            append(line);
        }
        else if (UsualGroupRepresentation.class.isInstance(value))
        {
            UsualGroupRepresentation repr = (UsualGroupRepresentation)value;

            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, //$NON-NLS-1$
                UsualGroupRepresentationEnum.Instance.serialize(repr, mScriptVariant));

            append(line);
        }
        else if (UsualGroupBehavior.class.isInstance(value))
        {
            UsualGroupBehavior behavior = (UsualGroupBehavior)value;

            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, //$NON-NLS-1$
                UsualGroupBehaviorEnum.Instance.serialize(behavior, mScriptVariant));

            append(line);
        }
        else if (AbstractDataPath.class.isInstance(value))
        {
            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, //$NON-NLS-1$
                serializeAbstractDataPath((AbstractDataPath)value));

            append(line);
        }
        else if (ItemVerticalAlignment.class.isInstance(value))
        {
            ItemVerticalAlignment aligment = (ItemVerticalAlignment)value;

            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, //$NON-NLS-1$
                ItemVerticalAlignEnum.Instance.serialize(aligment, mScriptVariant));

            append(line);

        }
        else if (ItemHorizontalAlignment.class.isInstance(value))
        {
            ItemHorizontalAlignment aligment = (ItemHorizontalAlignment)value;

            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, //$NON-NLS-1$
                ItemHorizontalAlignEnum.Instance.serialize(aligment, mScriptVariant));

            append(line);

        }
        else if (ManagedFormFieldType.class.isInstance(value))
        {
            ManagedFormFieldType field_type  = (ManagedFormFieldType)value;

            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, //$NON-NLS-1$
                FormFieldTypeEnum.Instance.serialize(field_type, mScriptVariant));

            append(line);

        }
        else if (FormElementTitleLocation.class.isInstance(value))
        {
            FormElementTitleLocation titleLocation = (FormElementTitleLocation)value;

            String line = String.format("%s.%s = %s;\n", mObjectName, propertyName, //$NON-NLS-1$
                FormElementTitleLocationEnum.Instance.serialize(titleLocation, mScriptVariant));

            append(line);

        }


    }

    public void writeMultilangString(String property, EMap<String, String> titles)
    {
        // Если заголовок не указан, то ничего не пишем
        if (titles.size() == 0)
            return;

        // https://its.1c.ru/db/v8std/content/761/hdoc
        // Оказывается НСтр надо лепить везде в интерфейсных текстах.
        // Поэтому так делать не комильфо.

        //        if (mTitles.size() == 1)
        //        {
        //            return mTitles.get(0).getValue();
        //        }

        // "ru = 'Здравствуйте'; en = 'Hello world'"

        String result = ""; //$NON-NLS-1$
        boolean firstEntry = true;

        ListIterator<Entry<String, String>> iter = titles.listIterator();

        while (iter.hasNext())
        {
            Entry<String, String> entry = iter.next();
            String langDescr = String.format("%s = '%s'", entry.getKey(), entry.getValue()); //$NON-NLS-1$

            if (!firstEntry)
                result = result + ";" + langDescr; //$NON-NLS-1$
            else
                result = langDescr;

        }


        String line = String.format("%s.%s = %s;\n", mObjectName, property, //$NON-NLS-1$
            String.format("%s(\"%s\")", mDecompilationConfig.getNStrExpression(), result)); //$NON-NLS-1$
        append(line);
    }

    public void append(String s)
    {
        mStringBuilder.append(s);
    }

    private String serializeAbstractDataPath(AbstractDataPath p)
    {
        // TODO: стандартные реквизиты (например "Проведен") сериализуются в английский вариант
        // не смотря на то что конфа пишется на русском (проверено на 2023.3.6)

        String result = ""; //$NON-NLS-1$

        for (String segment : p.getSegments())
            result = result + segment + "."; //$NON-NLS-1$

        result = result.substring(0, result.length() - 1);
        return result;
    }

    @Override
    public String toString()
    {
        return mStringBuilder.toString();
    }

    /**
     * @return
     */
    public DecompilationSettings getDecompilationSettings()
    {
        return mDecompilationConfig;
    }

    /**
     * @param handler
     */
    public void writeEventHandler(EventHandler handler)
    {
        // Форма.Элементы.ТоварыСумма.УстановитьДействие("ПриИзменении", "ЦАУ_ТоварыСуммаПриИзменении");
        String eventName = null;
        String setEventMethod = null;

        switch (mScriptVariant)
        {
        case ENGLISH:
            eventName = handler.getEvent().getName();
            setEventMethod = "SetAction"; //$NON-NLS-1$
            break;
        case RUSSIAN:
            eventName = handler.getEvent().getNameRu();
            setEventMethod = "УстановитьДействие"; //$NON-NLS-1$
            break;
        default:
            eventName = handler.getEvent().getNameRu();
            setEventMethod = "УстановитьДействие"; //$NON-NLS-1$
            break;
        }

        String handlerName = handler.getName();

        String line = String.format("%s.%s(\"%s\", \"%s\");\n", mObjectName, setEventMethod, //$NON-NLS-1$
            eventName, handlerName);

        append(line);
    }
}
