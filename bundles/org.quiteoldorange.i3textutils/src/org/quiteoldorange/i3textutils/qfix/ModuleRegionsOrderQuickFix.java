/**
 *
 */
package org.quiteoldorange.i3textutils.qfix;

import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.modulereformatter.ModuleRegionsReorderer;

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
            ModuleRegionsReorderer.reorderRegions(doc);
        }

    }

    @Fix("DUMMY")
    public void run(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        acceptor.accept(issue, "Упорядочить области модуля", "", null, //$NON-NLS-2$
            new ReorderModuleRegionsModification());

    }

}
