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

    private ScriptVariant mScriptVariant = ScriptVariant.RUSSIAN;
    private String mNewTypeDescriptionExpression = "Новый ОписаниеТипов"; //$NON-NLS-1$

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
}
