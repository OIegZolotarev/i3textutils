package org.quiteoldorange.i3textutils.qfix;

import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

import com._1c.g5.v8.dt.bsl.ui.quickfix.AbstractExternalQuickfixProvider;

public class QuickFixProvider
    extends AbstractExternalQuickfixProvider
{

    public QuickFixProvider()
    {
        // TODO Auto-generated constructor stub
    }

    @Fix("SU39999")
    public void Shit(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        acceptor.accept(issue, "Переместить метод в область", "", null, new IModification()
        {

            @Override
            public void apply(IModificationContext context) throws Exception
            {
            }
        });
    }

}
