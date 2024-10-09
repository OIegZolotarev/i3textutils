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

    private String mAttributesSectionStart = "/////   РЕКВИЗИТЫ   /////\n"; //$NON-NLS-1$
    private String mAttributesSectionEnd = "/////   КОНЕЦ РЕКВИЗИТЫ   /////\n"; //$NON-NLS-1$

    private ScriptVariant mScriptVariant = ScriptVariant.RUSSIAN;
    private String mNewTypeDescriptionExpression = "Новый ОписаниеТипов"; //$NON-NLS-1$
    private String mThisFormTemplateName = "ЭтаФорма";

    public DecompilationSettings()
    {

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
        String appendMethod = mScriptVariant == ScriptVariant.RUSSIAN ? "Добавить" : "Append";          //$NON-NLS-1$//$NON-NLS-2$
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
    }
}
