/**
 *
 */
package org.quiteoldorange.i3textutils;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.xtext.resource.XtextResource;

import com.e1c.g5.v8.dt.bsl.check.qfix.IXtextBslModuleFixModel;
import com.e1c.g5.v8.dt.bsl.check.qfix.SingleVariantXtextBslModuleFix;
import com.e1c.g5.v8.dt.check.qfix.components.QuickFix;

/**
 * @author ozolotarev
 *
 */

// checkId + supplierId идентифицируют ошибку!
@QuickFix(checkId = "form-module-missing-pragma", supplierId = "com.e1c.v8codestyle.bsl")
public class MissingPragmaQFix
    extends SingleVariantXtextBslModuleFix
{

    @Override
    protected TextEdit fixIssue(XtextResource arg0, IXtextBslModuleFixModel arg1) throws BadLocationException
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    protected void configureFix(FixConfigurer configurer)
    {
        configurer.interactive(true).description("Насрал").details("Базу");
    }



}
