package org.quiteoldorange.i3textutils.qfix;

import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.ui.quickfix.AbstractExternalQuickfixProvider;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

public class WrapObjectModuleWithPreprocessorDefinitions
    extends AbstractExternalQuickfixProvider
{

    /**
     * @author ozolotarev
     *
     */
    private static class WrapModuleModification
        implements IModification
    {

        @Override
        public void apply(IModificationContext context) throws Exception
        {

            var doc = context.getXtextDocument();
            ScriptVariant scriptVariant = Utils.getDocScriptVariant(doc);

            String start = ""; //$NON-NLS-1$
            String end = ""; //$NON-NLS-1$

            switch (scriptVariant)
            {
            case ENGLISH:
                start = "#If Server Or ThickClientOrdinaryApplication Or ExternalConnection Then"; //$NON-NLS-1$
                end = "#EndIf"; //$NON-NLS-1$
                break;
            case RUSSIAN:
            default:
                start = "#Если Сервер Или ТолстыйКлиентОбычноеПриложение Или ВнешнееСоединение Тогда"; //$NON-NLS-1$
                end = "#КонецЕсли"; //$NON-NLS-1$
                break;

            }

            var contents = doc.get();

            doc.replace(0, contents.length(), start + "\n" + contents + "\n" + end); //$NON-NLS-1$ //$NON-NLS-2$


        }

    }

    public WrapObjectModuleWithPreprocessorDefinitions()
    {
        // TODO Auto-generated constructor stub
    }

    @Fix("DUMMY")
    public void run(final Issue issue, IssueResolutionAcceptor acceptor)
    {
        acceptor.accept(issue, "Обернуть модуль в директивы препроцессора", "", null, //$NON-NLS-2$
            new WrapModuleModification());

    }

}
