/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;

import com._1c.g5.v8.dt.form.model.FormItem;

/**
 * @author ozolotarev
 *
 */
public class FormItemUnit
    extends DecompilationUnit
{
    protected enum ItemTypes
    {
        FormGroup,
        FormTable,
        FormField,
    };

    private String mAnchorFormItem;
    private String mParentFormItem;
    private ItemTypes mType;

    @Override
    public void decompile(StringBuilder output, DecompilationContext context)
    {
        // TODO Auto-generated method stub

    }

    FormItemUnit(ItemTypes formgroup, FormItem item)
    {
        mType = formgroup;
        mName = item.getName();
    }

    /**
     * @return the anchorFormItem
     */
    protected String getAnchorFormItem()
    {
        return mAnchorFormItem;
    }

    /**
     * @param anchorFormItem the anchorFormItem to set
     */
    protected void setAnchorFormItem(String anchorFormItem)
    {
        mAnchorFormItem = anchorFormItem;
    }

    /**
     * @return the parentFormItem
     */
    protected String getParentFormItem()
    {
        return mParentFormItem;
    }

    /**
     * @param parentFormItem the parentFormItem to set
     */
    protected void setParentFormItem(String parentFormItem)
    {
        mParentFormItem = parentFormItem;
    }

}
