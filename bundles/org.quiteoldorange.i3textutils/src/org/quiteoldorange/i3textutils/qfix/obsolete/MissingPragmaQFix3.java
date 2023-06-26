/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.obsolete;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;

import com._1c.g5.v8.dt.bsl.model.Method;
import com.e1c.g5.v8.dt.bsl.check.qfix.IXtextBslModuleFixModel;
import com.e1c.g5.v8.dt.bsl.check.qfix.SingleVariantXtextBslModuleFix;
import com.e1c.g5.v8.dt.check.settings.CheckUid;

/**
 * @author ozolotarev
 *
 */

// checkId + supplierId идентифицируют ошибку!
//@QuickFix(checkId = "form-module-missing-pragma", supplierId = "com.e1c.v8codestyle.bsl")
public class MissingPragmaQFix3
    extends SingleVariantXtextBslModuleFix
{

    private final CheckUid checkUid;

    public static final String AddAtServer = "НаСервере"; //$NON-NLS-1$
    public static final String AddAtClient = "НаКлиенте"; //$NON-NLS-1$
    public static final String AddAtServerNoContext = "НаСервереБезКонтекста"; //$NON-NLS-1$

    private String mId;
    private String mPragmaRU;
    private String mPragmaEN;

    @Override
    protected void configureFix(FixConfigurer configurer)
    {
        configurer.interactive(true)
            .description(String.format("Добавить директиву \"%s\"", mPragmaRU)) //$NON-NLS-1$
            .details(String.format("Добавляет директиву \"%s\"", mPragmaRU)); //$NON-NLS-1$
    }

    @Override
    protected TextEdit fixIssue(XtextResource arg0, IXtextBslModuleFixModel model) throws BadLocationException
    {
        if (!(model.getElement() instanceof Method))
            return null;

        Method m = (Method)model.getElement();

        // EDT BUG: панель с ошибками конфигурации не обновляется и может несколько раз исправлять одну и ту же ошибку.
        if (!m.getPragmas().isEmpty())
            return null;

        var node = NodeModelUtils.findActualNodeFor(m);
        var document = (IXtextDocument)model.getDocument();
        int realOffset = document.get().indexOf(node.getText().trim());

        return new ReplaceEdit(realOffset, 0, String.format("%s\n", mPragmaRU)); //$NON-NLS-1$
    }

    public MissingPragmaQFix3(CheckUid _checkUid, String id, String pragmaRU, String pragmaEN)
    {

        this.checkUid = _checkUid;

        mId = id;

        mPragmaRU = pragmaRU;
        mPragmaEN = pragmaEN;
    }

    @Override
    public CheckUid getCheckId()
    {
        return checkUid;
    }
}
