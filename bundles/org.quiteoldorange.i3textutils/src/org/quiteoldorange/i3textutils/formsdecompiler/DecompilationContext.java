/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.parser.InjectionNode;
import org.quiteoldorange.i3textutils.bsl.parser.MethodNode;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.Attribute;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.DecompilationUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.FormCommandUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.FormItemUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.ui.DecompilationDialogResult;
import org.quiteoldorange.i3textutils.preferences.projectoptions.impl.formsdecompiler.FormsDecompilerOptionSet.GeneratedCodePlacementOptions;

import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.form.model.EventHandler;
import com._1c.g5.v8.dt.form.model.Form;
import com._1c.g5.v8.dt.form.model.FormAttribute;
import com._1c.g5.v8.dt.form.model.FormCommand;
import com._1c.g5.v8.dt.form.model.FormItem;
import com._1c.g5.v8.dt.form.ui.editor.FormEditor;
import com._1c.g5.v8.dt.form.ui.editor.FormEditorModulePage;
import com._1c.g5.v8.dt.mcore.Event;

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
            try
            {
                return generateCodePreviewToFormModule();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        default:
            break;

        }
        return ""; //$NON-NLS-1$
    }

    /**
     * @return
     * @throws Exception
     */
    private String generateCodePreviewToFormModule() throws Exception
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


        IXtextDocument doc = getFormModuleDocument();

        String formModuleSrc = doc.get();

        // "ПриСозданииНаСервере" помимо того что может называться по английский
        // так еще и произвольно, а может и не быть вовсе, правильно будет достать из обработчиков событий формы

        Event onCreateAtServer = findFormEvent("OnCreateAtServer"); //$NON-NLS-1$

        if (onCreateAtServer == null)
        {
            // WTF?
            return "У формы нет события \"ПриСозданииНаСервере\""; //$NON-NLS-1$
        }

        EventHandler handler = findEventHandler(onCreateAtServer);

        String onCreateAtServerSubroutine = null;

        if (handler == null)
        {
            onCreateAtServerSubroutine = defaultOnCreateAtServerSubroutine();
            // mForm.getHandlers().add(handler);
        }
        else
            onCreateAtServerSubroutine = handler.getName();

        ModuleASTTree tree = new ModuleASTTree(formModuleSrc);

        MethodNode onCreateAtServerSource = tree.findMethodDefinition(onCreateAtServerSubroutine);

        if (onCreateAtServerSource == null)
        {
            return "<Назначенный обработчик \"ПриСозданииНаСервере\" не найден в модуле формы>";
        }

        StringBuilder injectedBuilder = new StringBuilder();

        injectedBuilder.append("\n//{{I3_TEXUTILS_FORMS_DECOMPILER\n");
        injectedBuilder.append("ДобавитьЭлементыФормы();\n");
        injectedBuilder.append("//I3_TEXUTILS_FORMS_DECOMPILER}}\n");

        InjectionNode subroutineCall = new InjectionNode(injectedBuilder.toString());
        onCreateAtServerSource.addChildren(subroutineCall);

        return onCreateAtServerSource.serialize(mSettings.scriptVariant());
    }

    /**
     * @return
     */
    private IXtextDocument getFormModuleDocument()
    {
        FormEditorModulePage mp = (FormEditorModulePage)mFormEditor.findPage("editors.form.pages.module");

        if (mp == null)
        {
            // TODO: как-то внятно поругаться?
            return null;
        }

        XtextEditor editor = mp.getAdapter(XtextEditor.class);
        IXtextDocument doc = editor.getDocument();
        return doc;
    }

    private String defaultOnCreateAtServerSubroutine()
    {
        switch (mSettings.scriptVariant())
        {
        case ENGLISH:
            return "OnCreateAtServer"; //$NON-NLS-1$
        case RUSSIAN:
            return "ПриСозданииНаСервере"; //$NON-NLS-1$
        default:
            return "ПриСозданииНаСервере"; //$NON-NLS-1$
        }

    }

    private Event findFormEvent(String eventName)
    {
        for (Event ev : mForm.getFormEvents())
        {
            if (ev.getName().equals(eventName))
                return ev;
        }

        return null;
    }

    private EventHandler findEventHandler(Event event)
    {
        for (EventHandler evh : mForm.getHandlers())
        {
            if (evh.getEvent() == event)
                return evh;
        }

        return null;
    }

    private String generateCodeForManualEditing()
    {
        CodeGenerator b = new CodeGenerator(mSettings);

        generateAttributesBlock(b, mDialogResult.getSelectedAttributes());
        generateCommandsBlock(b, mDialogResult.getSelectedCommands());
        generateItemsBlock(b, mDialogResult.getSelectedFormItems());

        return b.toString();
    }

    /**
     * @param b
     * @param formItems
     */
    private void generateItemsBlock(CodeGenerator b, List<FormItemUnit> formItems)
    {
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
    }

    /**
     * @param b
     * @param commands
     */
    private void generateCommandsBlock(CodeGenerator b, List<FormCommandUnit> commands)
    {
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
    }

    /**
     * @param b
     * @param attributes
     */
    private void generateAttributesBlock(CodeGenerator b, List<Attribute> attributes)
    {
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

    /**
     *
     */
    public void onWizardDialogFinished()
    {
        GeneratedCodePlacementOptions codePlacement = mSettings.getGeneratedCodePlacement();

        switch (codePlacement)
        {
        case DoNothing:
            return;
        case ToCommonModule:
            break;
        case ToFormModule:
            decompileToFormModule();
        default:
            break;

        }

    }

    /**
     *
     */
    private void decompileToFormModule()
    {
        IXtextDocument doc = getFormModuleDocument();

        String formModuleSrc = doc.get();

        // "ПриСозданииНаСервере" помимо того что может называться по английский
        // так еще и произвольно, а может и не быть вовсе, правильно будет достать из обработчиков событий формы

        Event onCreateAtServer = findFormEvent("OnCreateAtServer"); //$NON-NLS-1$

        if (onCreateAtServer == null)
        {
            // WTF?
            return;
        }

        EventHandler handler = findEventHandler(onCreateAtServer);

        String onCreateAtServerSubroutine = null;

        if (handler == null)
        {
            onCreateAtServerSubroutine = defaultOnCreateAtServerSubroutine();
            // mForm.getHandlers().add(handler);
        }
        else
            onCreateAtServerSubroutine = handler.getName();

        ModuleASTTree tree = new ModuleASTTree(formModuleSrc);

        MethodNode onCreateAtServerSource = tree.findMethodDefinition(onCreateAtServerSubroutine);

        if (onCreateAtServerSource == null)
        {
            return;
        }

        StringBuilder injectedBuilder = new StringBuilder();

        injectedBuilder.append("\n//{{I3_TEXUTILS_FORMS_DECOMPILER\n");
        injectedBuilder.append("ДобавитьЭлементыФормы();\n");
        injectedBuilder.append("//I3_TEXUTILS_FORMS_DECOMPILER}}\n");

        InjectionNode subroutineCall = new InjectionNode(injectedBuilder.toString());
        onCreateAtServerSource.addChildren(subroutineCall);

        try
        {
            doc.replace(0, 0, tree.serialize(mSettings.scriptVariant()));
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
