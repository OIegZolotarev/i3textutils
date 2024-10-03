/**
 *
 */
package org.quiteoldorange.i3textutils.qfix2023.addmethodpragma;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;

import com._1c.g5.v8.dt.bsl.model.Method;
import com.e1c.g5.v8.dt.bsl.check.qfix.IXtextInteractiveBslModuleFixModel;
import com.e1c.g5.v8.dt.bsl.check.qfix.MultiVariantXtextBslModuleFix;
import com.e1c.g5.v8.dt.bsl.check.qfix.SingleVariantXtextBslModuleFixContext;
import com.e1c.g5.v8.dt.check.qfix.IFixSession;
import com.e1c.g5.v8.dt.check.qfix.components.QuickFix;

/**
 * @author ozolotarev
 *
 */
@QuickFix(checkId = "form-module-missing-pragma", supplierId = i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE)
public class AddMethodPragmaFix
    extends MultiVariantXtextBslModuleFix
{
    static class FixVariant
    {
        String mDescription;
        String mValue;

        public FixVariant(String description, String value)
        {
            mDescription = description;
            mValue = value;
        }

        public String getDescription()
        {
            return String.format("Добавить директиву \"%s\"", mDescription);
        }

        public String getValue()
        {
            return mValue;
        }
    };

    private static final String AddAtServer = "НаСервере";
    private static final String AddAtClient = "НаКлиенте";
    private static final String AddAtServerNoContext = "НаСервереБезКонтекста";


    static FixVariant[] fixVariants = {
        new FixVariant(AddAtServer, "&НаСервере"),
        new FixVariant(AddAtClient, "&НаКлиенте"),
        new FixVariant(AddAtServerNoContext, "&НаСервереБезКонтекста"),
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
            document.replace(realOffset, 0, String.format("%s\n", variant.getValue())); //$NON-NLS-1$
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
                .description(variant.getDescription(), variant.getDescription())
                .interactive(true)
                .change((context, session, state, model) -> addPragma(context, session, state,
                    (IXtextInteractiveBslModuleFixModel)model, variant))
                .build();
        }
    }

}
