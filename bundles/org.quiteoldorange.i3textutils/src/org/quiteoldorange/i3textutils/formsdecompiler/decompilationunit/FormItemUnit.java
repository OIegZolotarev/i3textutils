/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.quiteoldorange.i3textutils.Log;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationSettings;
import org.quiteoldorange.i3textutils.formsdecompiler.P;

import com._1c.g5.v8.dt.form.model.FormField;
import com._1c.g5.v8.dt.form.model.FormGroup;
import com._1c.g5.v8.dt.form.model.FormItem;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

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
        FormDecoration,
        FormButton
    };

    private String mAnchorFormItem = null;
    private String mParentFormItem = null;
    private ItemTypes mType;



    @Override
    public void decompile(StringBuilder output, DecompilationContext context)
    {
        DecompilationSettings cfg = context.getDecompilationSettings();
        boolean isRussian = cfg.scriptVariant() == ScriptVariant.RUSSIAN;

        String newItemTemplate = cfg.getNewItemTemplateName();

        String itemTypeExpression = generateItemTypeExpression();
        String itemParentExpression = generateFormItemExpression(cfg, mParentFormItem);
        String itemAnchorExpression = generateFormItemExpression(cfg, mAnchorFormItem);

        String line =
            String.format("%s = %s.%s.%s(\"%s\", %s, %s, %s);\n", newItemTemplate, cfg.getThisFormTemplateName(), //$NON-NLS-1$
                P.Items, P.Insert, mName,
            itemTypeExpression,
            itemParentExpression,
            itemAnchorExpression);

        output.append(line);

    }

    /**
     * @param cfg
     * @param anchorFormItem
     * @return
     */
    private String generateFormItemExpression(DecompilationSettings cfg, String formItem)
    {
        if (formItem == null)
            return P.Undefined;

        return P.Undefined;
    }

    /**
     * @param isRussian
     * @return
     */
    private String generateItemTypeExpression()
    {
        String itemTypeString = "";

        switch (mType)
        {
        case FormButton:
            itemTypeString = P.FormButton;
            break;
        case FormDecoration:
            itemTypeString = P.FormDecoration;
            break;
        case FormField:
            itemTypeString = P.FormField;
            break;
        case FormGroup:
            itemTypeString = P.FormGroup;
            break;
        case FormTable:
            itemTypeString = P.FormTable;
            break;
        default:
            break;

        }

        return String.format("%s(\"%s\")", P.TypeFunction, itemTypeString);
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

    public static FormItemUnit construct(FormItem item)
    {
        if (item instanceof FormGroup)
            return (new FormGroupUnit((FormGroup)item));
        else if (item instanceof FormField)
            return (new FormFieldUnit(item));

        Log.Debug("FormItemUnit.construct: не знаю как работать элементом под именем %s", item.getName());

        return null;
    }

}
