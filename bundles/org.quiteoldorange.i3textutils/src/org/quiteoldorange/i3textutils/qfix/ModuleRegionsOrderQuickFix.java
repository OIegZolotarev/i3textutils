/**
 *
 */
package org.quiteoldorange.i3textutils.qfix;

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

        }

    }

    @Fix("DUMMY")
    public void run(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        acceptor.accept(issue, "Упорядочить области модуля", "", null, //$NON-NLS-2$
            new ReorderModuleRegionsModification());

    }

}
