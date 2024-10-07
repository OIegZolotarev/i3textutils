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
import org.quiteoldorange.i3textutils.Tuple;

import com._1c.g5.v8.bm.integration.IBmEditingContext;
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
import com._1c.g5.v8.dt.ui.IModelApiAwareSelection;

public class GroupUIItemsCommand
extends AbstractHandler
{

    private FormItem findNamedItem(Form form, String itemName)
    {
        for (var item : form.getItems())
        {
            if (item.getName().equals(itemName))
                return item;
        }

        return null;
    }

    private void moveItemsToNewLocation(List<FormItem> itemsToMove, FormItemContainer newParent,
        IBmEditingContext edContext,
        IFormItemMovementService moveService)
    {
        for (var item : itemsToMove)
        {
            MoveFormItemTask moveTask = new MoveFormItemTask(moveService, item, newParent, 0);
            edContext.execute(moveTask);
        }
    }

    Tuple<String, String> suggestNewGroupName(List<FormItem> items)
    {
        String resultName = "Группа"; //$NON-NLS-1$
        String resultTitle = "Зоголовок"; //$NON-NLS-1$

        for (var item : items)
        {
            resultName += item.getName();
        }

        return new Tuple<>(resultName, resultTitle);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {

        IWorkbenchPart part = HandlerUtil.getActivePart(event);

        if (!(part instanceof IFormEditor))
            return null;

        FormEditor formEditor = (FormEditor)part;
        Form f = formEditor.getForm();

        // Поломано?
        //IBmEditingContext editingContext = formEditor.getAdapter(IBmEditingContext.class);
        @SuppressWarnings("deprecation")
        IBmEditingContext editingContext = formEditor.getEditingContext();

        //var iV8Project = formEditor.getV8projectManager().getProject(f).getDtProject();

//        IBmModelManager bmModelManager = ServicesAdapter.instance().getBmModelManager();
//        IBmModel model = bmModelManager.getModel(iV8Project);

        var selRaw = (IModelApiAwareSelection)formEditor.getEditorInput().getSelection();
        var itemsList = selRaw.toList();


        Tuple<String, String> newGroupNames = suggestNewGroupName(itemsList);

        //DtSelectionFactory sel = (DtSelectionFactory)selRaw;
        var moveService = ServicesAdapter.instance().getFormItemMovementService();

        try
        {

            Map<String, String> titles = new HashMap<>();
            titles.put("ru", newGroupNames.getSecond()); //$NON-NLS-1$
            titles.put("en", newGroupNames.getSecond()); //$NON-NLS-1$

            AddGroupTask tsk = new AddGroupTask(f, ManagedFormGroupType.USUAL_GROUP, 0,
                new FormNewItemDescriptor(newGroupNames.getFirst(), titles, false));

            editingContext.execute(tsk);

            var parent = findNamedItem(f, newGroupNames.getFirst());
            moveItemsToNewLocation(itemsList, (FormItemContainer)parent, editingContext, moveService);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }



}
