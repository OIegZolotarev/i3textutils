/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class DecompilationSettings
{
    private String mNewAttributesArrayName = "НовыеРеквизиты"; //$NON-NLS-1$
    private String mModifiedFormName = "ЭтотОбъект"; //$NON-NLS-1$
    private String mNewAttributeTemplateName = "НовыйРеквизит"; //$NON-NLS-1$
    private String mNewCommandTemplateName = "НоваяКоманда"; //$NON-NLS-1$

    private String mAttributesSectionStart = "/////   Реквизиты   /////\n"; //$NON-NLS-1$
    private String mAttributesSectionEnd = "/////   Конец реквизиты   /////\n"; //$NON-NLS-1$

    private String mCommandsStartSection = "/////   Команды   /////\n"; //$NON-NLS-1$
    private String mCommandsEndSection = "/////   Конец команды   /////\n"; //$NON-NLS-1$

    private String mFormItemsStartSection = "/////   Элементы /////\n"; //$NON-NLS-1$
    private String mFormItemsEndSection = "/////   Конец элементы /////\n"; //$NON-NLS-1$

    private ScriptVariant mScriptVariant = ScriptVariant.RUSSIAN;
    private String mNewTypeDescriptionExpression = "Новый ОписаниеТипов"; //$NON-NLS-1$
    private String mThisFormTemplateName = "ЭтаФорма"; //$NON-NLS-1$
    private String mNewItemTemplateName = "НовыйЭлемент";

    public void setRegionDirectiveUsage()
    {
        // TODO: Английский вариант
        mAttributesSectionStart = "#Область Реквизиты\n"; //$NON-NLS-1$
        mAttributesSectionEnd = "#КонецОбласти\n"; //$NON-NLS-1$

        mCommandsStartSection = "#Область Команды\n"; //$NON-NLS-1$
        mCommandsEndSection = "#КонецОбласти\n"; //$NON-NLS-1$
    }

    public DecompilationSettings(ScriptVariant variant)
    {
        mScriptVariant = variant;
        P.Init(variant == ScriptVariant.RUSSIAN);
    }

    /**
     * @return the newAttributesArrayName
     */
    public String getNewAttributesArrayName()
    {
        return mNewAttributesArrayName;
    }

    /**
     * @return the modifiedFormName
     */
    public String getModifiedFormName()
    {
        return mModifiedFormName;
    }

    public String getNewAttributeExpression()
    {
        if (mScriptVariant == ScriptVariant.RUSSIAN)
            return "Новый РеквизитФормы"; //$NON-NLS-1$
        else
            // TODO: проверить корректность
            return "New FormAttribute"; //$NON-NLS-1$
    }

    /**
     * @return the newAttributeTemplateName
     */
    public String getNewAttributeTemplateName()
    {
        return mNewAttributeTemplateName;
    }

    /**
     * @return
     */
    public String getNewTypeDescriptionExpression()
    {
        // TODO Auto-generated method stub
        return mNewTypeDescriptionExpression;
    }

    /**
     * @return
     */
    public ScriptVariant scriptVariant()
    {
        // TODO Auto-generated method stub
        return mScriptVariant;
    }

    /**
     * @return
     */
    public String getAppendAttributeToNewAttributeArray()
    {
        // TODO: проверить английский вариант
        String appendMethod = mScriptVariant == ScriptVariant.RUSSIAN ? "Добавить" : "Append"; //$NON-NLS-1$//$NON-NLS-2$
        String result =
            String.format("%s.%s(%s);\n", getNewAttributesArrayName(), appendMethod, getNewAttributeTemplateName()); //$NON-NLS-1$

        return result;
    }

    /**
     * @return
     */
    public String getNStrExpression()
    {
        if (mScriptVariant == ScriptVariant.RUSSIAN)
            return "НСтр"; //$NON-NLS-1$
        else
            return "NStr"; //$NON-NLS-1$
    }

    public String getAttributesStartSection()
    {
        return mAttributesSectionStart;
    }

    public String getAttributesEndSection()
    {
        return mAttributesSectionEnd;
    }

    /**
     * @return
     */
    public String getNewCommadTemplateName()
    {
        return mNewCommandTemplateName;
    }

    /**
     * @return
     */
    public String getThisFormTemplateName()
    {
        return mThisFormTemplateName;
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

    /**
     * @return
     */
    public String getCommandsStartSection()
    {
        // TODO Auto-generated method stub
        return mCommandsStartSection;
    }

    /**
     * @return
     */
    public String getCommandsEndSection()
    {
        // TODO Auto-generated method stub
        return mCommandsEndSection;
    }

    /**
     * @return
     */
    public String getFormItemsStartSection()
    {
        // TODO Auto-generated method stub
        return mFormItemsStartSection;
    }

    /**
     * @return
     */
    public String getFormItemsEndSection()
    {
        // TODO Auto-generated method stub
        return mFormItemsEndSection;
    }

    /**
     * @return
     */
    public String getNewItemTemplateName()
    {
        return mNewItemTemplateName;
    }

    /**
     * @return
     */
    public boolean outputDefaultValues()
    {
        // TODO Auto-generated method stub
        return true;
    }
}
