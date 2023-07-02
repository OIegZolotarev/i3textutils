/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.obsolete;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.xtext.resource.XtextResource;

import com.e1c.g5.v8.dt.bsl.check.qfix.IXtextBslModuleFixModel;
import com.e1c.g5.v8.dt.bsl.check.qfix.SingleVariantXtextBslModuleFix;

/**
 * @author ozolotarev
 *
 */
//@QuickFix(checkId = "form-module-missing-pragma", supplierId = "com.e1c.v8codestyle.bsl")
public class MissingPragmaQFix2
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
        //configurer.interactive(true).description("Насрал 2").details("MissingPragmaQFix2"); //$NON-NLS-1$ //$NON-NLS-2$
    }



}
