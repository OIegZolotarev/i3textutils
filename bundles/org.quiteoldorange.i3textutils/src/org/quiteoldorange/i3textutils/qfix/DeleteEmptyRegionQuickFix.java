/**
 *
 */
package org.quiteoldorange.i3textutils.qfix;

import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;
import org.quiteoldorange.i3textutils.dialogs.GracefullErrorDialog;
import org.quiteoldorange.i3textutils.modulereformatter.ModuleReformatterContext;

import com._1c.g5.v8.dt.bsl.ui.quickfix.AbstractExternalQuickfixProvider;

/**
 * @author ozolotarev
 *
 */
public class DeleteEmptyRegionQuickFix
    extends AbstractExternalQuickfixProvider

{

    /**
     * @author ozolotarev
     *
     */
    private static class DeleteEmptyRegionModification
        implements IModification
    {

        private Issue mIssue;

        @Override
        public void apply(IModificationContext context) throws Exception
        {

            var doc = context.getXtextDocument();
            String source = doc.get();

            ModuleASTTree tree = new ModuleASTTree(source);

            String regionName = doc.get(mIssue.getOffset(), mIssue.getLength());

            AbsractBSLElementNode region = tree.findRegion(regionName);

            if (region == null)
            {
                GracefullErrorDialog.constructAndShow("Не получилось разобраться где находится область",
                    "Возможно ошибка в исходном тексте модуля");
                return;
            }

            int startingOffset = region.getStartingOffset();
            int length = region.getLength();

            // TODO: optimize
            doc.replace(startingOffset, length, ""); //$NON-NLS-1$
            doc.replace(0, doc.getLength(), ModuleReformatterContext.cleanupConsecutiveBlankLines(doc.get()));

        }

        public DeleteEmptyRegionModification(Issue issue)
        {
            mIssue = issue;
        }
    }


    public DeleteEmptyRegionQuickFix()
    {

    }

    @Fix("DUMMY")
    public void run(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        acceptor.accept(issue, "Удалить область", "", null, //$NON-NLS-2$
            new DeleteEmptyRegionModification(issue));

    }

}

