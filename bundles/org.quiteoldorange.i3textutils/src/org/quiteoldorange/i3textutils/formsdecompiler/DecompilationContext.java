/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.Attribute;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.DecompilationUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.FormCommandUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.FormItemUnit;

import com._1c.g5.v8.dt.form.model.Form;
import com._1c.g5.v8.dt.form.model.FormAttribute;
import com._1c.g5.v8.dt.form.model.FormCommand;
import com._1c.g5.v8.dt.form.model.FormItem;

/**
 * @author ozolotarev
 *
 */
public class DecompilationContext
{
    private DecompilationSettings mSettings = new DecompilationSettings();
    private Form mForm;

    private List<Attribute> mAttributes = new LinkedList<>();
    private List<FormCommandUnit> mCommands = new LinkedList<>();
    private List<FormItemUnit> mFormItems = new LinkedList<>();

    public DecompilationContext(Form form)
    {
        mForm = form;

        EList<FormAttribute> attributes = mForm.getAttributes();

        for (int i = 0; i < attributes.size(); i++)
        {
            mAttributes.add(new Attribute(attributes.get(i)));
        }

        EList<FormCommand> commands = mForm.getFormCommands();

        for (FormCommand cmd : commands)
        {
            mCommands.add(new FormCommandUnit(cmd));
        }

        EList<FormItem> formItems = mForm.getItems();

        for (FormItem item : formItems)
        {
            mFormItems.add(FormItemUnit.construct(item));
        }

    }

    /**
     * @return
     */
    public DecompilationSettings getDecompilationSettings()
    {
        // TODO Auto-generated method stub
        return mSettings;
    }

    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes()
    {
        return mAttributes;
    }

    public List<FormCommandUnit> getCommands()
    {
        return mCommands;
    }

    /**
     * @param selectedAttributes
     * @return
     */
    public String generateCode(List<Attribute> selectedAttributes)
    {
        StringBuilder builder = new StringBuilder();

        if (selectedAttributes.size() > 0)
        {

            builder.append(mSettings.getAttributesStartSection() + "\n"); //$NON-NLS-1$

            for (DecompilationUnit item : selectedAttributes)
            {
                item.decompile(builder, this);
            }

            builder.append(mSettings.getAttributesEndSection());
        }

        return builder.toString();
    }

    /**
     * @return
     */
    public List<FormItemUnit> getFormItems()
    {
        return mFormItems;
    }
}
