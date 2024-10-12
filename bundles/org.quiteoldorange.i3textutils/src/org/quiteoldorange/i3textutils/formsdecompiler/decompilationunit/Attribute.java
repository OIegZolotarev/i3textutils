/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;


import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationSettings;

import com._1c.g5.v8.dt.form.model.FormAttribute;
import com._1c.g5.v8.dt.form.model.FormAttributeAdditionalColumns;
import com._1c.g5.v8.dt.form.model.FormAttributeColumn;
import com._1c.g5.v8.dt.mcore.TypeDescription;
import com._1c.g5.v8.dt.mcore.TypeItem;
import com._1c.g5.v8.dt.metadata.common.FillChecking;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class Attribute
    extends DecompilationUnit
{

    @Override
    public String toString()
    {
        return String.format("Реквизит: %s", getName()); //$NON-NLS-1$
    }

    protected TypeDescription mValueType;
    protected FillChecking mFillChecking;

    protected FormAttribute mEDTAttribute;

    public enum Kind
    {
        Field,
        Table,
        TabularSection,
        Column
    };

    private Kind mKind;


    public Attribute()
    {

    }

    public Attribute(FormAttribute formAttribute)
    {
        mName = formAttribute.getName();
        mTitles = formAttribute.getTitle();
        mValueType = formAttribute.getValueType();

        mFillChecking = formAttribute.getFillChecking();

        if (formAttribute.getAdditionalColumns().size() > 0)
            setKind(Kind.TabularSection);
        else if (formAttribute.getColumns().size() > 0)
            setKind(Kind.Table);

        for (FormAttributeAdditionalColumns set : formAttribute.getAdditionalColumns())
        {
            addChildren(new TabularSectionWrapper(set));
        }

        for (FormAttributeColumn column : formAttribute.getColumns())
        {
            addChildren(new AttributeColumn(column, formAttribute.getName()));
        }

        mEDTAttribute = formAttribute;

        // Объект -> ТабличнаяЧасть -> РеквизитТабличнойЧасти
        // formAttribute.getAdditionalColumns().get(0).getColumns().get(0).getName();

        // Объект -> ДругаяТабличнаяЧасть -> ДругойРеквизитТабличнойЧасти
        // formAttribute.getAdditionalColumns().get(1).getColumns().get(0).getName()

        // Путь к реквизиту
        // formAttribute.getAdditionalColumns().get(1).getTablePath()


        // Реквизит формы с колонками
        // formAttribute.getColumns().get(0).getName()
    }

    @Override
    public void decompile(StringBuilder output, DecompilationContext context)
    {
        DecompilationSettings cfg = context.getDecompilationSettings();

        String newAttribute = cfg.getNewAttributeTemplateName();
        boolean isRussian = cfg.scriptVariant() == ScriptVariant.RUSSIAN;

        if (mValueType.getTypes().size() == 1)
        {
            TypeItem type = mValueType.getTypes().get(0);

            String typeDescription = String.format("%s(\"%s\")", cfg.getNewTypeDescriptionExpression(), //$NON-NLS-1$
                isRussian ? type.getNameRu() : type.getName());

            String construction = String.format("%s = %s(\"%s\", %s);\n", newAttribute, //$NON-NLS-1$
                cfg.getNewAttributeExpression(), mName, typeDescription);

            output.append(construction);
        }
        else
        {
            // TODO: implement
        }

        String titleStringValue = serializeMultiLangualString(mTitles, cfg);
        String captionProperty = isRussian ? "Заголовок" : "Title"; //$NON-NLS-1$//$NON-NLS-2$
        String captionExpression = String.format("%s.%s = %s;\n", newAttribute, captionProperty, titleStringValue); //$NON-NLS-1$

        output.append(captionExpression);


        if (getParent() != null)
        {
            String pathValue = null;

            if (getParent() instanceof TabularSectionWrapper)
            {
                TabularSectionWrapper parent = (TabularSectionWrapper)getParent();
                pathValue = parent.getPathExpression();
            }
            else
            {
                DecompilationUnit attr = getParent();
                pathValue = attr.getName();
            }

            // TODO: проверить английский вариант
            String pathProperty = isRussian ? "Путь" : "Path"; //$NON-NLS-1$//$NON-NLS-2$
            String pathExpression = String.format("%s.%s = \"%s\";\n", newAttribute, pathProperty, pathValue); //$NON-NLS-1$

            output.append(pathExpression);
        }

        output.append(cfg.getAppendAttributeToNewAttributeArray());
    }

    /**
     * @return the kind
     */
    public Kind getKind()
    {
        return mKind;
    }

    /**
     * @param kind the kind to set
     */
    protected void setKind(Kind kind)
    {
        mKind = kind;
    }

}

