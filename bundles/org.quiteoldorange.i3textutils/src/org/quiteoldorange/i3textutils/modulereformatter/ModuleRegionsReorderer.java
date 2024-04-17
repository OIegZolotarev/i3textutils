/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.parser.BSLRegionNode;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class ModuleRegionsReorderer
{
    public static void ReorderRegions(IXtextDocument doc) throws BadLocationException
    {
        Module moduleModel = Utils.getModuleFromXTextDocument(doc);
        String templateSource = Utils.getBSLModuleTemplate(moduleModel.getModuleType(), null);

        ModuleASTTree templateTree = new ModuleASTTree(templateSource);
        ModuleASTTree sourceTree = new ModuleASTTree(doc.get());

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

        doc.replace(0, doc.getLength(), sourceTree.serialize(ScriptVariant.RUSSIAN));
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
