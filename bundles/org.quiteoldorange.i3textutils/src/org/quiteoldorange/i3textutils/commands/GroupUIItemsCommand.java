package org.quiteoldorange.i3textutils.commands;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.quiteoldorange.i3textutils.ServicesAdapter;

import com._1c.g5.v8.bm.integration.IBmModel;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.form.model.Form;
import com._1c.g5.v8.dt.form.model.FormItem;
import com._1c.g5.v8.dt.form.model.FormItemContainer;
import com._1c.g5.v8.dt.form.model.ManagedFormGroupType;
import com._1c.g5.v8.dt.form.service.item.FormNewItemDescriptor;
import com._1c.g5.v8.dt.form.service.item.IFormItemMovementService;
import com._1c.g5.v8.dt.form.service.item.task.AddGroupTask;
import com._1c.g5.v8.dt.form.service.item.task.MoveFormItemTask;
import com._1c.g5.v8.dt.form.ui.editor.FormEditor;
import com._1c.g5.v8.dt.form.ui.editor.IFormEditor;

public class GroupUIItemsCommand
extends AbstractHandler
{

    public FormItem FindNamedItem(Form form, String itemName)
    {
        for (var item : form.getItems())
        {
            if (item.getName().equals(itemName))
                return item;
        }

        return null;
    }

    private void moveItemsToNewLocation(List<FormItem> itemsToMove, FormItemContainer newParent, IBmModel model,
        IFormItemMovementService moveService)
    {
        for (var item : itemsToMove)
        {
            MoveFormItemTask moveTask = new MoveFormItemTask(moveService, item, newParent, 0);
            model.execute(moveTask);
        }
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {

        IWorkbenchPart part = HandlerUtil.getActivePart(event);

        if (!(part instanceof IFormEditor))
            return null;

        FormEditor formEditor = (FormEditor)part;
        Form f = formEditor.getForm();

        var iV8Project = formEditor.getV8projectManager().getProject(f).getDtProject();

        IBmModelManager bmModelManager = ServicesAdapter.instance().getBmModelManager();
        IBmModel model = bmModelManager.getModel(iV8Project);

        //var selRaw = (IModelApiAwareSelection)formEditor.getEditorInput().getSelection();
        //var itemsList = selRaw.toList();
        //DtSelectionFactory sel = (DtSelectionFactory)selRaw;
        //var moveService = ServicesAdapter.instance().getFormItemMovementService();

        try
        {

            Map<String, String> titles = new HashMap<>();
            titles.put("ru", "Добрый день");
            titles.put("en", "Hello world");

            AddGroupTask tsk = new AddGroupTask(f, ManagedFormGroupType.USUAL_GROUP, 0,
                new FormNewItemDescriptor("ДобрыйДень", titles, false));
            model.execute(tsk);

            //var parent = f.getItems().get(0);
            //parent.getName()


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }



}
