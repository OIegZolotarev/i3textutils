/**
 *
 */
package org.quiteoldrange.i3textutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.ModuleType;
import com._1c.g5.v8.dt.bsl.model.RegionPreprocessor;

/**
 * @author ozolotarev
 *
 */
public class ModuleStructureFiller
    extends AbstractHandler
{

    public void fillModuleStructure(IXtextDocument doc, IProject project) throws IOException, BadLocationException

    {
        String codeMarker = "//%CURRENT_CODE%"; //$NON-NLS-1$

        Module moduleModel = ModuleRefactoringUtils.getModuleModel(doc);

        if (moduleModel == null)
        {
            return;
        }

        IFile templatePath = project.getFile(getFileTemplatePathForModuleType(moduleModel.getModuleType()));
        File f = templatePath.getLocation().toFile();

        String templateSource = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));

        RegionPreprocessor r = ModuleRefactoringUtils.findModuleRegion("Сборка_Элементов_Интерфейса", moduleModel); //$NON-NLS-1$

        if (templateSource.indexOf(codeMarker) > 0)
        {
            templateSource = templateSource.replace("//%CURRENT_CODE%", doc.get()); //$NON-NLS-1$
            doc.replace(0, doc.getLength(), templateSource);
        }
        else
        {
            doc.replace(0, 0, templateSource);
        }

    }

    private String getFileTemplatePathForModuleType(ModuleType type)
    {
        if (type == ModuleType.COMMON_MODULE)
        {
            return ".settings/templates/common_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.ORDINARY_APP_MODULE)
        {
            return ".settings/templates/ordinary_app_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.BOT_MODULE)
        {
            return ".settings/templates/bot_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.COMMAND_MODULE)
        {
            return ".settings/templates/command_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.EXTERNAL_CONN_MODULE)
        {
            return ".settings/templates/external_conn_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.FORM_MODULE)
        {
            return ".settings/templates/form_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.HTTP_SERVICE_MODULE)
        {
            return ".settings/templates/http_service_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.INTEGRATION_SERVICE_MODULE)
        {
            return ".settings/templates/integration_service_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.MANAGED_APP_MODULE)
        {
            return ".settings/templates/managed_app_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.MANAGER_MODULE)
        {
            return ".settings/templates/manager_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.OBJECT_MODULE)
        {
            return ".settings/templates/object_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.RECORDSET_MODULE)
        {
            return ".settings/templates/recordset_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.SESSION_MODULE)
        {
            return ".settings/templates/session_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.VALUE_MANAGER_MODULE)
        {
            return ".settings/templates/value_manager_module.bsl"; //$NON-NLS-1$
        }
        else if (type == ModuleType.WEB_SERVICE_MODULE)
        {
            return ".settings/templates/web_service_module.bsl"; //$NON-NLS-1$
        }

        return null;
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IProject project = ModuleRefactoringUtils.getProjectFromEvent(event);

        if (project == null)
        {
            return null;
        }

        IXtextDocument xTextDocument = ModuleRefactoringUtils.getXTextDocumentFromEvent(event);

        if (xTextDocument == null)
        {
            return null;
        }

        try
        {
            fillModuleStructure(xTextDocument, project);
        }
        catch (IOException | BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
