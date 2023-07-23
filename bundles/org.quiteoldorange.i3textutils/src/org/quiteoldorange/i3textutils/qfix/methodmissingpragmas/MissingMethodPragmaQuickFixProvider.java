/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.methodmissingpragmas;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.StringUtils;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.ui.quickfix.AbstractExternalQuickfixProvider;

/**
 * @author ozolotarev
 *
 */
public class MissingMethodPragmaQuickFixProvider
    extends AbstractExternalQuickfixProvider
{
    private static final String AddAtServer = "НаСервере"; //$NON-NLS-1$
    private static final String AddAtClient = "НаКлиенте"; //$NON-NLS-1$
    private static final String AddAtServerNoContext = "НаСервереБезКонтекста"; //$NON-NLS-1$
    private static final String AddAtServerAtClientNoContext = "НаСервереНаКлиентеБезКонтекста"; //$NON-NLS-1$

    /**
     * @author ozolotarev
     *
     */
    private final class AddMissingPragmaModification
        implements IModification
    {
        /**
         *
         */
        private final Issue mIssue;
        private FixVariant mVariant;

        /**
         * @param issue
         */
        private AddMissingPragmaModification(Issue issue, FixVariant variant)
        {
            mIssue = issue;
            mVariant = variant;
        }

        @Override
        public void apply(IModificationContext context) throws Exception
        {
            var doc = context.getXtextDocument();
            var model = Utils.getModuleFromXTextDocument(doc);

            String methodName = StringUtils.parseMethodFromURIToProblem(mIssue.getUriToProblem().toString());

            Method m = Utils.findModuleMethod(methodName, model);

            var node = NodeModelUtils.findActualNodeFor(m);

            if (!m.getPragmas().isEmpty())
                return;

            try
            {
                doc.replace(node.getOffset(), 0, String.format("%s\n", mVariant.getPragmaRU())); //$NON-NLS-1$
            }
            catch (BadLocationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Fix("DUMMY")
    public void run(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        var resolutions = FixVariant.buildResolutions(issue, acceptor);

        for (FixVariant variant : resolutions)
        {
            acceptor.accept(issue, variant.getDescription(), "", null,
                new AddMissingPragmaModification(issue, variant));
        }
    }
}
