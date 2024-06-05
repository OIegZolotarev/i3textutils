/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.Log;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.parser.BSLRegionNode;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class ModuleReformatter
{
    IXtextDocument mDoc;
    IProject mProject;
    Module mModule;



    boolean mCleanupEmptyLines = false;
    boolean mReorderRegions = false;
    boolean mAddAtServerPragma = false;
    boolean mRemoveEmptyRegions = false;

    public void scheduleTask(ReformatterTask task, boolean perform)
    {
        switch (task)
        {
        case AddAtServerPragam:
            mAddAtServerPragma = perform;
            break;
        case RemoveEmptyRegions:
            mRemoveEmptyRegions = perform;
            break;
        case CleanupEmptyLines:
            mCleanupEmptyLines = perform;
            break;
        case ReorderRegions:
            mReorderRegions = perform;
            break;
        default:
            break;
        }
    }

    /**
     *
     */
    public void scheduleAll()
    {
        mCleanupEmptyLines = true;
        mReorderRegions = true;
        mAddAtServerPragma = true;
        mRemoveEmptyRegions = true;
    }

    public ModuleReformatter(IProject project, IXtextDocument doc)
    {
        mDoc = doc;
        mModule = Utils.getModuleFromXTextDocument(doc);
        mProject = project;
    }

    public static ModuleReformatter construct(ExecutionEvent event)
    {
        IXtextDocument doc = Utils.getXTextDocumentFromEvent(event);

        if (doc == null)
            return null;

        var project = Utils.getProjectFromEvent(event);

        if (project == null)
            return null;

        return new ModuleReformatter(project, doc);
    }

    public void run()
    {
        String originalSource = mDoc.get();

        ModuleASTTree sourceTree = new ModuleASTTree(originalSource);

        // Fallback когда не смогли распарсить дерево,
        // чтобы не очищать полностью документ
        if (sourceTree.isFailedToParse())
        {
            Log.Debug("Произошла ошибка при разборе модуля: %s", sourceTree.getParsingError());

            if (mCleanupEmptyLines)
            {
                String cleanedUpSource = cleanupConsecutiveBlankLines(originalSource);
                applyChanges(cleanedUpSource);
            }
            return;
        }

        if (mReorderRegions)
            reorderRegions(sourceTree);

        // TODO: fix ScriptVariant hardcoded
        String source;
        try
        {
            source = sourceTree.serialize(ScriptVariant.RUSSIAN);
        }
        catch (Exception e)
        {
            source = originalSource;
        }

        if (mCleanupEmptyLines)
            source = cleanupConsecutiveBlankLines(source);

        applyChanges(source);
    }

    private void applyChanges(String source)
    {
        try
        {
            mDoc.replace(0, mDoc.getLength(), source);
        }
        catch (BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String cleanupConsecutiveBlankLines(String source)
    {
        String lines[] = source.split("\n"); //$NON-NLS-1$

        StringBuilder builder = new StringBuilder();

        boolean hasEmptyLine = false;

        for (var line : lines)
        {
            if (line.isBlank() && hasEmptyLine)
                continue;
            else if (line.isBlank() && !hasEmptyLine)
            {
                builder.append("\n"); //$NON-NLS-1$
                hasEmptyLine = true;
            }
            else
            {
                hasEmptyLine = false;
                builder.append(line + "\n");
            }
        }

        return builder.toString();
    }

    public void reorderRegions(ModuleASTTree sourceTree)
    {
        String templateSource = Utils.getBSLModuleTemplate(mModule.getModuleType(), null);

        ModuleASTTree templateTree = new ModuleASTTree(templateSource);

        // Parse source tree if not parsed yet
        if (sourceTree == null)
            sourceTree = new ModuleASTTree(mDoc.get());

        List<BSLRegionNode> topRegions = sourceTree.dumpTopRegions();
        List<BSLRegionNode> topRegionsTemplate = templateTree.dumpTopRegions();

        mapIdealOrder(topRegions, topRegionsTemplate);

        for (int i = topRegions.size() - 1; i >= 0; i--)
        {
            for (int j = 0; j < i; j++)
            {
                BSLRegionNode a = topRegions.get(i);
                BSLRegionNode b = topRegions.get(j);

                // TODO: обработка областей с порядком -1 - нестандартных для модуля.

                if (b.getIdealOrder() > a.getIdealOrder())
                {
                    Collections.swap(topRegions, i, j);
                    sourceTree.swapNodes(a, b);
                }
            }
        }

    }

    private static int getRegionOrder(List<BSLRegionNode> idealRegions, String regionName)
    {
        int index = 0;

        for (BSLRegionNode region : idealRegions)
        {
            if (region.getName().equals(regionName))
            {
                return index;
            }

            index++;
        }

        return -1;
    }

    private static void mapIdealOrder(List<BSLRegionNode> currentRegions, List<BSLRegionNode> idealRegions)
    {
        for (BSLRegionNode region : currentRegions)
        {
            int regionOrder = getRegionOrder(idealRegions, region.getName());
            region.setIdealOrder(regionOrder);
        }
    }


}
