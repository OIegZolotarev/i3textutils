/**
 *
 */
package org.quiteoldorange.i3textutils.commands;

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
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.RegionPreprocessor;

/**
 * @author ozolotarev
 *
 */
public class FillModuleStructure
    extends AbstractHandler
{

    public void fillModuleStructure(IXtextDocument doc, IProject project) throws IOException, BadLocationException

    {
        String codeMarker = "//%CURRENT_CODE%"; //$NON-NLS-1$

        Module moduleModel = Utils.getModuleFromXTextDocument(doc);

        if (moduleModel == null)
        {
            return;
        }

        IFile templatePath = project.getFile(Utils.getFileTemplatePathForModuleType(moduleModel.getModuleType()));
        File f = templatePath.getLocation().toFile();

        String templateSource = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));

        RegionPreprocessor r = Utils.findModuleRegion("Сборка_Элементов_Интерфейса", moduleModel); //$NON-NLS-1$

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

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IProject project = Utils.getProjectFromEvent(event);

        if (project == null)
        {
            return null;
        }

        IXtextDocument xTextDocument = Utils.getXTextDocumentFromEvent(event);

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
