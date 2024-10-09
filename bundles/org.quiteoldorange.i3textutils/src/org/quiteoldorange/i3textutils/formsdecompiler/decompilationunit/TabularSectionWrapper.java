/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.eclipse.emf.common.util.EList;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;

import com._1c.g5.v8.dt.form.model.AbstractDataPath;
import com._1c.g5.v8.dt.form.model.FormAttributeAdditionalColumns;
import com._1c.g5.v8.dt.form.model.FormAttributeColumn;

/**
 * @author ozolotarev
 *
 */
public class TabularSectionWrapper
    extends Attribute
{
    @Override
    public void decompile(StringBuilder output, DecompilationContext context)
    {
        return;
    }

    private AbstractDataPath mTablePath;

    public TabularSectionWrapper(FormAttributeAdditionalColumns set)
    {
        mTablePath = set.getTablePath();

        // Fallback для случаев без имени реквизита;
        mName = "ТабличнаяЧастьБезИмени";

        // Настройка имени ТЧ
        EList<String> segments = mTablePath.getSegments();
        if (segments.size() > 0)
            mName = segments.get(segments.size() - 1);

        for (FormAttributeColumn column : set.getColumns())
        {
            addChildren(new AttributeColumn(column, set.getTablePath()));
        }
    }

    /**
     * @return
     */
    public String getPathExpression()
    {
        String result = "";

        for (String segment : mTablePath.getSegments())
            result = result + segment + "."; //$NON-NLS-1$

        result = result.substring(0, result.length() - 1);
        return result;
    }
}
