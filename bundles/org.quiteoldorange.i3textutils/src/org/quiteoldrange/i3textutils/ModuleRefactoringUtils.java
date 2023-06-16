/**
 *
 */
package org.quiteoldrange.i3textutils;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.RegionPreprocessor;
import com._1c.g5.v8.dt.bsl.model.util.BslUtil;

/**
 * @author ozolotarev
 *
 */
public class ModuleRefactoringUtils
{
    /**
     * Ищет область модуля по имени
     * @param name - Имя метода
     * @param model - модель модуля
     * @return - найденная область
     */
    public static RegionPreprocessor findModuleRegion(String name, Module model)
    {

        List<RegionPreprocessor> items = BslUtil.getAllRegionPreprocessors(model);

        for (RegionPreprocessor region : items)
        {
            if (region.getName().equals(name))
            {
                return region;
            }
        }

        return null;

    }

    /**
     * Ищет метод модуля по имени
     * @param name - Имя метода
     * @param model - модель модуля
     * @return - найденная область
     */
    public static Method findModuleMethod(String name, Module model)
    {

        List<Method> items = model.allMethods();

        for (Method method : items)
        {
            if (method.getName().equals(name))
            {
                return method;
            }
        }

        return null;

    }

    /**
     * Получает документ проекта из редактора
     * @param event - событие полученное плагином
     * @return документ с которым может работать документ
     */
    public static IXtextDocument getXTextDocumentFromEvent(ExecutionEvent event)
    {
        //
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        XtextEditor target = part.getAdapter(XtextEditor.class);

        if (target != null)
        {
            if (!(target.getEditorInput() instanceof IFileEditorInput))
            {
                return null;
            }

            IFileEditorInput input = (IFileEditorInput)target.getEditorInput();
            IFile file = input.getFile();

            if (file == null)
            {
                return null;
            }

            IProject project = file.getProject();
            if (project == null)
            {
                return null;
            }

            IXtextDocument doc = target.getDocument();
            return doc;
        }

        return null;

    }

    /**
     * Получает проект из редактора
     * @param event - событие полученное плагином
     * @return проект
     */
    public static IProject getProjectFromEvent(ExecutionEvent event)
    {
        //
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        XtextEditor target = part.getAdapter(XtextEditor.class);

        if (target != null)
        {
            if (!(target.getEditorInput() instanceof IFileEditorInput))
            {
                return null;
            }

            IFileEditorInput input = (IFileEditorInput)target.getEditorInput();
            IFile file = input.getFile();

            if (file == null)
            {
                return null;
            }

            IProject project = file.getProject();

            return project;
        }

        return null;

    }

    public static Module getModuleModel(IXtextDocument doc)
    {
        return (Module)doc.readOnly(new IUnitOfWork<EObject, XtextResource>()
        {
            @Override
            public EObject exec(XtextResource res) throws Exception
            {
                //сперва проверяем, доступность семантической модели встроенного языка
                if (res.getContents() != null && !res.getContents().isEmpty())
                {
                    EObject obj = res.getContents().get(0);
                    if (obj instanceof Module) // проверили, что работаем с правильным объектом семантической модели
                    {
                        Module module = (Module)obj;
                        return module;
                    }
                }
                return null;
            }
        });
    }

}