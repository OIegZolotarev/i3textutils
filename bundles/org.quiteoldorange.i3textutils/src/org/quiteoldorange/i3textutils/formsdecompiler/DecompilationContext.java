/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.Attribute;

import com._1c.g5.v8.dt.form.model.Form;
import com._1c.g5.v8.dt.form.model.FormAttribute;

/**
 * @author ozolotarev
 *
 */
public class DecompilationContext
{
    private DecompilationSettings mSettings = new DecompilationSettings();
    private Form mForm;

    private List<Attribute> mAttributes = new ArrayList<>();

    public DecompilationContext(Form form)
    {
        mForm = form;

        EList<FormAttribute> attributes = mForm.getAttributes();

        for (int i = 0; i < attributes.size(); i++)
        {
            mAttributes.add(new Attribute(attributes.get(i)));
        }

        StringBuilder test = new StringBuilder();

        for (Attribute attr : mAttributes)
        {
            attr.Decompile(test, this);
        }

        String r = test.toString();
    }

    /**
     * @return
     */
    public DecompilationSettings getDecompilationSettings()
    {
        // TODO Auto-generated method stub
        return mSettings;
    }
}
