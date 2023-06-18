/**
 *
 */
package org.quiteoldorange.i3textutils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;

import com._1c.g5.v8.dt.bsl.model.StaticFeatureAccess;
import com.e1c.g5.v8.dt.bsl.check.qfix.IXtextBslModuleFixModel;
import com.e1c.g5.v8.dt.bsl.check.qfix.SingleVariantXtextBslModuleFix;
import com.e1c.g5.v8.dt.check.qfix.components.QuickFix;

/**
 * @author ozolotarev
 *
 */
@QuickFix(checkId = "form-module-missing-pragma", supplierId = "")
public class MissingPragmaQFix
    extends SingleVariantXtextBslModuleFix
{

    @Override
    protected void configureFix(FixConfigurer configurer)
    {
        configurer.interactive(true).description("Тест").details("Детали"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    protected TextEdit fixIssue(XtextResource state, IXtextBslModuleFixModel model) throws BadLocationException
    {
        EObject element = model.getElement();
        if (!(element instanceof StaticFeatureAccess))
        {
            return null;
        }
        INode node = NodeModelUtils.findActualNodeFor(element);
        return new DeleteEdit(node.getOffset(), node.getLength() + 1);
    }


}
