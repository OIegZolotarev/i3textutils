/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.quiteoldorange.i3textutils.Log;
import org.quiteoldorange.i3textutils.formsdecompiler.CodeGenerator;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationSettings;
import org.quiteoldorange.i3textutils.formsdecompiler.P;

import com._1c.g5.v8.dt.form.model.Button;
import com._1c.g5.v8.dt.form.model.FormField;
import com._1c.g5.v8.dt.form.model.FormGroup;
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
        FormDecoration,
        FormButton
    };

    private String mAnchorFormItem = null;
    private String mParentFormItem = null;
    private ItemTypes mType;



    @Override
    public void decompile(CodeGenerator b)
    {
        DecompilationSettings cfg = b.getDecompilationSettings();
        String newItemTemplate = cfg.getNewItemTemplateName();

        b.beginNewFormItem();

        String itemTypeExpression = generateItemTypeExpression();
        String itemParentExpression = generateFormItemExpression(cfg, mParentFormItem);
        String itemAnchorExpression = generateFormItemExpression(cfg, mAnchorFormItem);

        String line =
            String.format("%s = %s.%s.%s(\"%s\", %s, %s, %s);\n", newItemTemplate, cfg.getThisFormTemplateName(), //$NON-NLS-1$
                P.Items, P.Insert, mName,
            itemTypeExpression,
            itemParentExpression,
            itemAnchorExpression);

        b.append(line);

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

        String thisFormExpr = cfg.getThisFormTemplateName();

        switch (cfg.scriptVariant())
        {
        case ENGLISH:
            return String.format("%s.Items[\"%s\"]", thisFormExpr, formItem); //$NON-NLS-1$
        default:
            return String.format("%s.Элементы[\"%s\"]", thisFormExpr, formItem); //$NON-NLS-1$
        }
    }

    /**
     * @param isRussian
     * @return
     */
    private String generateItemTypeExpression()
    {
        String itemTypeString = ""; //$NON-NLS-1$

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

        return String.format("%s(\"%s\")", P.TypeFunction, itemTypeString); //$NON-NLS-1$
    }

    FormItemUnit(ItemTypes type, FormItem item)
    {
        mType = type;
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

    public static FormItemUnit construct(FormItem item, String nextItem, String parentItem)
    {


        FormItemUnit newItem = null;

        if (item instanceof FormGroup)
            newItem = (new FormGroupUnit((FormGroup)item));
        else if (item instanceof FormField)
            newItem = (new FormFieldUnit((FormField)item));
        else if (item instanceof Button)
            newItem = (new FormButtonUnit((Button)item));
        else
            Log.Debug("FormItemUnit.construct: не знаю как работать элементом под именем %s", item.getName()); //$NON-NLS-1$

        if (newItem != null)
        {
            newItem.mAnchorFormItem = nextItem;
            newItem.mParentFormItem = parentItem;
        }

        return newItem;
    }

}
