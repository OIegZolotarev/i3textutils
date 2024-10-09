/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import com._1c.g5.v8.dt.form.model.AbstractDataPath;
import com._1c.g5.v8.dt.form.model.FormAttributeColumn;

/**
 * @author ozolotarev
 *
 */
public class AttributeColumn
    extends Attribute
{
    private String mDataPath;

    /**
     * @param formAttribute
     * @param string
     */
    public AttributeColumn(FormAttributeColumn column, String dataPath)
    {
        mName = column.getName();
        mTitles = column.getTitle();
        mValueType = column.getValueType();

        mDataPath = dataPath;

        setKind(Kind.Column);

    }

    /**
     * @param column
     * @param tablePath
     */
    public AttributeColumn(FormAttributeColumn column, AbstractDataPath tablePath)
    {
        mName = column.getName();
        mTitles = column.getTitle();
        mValueType = column.getValueType();

        // TODO: конвертировать в формат предприятия.
        mDataPath = tablePath.toString();

        setKind(Kind.Column);
    }

}
