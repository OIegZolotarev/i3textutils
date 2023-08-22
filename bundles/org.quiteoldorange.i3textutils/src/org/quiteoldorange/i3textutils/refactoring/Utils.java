/**
 *
 */
package org.quiteoldorange.i3textutils.refactoring;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
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
import org.quiteoldorange.i3textutils.ServicesAdapter;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;

import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.ModuleType;
import com._1c.g5.v8.dt.bsl.model.RegionPreprocessor;
import com._1c.g5.v8.dt.bsl.model.util.BslUtil;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.Resources;

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

    /**
     * Получает информацию о методе модуля, с учетом документирующего комментария.
     *
     * @param method - метод модуля
     * @param doc - текущий документ
     * @return - информация о расположении метода
     * @throws BadLocationException
     */
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

        try
        {
            // Перемещаем в конец линии, чтобы явно захватить комментарий к концу процедуры
            var lineInfo = doc.getLineInformationOfOffset(endingOffset);
            endingOffset = lineInfo.getOffset() + lineInfo.getLength();
        }
        catch (BadLocationException e)
        {
            // Do nothing...
        }

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

    /**
     * @param doc
     * @return
     */
    public static Module getModuleFromXTextDocument(IXtextDocument doc)
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

    @SuppressWarnings("unchecked")
    public static <T> T getServiceInstance(Class<?> serviceClass)
    {
        var bundle = i3TextUtilsPlugin.getDefault().getBundle();
        var bundleContext = bundle.getBundleContext();

        var serviceRef = bundleContext.getServiceReference(serviceClass);
        return (T)bundleContext.getService(serviceRef);
    }

    @SuppressWarnings("nls")
    public static String getFileTemplateNameForModuleType(ModuleType type)
    {
        switch (type)
        {
        case BOT_MODULE:
            return "bot_module.bsl";
        case COMMAND_MODULE:
            return "command_module.bsl";
        case COMMON_MODULE:
            return "common_module.bsl";
        case EXTERNAL_CONN_MODULE:
            return "external_conn_module.bsl";
        case FORM_MODULE:
            return "form_module.bsl";
        case HTTP_SERVICE_MODULE:
            return "http_service_module.bsl";
        case INTEGRATION_SERVICE_MODULE:
            return "integration_service_module.bsl";
        case MANAGED_APP_MODULE:
            return "managed_app_module.bsl";
        case MANAGER_MODULE:
            return "manager_module.bsl";
        case OBJECT_MODULE:
            return "object_module.bsl";
        case ORDINARY_APP_MODULE:
            return "ordinary_app_module.bsl";
        case RECORDSET_MODULE:
            return "recordset_module.bsl";
        case SESSION_MODULE:
            return "session_module.bsl";
        case VALUE_MANAGER_MODULE:
            return "value_manager_module.bsl";
        case WEB_SERVICE_MODULE:
            return "web_service_module.bsl";
        default:
            break;

        }
        return null;
    }

    public static String getBSLModuleTemplate(ModuleType type, IProject project)
    {
        String fileName = getFileTemplateNameForModuleType(type);

        if (fileName == null)
            return null;

        String templateFile = ".settings/templates/" + fileName;
        Path p = new Path(templateFile);

        if (project.exists(p))
        {

            try
            {
                IFile templatePath = project.getFile(templateFile);
                File f = templatePath.getLocation().toFile();

                String templateSource = Files.readString(Paths.get(f.getAbsolutePath().toString()));

                return templateSource;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        // TODO: английские варианты шаблонов (чтобы по красоте все было)
        String internalPath = "bsl_templates/ru/" + fileName;
        var charSource = getFileInputSupplier(internalPath);

        return readContents(charSource, internalPath);

    }

    public static ScriptVariant getDocScriptVariant(IXtextDocument doc)
    {
        IFile file = doc.getAdapter(IFile.class);
        var project = file.getProject();

        return ServicesAdapter.instance().getProjectScriptVariant(project);
    }

    private static CharSource getFileInputSupplier(String partName)
    {
        return Resources.asCharSource(Utils.class.getResource("/resources/" + partName), //$NON-NLS-1$
            StandardCharsets.UTF_8);
    }

    private static String readContents(CharSource source, String path)
    {
        try (Reader reader = source.openBufferedStream())
        {
            return CharStreams.toString(reader);
        }
        catch (IOException | NullPointerException e)
        {
            return ""; //$NON-NLS-1$
        }
    }

}
