/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.Attribute;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.DecompilationUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.FormCommandUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.FormItemUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.ui.DecompilationDialogResult;
import org.quiteoldorange.i3textutils.preferences.projectoptions.impl.formsdecompiler.FormsDecompilerOptionSet.GeneratedCodePlacementOptions;

import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.form.model.Form;
import com._1c.g5.v8.dt.form.model.FormAttribute;
import com._1c.g5.v8.dt.form.model.FormCommand;
import com._1c.g5.v8.dt.form.model.FormItem;
import com._1c.g5.v8.dt.form.ui.editor.FormEditor;

/**
 * @author ozolotarev
 *
 */
public class DecompilationContext
{
    private DecompilationSettings mSettings = null;
    private Form mForm;

    private List<Attribute> mAttributes = new LinkedList<>();
    private List<FormCommandUnit> mCommands = new LinkedList<>();
    private List<FormItemUnit> mFormItems = new LinkedList<>();
    private DecompilationDialogResult mDialogResult = new DecompilationDialogResult();
    private IV8Project mV8Project;
    private FormEditor mFormEditor;

    public DecompilationContext(FormEditor formEditor)
    {
        mFormEditor = formEditor;
        mForm = formEditor.getForm();
        IV8Project v8Project = formEditor.getV8projectManager().getProject(mForm.bmGetEngine().getId());

        mV8Project = v8Project;
        mSettings = new DecompilationSettings(v8Project);


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

        String nextItem = null;
        String parentItem = null;

        for (int index = 0; index < formItems.size(); index++)
        {
            FormItem item = formItems.get(index);

            if (index < (formItems.size() - 1))
                nextItem = formItems.get(index + 1).getName();
            else
                nextItem = null;

            mFormItems.add(FormItemUnit.construct(item, nextItem, parentItem));
            nextItem = item.getName();
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
    public String generateCodePreview()
    {
        GeneratedCodePlacementOptions codePlacement = mSettings.getGeneratedCodePlacement();

        switch (codePlacement)
        {
        case DoNothing:
            return generateCodeForManualEditing();
        case ToCommonModule:
            break;
        case ToFormModule:
            return generateCodeToFormModule();
        default:
            break;

        }
        return "";


    }

    /**
     * @return
     */
    private String generateCodeToFormModule()
    {

        // Шайтан-код чтобы достать модуль формы из редактора.
        //        FormEditorModulePage mp = (FormEditorModulePage)mFormEditor.findPage("editors.form.pages.module");
        //        XtextEditor editor = mp.getAdapter(XtextEditor.class);
        //
        //        String formModuleSrc = editor.getDocument().get();

        // 1) Достать текст "ПриСозданииНаСервере"
        // 2) Вставить туда вызов генерации формы
        // 3) Вставить следом процедуру генерации формы


        // 4) При внесении изменении обновить "ПриСозданииНаСервере" и добавить процедуру генерации
        // 5) (Опционально) удалить элементы?

        return "";
    }

    private String generateCodeForManualEditing()
    {
        CodeGenerator b = new CodeGenerator(mSettings);

        // Реквизиты

        List<Attribute> attributes = mDialogResult.getSelectedAttributes();
        if (attributes.size() > 0)
        {

            b.append(mSettings.getAttributesStartSection() + "\n"); //$NON-NLS-1$

            b.generateNewElementsPrologue();

            for (DecompilationUnit item : attributes)
            {
                item.decompile(b);
                b.append("\n");
            }

            b.generateNewElementsEpilogue();

            b.append("\n" + mSettings.getAttributesEndSection());
        }

        // Команды

        List<FormCommandUnit> commands = mDialogResult.getSelectedCommands();

        if (commands.size() > 0)
        {
            b.append("\n");

            b.append(mSettings.getCommandsStartSection() + "\n");

            for (DecompilationUnit item : commands)
            {
                item.decompile(b);
                b.append("\n");
            }

            b.append("\n" + mSettings.getCommandsEndSection() + "\n");
        }

        // Элементы формы

        List<FormItemUnit> formItems = mDialogResult.getSelectedFormItems();

        if (formItems.size() > 0)
        {
            b.append("\n");

            b.append(mSettings.getFormItemsStartSection() + "\n");

            for (DecompilationUnit item : formItems)
            {
                item.decompile(b);
                b.append("\n");
            }

            b.append("\n" + mSettings.getFormItemsEndSection() + "\n");
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

    /**
     * @return
     */
    public IProject getProject()
    {
        if (mV8Project != null)
            return mV8Project.getProject();
        else
            return null;
    }
}
