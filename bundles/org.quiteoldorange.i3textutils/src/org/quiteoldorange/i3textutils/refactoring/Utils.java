/**
 *
 */
package org.quiteoldorange.i3textutils.refactoring;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
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
public class Utils
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

    public static MethodSourceInfo getMethodSourceInfo(Method method, IXtextDocument doc) throws BadLocationException
    {
        var node = NodeModelUtils.findActualNodeFor(method);
        int startingLine = 0;

        startingLine = doc.getLineOfOffset(node.getOffset());

        if (startingLine == 0)
        {
            return new MethodSourceInfo(node.getOffset(), node.getEndOffset(), node.getText());
        }

        int endingLine = startingLine;

        while(true)
        {
            if (endingLine == 0)
                break;

            int newOffset = doc.getLineOffset(endingLine - 1);

            if (doc.get(newOffset, 2).equals("//")) //$NON-NLS-1$
            {
                endingLine--;
            }
            else
                break;
        }

        int startingOffset = doc.getLineOffset(endingLine);
        int endingOffset = node.getEndOffset();

        String sourceText = doc.get(startingOffset, endingOffset - startingOffset);

        return new MethodSourceInfo(startingOffset, endingOffset, sourceText);
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
