/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import com._1c.g5.v8.dt.form.model.AbstractDataPath;
import com._1c.g5.v8.dt.form.model.FormAttribute;
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
    public AttributeColumn(FormAttributeColumn formAttribute, String dataPath)
    {
        super((FormAttribute)formAttribute);

        mDataPath = dataPath;

    }

    /**
     * @param column
     * @param tablePath
     */
    public AttributeColumn(FormAttributeColumn column, AbstractDataPath tablePath)
    {
        super((FormAttribute)column);
    }

}
