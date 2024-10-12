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
import org.quiteoldorange.i3textutils.formsdecompiler.ui.DecompilationDialogResult;

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
    private DecompilationDialogResult mDialogResult = new DecompilationDialogResult();

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
     * @return
     */
    public String generateCode()
    {
        StringBuilder b = new StringBuilder();

        mSettings.setRegionDirectiveUsage();

        List<Attribute> attributes = mDialogResult.getSelectedAttributes();
        if (attributes.size() > 0)
        {

            b.append(mSettings.getAttributesStartSection() + "\n"); //$NON-NLS-1$

            for (DecompilationUnit item : attributes)
            {
                item.decompile(b, this);
            }

            b.append(mSettings.getAttributesEndSection());
        }

        List<FormCommandUnit> commands = mDialogResult.getSelectedCommands();

        if (commands.size() > 0)
        {
            b.append("\n");

            b.append(mSettings.getCommandsStartSection() + "\n");

            for (DecompilationUnit item : commands)
            {
                item.decompile(b, this);
            }

            b.append(mSettings.getCommandsEndSection() + "\n");
        }

        List<FormItemUnit> formItems = mDialogResult.getSelectedFormItems();

        if (formItems.size() > 0)
        {
            b.append("\n");

            b.append(mSettings.getFormItemsStartSection() + "\n");

            for (DecompilationUnit item : formItems)
            {
                item.decompile(b, this);
            }

            b.append(mSettings.getFormItemsEndSection() + "\n");
        }

        return b.toString();
    }

    /**
     * @return
     */
    public List<FormItemUnit> getFormItems()
    {
        return mFormItems;
    }

    /**
     * @return
     */
    public DecompilationDialogResult getDecompilationDialogResult()
    {
        return mDialogResult;
    }
}
