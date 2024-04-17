/**
 *
 */
package org.quiteoldorange.i3textutils.qfix;

import java.util.Collections;
import java.util.List;

import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.parser.BSLRegionNode;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.ui.quickfix.AbstractExternalQuickfixProvider;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class ModuleRegionsOrderQuickFix
    extends AbstractExternalQuickfixProvider
{
    /**
     * @author ozolotarev
     *
     */
    private static class ReorderModuleRegionsModification
        implements IModification
    {

        @Override
        public void apply(IModificationContext context) throws Exception
        {
            IXtextDocument doc = context.getXtextDocument();
            Module moduleModel = Utils.getModuleFromXTextDocument(doc);

            if (moduleModel == null)
            {
                return;
            }

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

                    if (b.getIdealOrder() > a.getIdealOrder())
                    {
                        Collections.swap(topRegions, i, j);
                        sourceTree.swapNodes(a, b);
                    }
                }
            }

            doc.replace(0, doc.getLength(), sourceTree.serialize(ScriptVariant.RUSSIAN));

        }

        private int getRegionOrder(List<BSLRegionNode> idealRegions, String regionName)
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

        private void mapIdealOrder(List<BSLRegionNode> currentRegions, List<BSLRegionNode> idealRegions)
        {
            for (BSLRegionNode region : currentRegions)
            {
                int regionOrder = getRegionOrder(idealRegions, region.getName());
                region.setIdealOrder(regionOrder);
            }
        }

    }

    @Fix("DUMMY")
    public void run(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        acceptor.accept(issue, "Упорядочить области модуля", "", null, //$NON-NLS-2$
            new ReorderModuleRegionsModification());

    }

}
