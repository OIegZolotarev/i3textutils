/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.quickactions;

import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.qfix.quickactions.MethodConverter.ConversionDirection;

import com._1c.g5.v8.dt.bsl.ui.quickfix.AbstractExternalQuickfixProvider;

/**
 * @author ozolotarev
 *
 */
public class MethodQuickActions
    extends AbstractExternalQuickfixProvider
{
    @Fix("MethodQuickActionsProcedure")
    public void methodQuickActionsProcedure(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        acceptor.accept(issue, "Преобразовать в функцию", "", null,
            new MethodConverter(issue, ConversionDirection.ToFunction));
    }

    @Fix("MethodQuickActionsFunction")
    public void methodQuickActionsFunctions(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        acceptor.accept(issue, "Преобразовать в процедуру", "", null,
            new MethodConverter(issue, ConversionDirection.ToProcedure));
    }
}
