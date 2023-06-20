/**
 *
 */
package org.quiteoldorange.i3textutils.qfix;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;

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

// checkId + supplierId идентифицируют ошибку!
@QuickFix(checkId = "form-module-missing-pragma", supplierId = "com.e1c.v8codestyle.bsl")
public class MissingPragmaQFix
    extends MultiVariantXtextBslModuleFix
{

    private static final String AddAtServer = "НаСервере"; //$NON-NLS-1$
    private static final String AddAtClient = "НаКлиенте"; //$NON-NLS-1$
    private static final String AddAtServerNoContext = "НаСервереБезКонтекста"; //$NON-NLS-1$

    static private class FixVariant
    {
        private String mId;
        private String mPragmaRU;
        private String mPragmaEN;

        /**
         *
         */
        public FixVariant(String id, String pragmaRU, String pragmaEN)
        {
            mId = id;

            mPragmaRU = pragmaRU;
            mPragmaEN = pragmaEN;
        }

        /**
         * @return the description
         */
        public String getDescription()
        {
            return String.format("Добавить директиву \"%s\"", mPragmaRU); //$NON-NLS-1$
        }
        /**
         * @return the id
         */
        public String getId()
        {
            return mId;
        }
        /**
         * @return the pragmaRU
         */
        public String getPragmaRU()
        {
            return mPragmaRU;
        }
        /**
         * @return the pragmaEN
         */
        public String getPragmaEN()
        {
            return mPragmaEN;
        }

        /**
         * @return
         */
        public String getDetails()
        {
            return "<Детали>"; //$NON-NLS-1$
        }
    }

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

        // Улетают оффсеты напрочь, надо посмотреть MultiTextEdit


        IXtextDocument doc = (IXtextDocument)model.getDocument();
        try
        {

            doc.replace(node.getOffset(), 0, String.format("%s\n", variant.getPragmaRU())); //$NON-NLS-1$
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
        return;
/*        for (FixVariant variant : fixVariants)
{
    VariantBuilder.create(this)
        .description(variant.getDescription(), variant.getDetails())
        .interactive(true)
    .change((context, session, state, model) -> addPragma(context, session, state,
            (IXtextInteractiveBslModuleFixModel)model, variant))
        .build();
}
*/


    }



}
