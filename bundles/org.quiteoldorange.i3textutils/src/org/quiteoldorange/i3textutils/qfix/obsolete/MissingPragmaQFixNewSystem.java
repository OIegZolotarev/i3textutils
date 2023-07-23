/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.obsolete;

import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;

import com.e1c.g5.v8.dt.bsl.check.qfix.MultiVariantXtextBslModuleFix;
import com.e1c.g5.v8.dt.check.qfix.components.QuickFix;


/**
 * @author ozolotarev
 *
 */

// checkId + supplierId идентифицируют ошибку!
/**
 * @author ozolotarev
 *
 */
@QuickFix(checkId = "form-module-missing-pragma", supplierId = i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE)
public class MissingPragmaQFixNewSystem
    extends MultiVariantXtextBslModuleFix
{

    @Override
    protected void buildVariants()
    {
        // TODO Auto-generated method stub

    }

    /*  private static final String AddAtServer = "НаСервере"; //$NON-NLS-1$
    private static final String AddAtClient = "НаКлиенте"; //$NON-NLS-1$
    private static final String AddAtServerNoContext = "НаСервереБезКонтекста"; //$NON-NLS-1$

    static FixVariant[] fixVariants = {
        new FixVariant(AddAtServer, "&НаСервере", "&AtClient"), //$NON-NLS-1$ //$NON-NLS-2$
        new FixVariant(AddAtClient, "&НаКлиенте", "&AtServer"), //$NON-NLS-1$ //$NON-NLS-2$
        new FixVariant(AddAtServerNoContext, "&НаСервереБезКонтекста", "&AtServerNoContext"), //$NON-NLS-1$ //$NON-NLS-2$
    };

    private void addPragma(SingleVariantXtextBslModuleFixContext context, IFixSession session, XtextResource state,
        IXtextInteractiveBslModuleFixModel model, FixVariant variant)
    {
        if (!(model.getElement() instanceof Method))
            return;

        Method m = (Method)model.getElement();

        var node = NodeModelUtils.findActualNodeFor(m);

        if (!m.getPragmas().isEmpty())
            return;

        var document = (IXtextDocument)model.getDocument();
        int realOffset = document.get().indexOf(node.getText().trim());

        try
        {
            document.replace(realOffset, 0, String.format("%s\n", variant.getPragmaRU())); //$NON-NLS-1$
        }
        catch (BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void buildVariants()
    {
        for (FixVariant variant : fixVariants)
        {
            VariantBuilder.create(this)
                .description(variant.getDescription(), variant.getDetails())
                .interactive(true)
                .change((context, session, state, model) -> addPragma(context, session, state,
                    (IXtextInteractiveBslModuleFixModel)model, variant))
                .build();
        }

    }


    */
}
