/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;


import java.util.ListIterator;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EMap;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationSettings;

import com._1c.g5.v8.dt.form.model.FormAttribute;
import com._1c.g5.v8.dt.mcore.TypeDescription;
import com._1c.g5.v8.dt.mcore.TypeItem;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class Attribute
    extends DecompilationUnit
{
    private String mName;
    private EMap<String, String> mTitles;
    private TypeDescription mValueType;

    public Attribute(FormAttribute formAttribute)
    {
        mName = formAttribute.getName();
        mTitles = formAttribute.getTitle();
        mValueType = formAttribute.getValueType();
    }

    @Override
    void Decompile(StringBuilder output, DecompilationContext context)
    {
        DecompilationSettings cfg = context.getDecompilationSettings();

        String newAttribute = cfg.getNewAttributeTemplateName();
        boolean isRussian = cfg.scriptVariant() == ScriptVariant.RUSSIAN;

        if (mValueType.getTypes().size() == 1)
        {
            TypeItem type = mValueType.getTypes().get(0);

            String typeDescription = String.format("%s(\"%s\")", cfg.getNewTypeDescriptionExpression(),
                isRussian ? type.getNameRu() : type.getName());

            String construction = String.format("%s = %s(\"%s\", %s);\n", newAttribute,
                cfg.getNewAttributeExpression(), mName, typeDescription);

            output.append(construction);
        }
        else
        {
            // TODO: implement
        }

        String titleStringValue = serializeTitles();
        String captionProperty = isRussian ? "Заголовок" : "Title"; //$NON-NLS-1$//$NON-NLS-2$
        String captionExpression = String.format("%s.%s = %s;\n", newAttribute, captionProperty, titleStringValue); //$NON-NLS-1$

        output.append(captionExpression);
        output.append(cfg.getAppendAttributeToNewAttributeArray());
        output.append("\n"); //$NON-NLS-1$
    }

    /**
     * @return
     */
    private String serializeTitles()
    {

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


        ListIterator<Entry<String, String>> iter = mTitles.listIterator();

        while (iter.hasNext())
        {
            Entry<String, String> entry = iter.next();
            String langDescr = String.format("%s = '%s'", entry.getKey(), entry.getValue()); //$NON-NLS-1$

            if (!firstEntry)
                result = result + ";" + langDescr; //$NON-NLS-1$
            else
                result = langDescr;

        }

        return null;

    }

}
