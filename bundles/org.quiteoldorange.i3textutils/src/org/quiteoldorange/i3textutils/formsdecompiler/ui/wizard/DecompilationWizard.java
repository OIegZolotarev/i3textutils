/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.ui.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;

/**
 * @author ozolotarev
 *
 */
public class DecompilationWizard
    extends Wizard
{

    @Override
    public IWizardPage getStartingPage()
    {
        // TODO Auto-generated method stub
        return mPageSelectedItems;
    }

    protected PageSelectItems mPageSelectedItems;
    protected PagePreviewResult mPagePreviewResult;
    private DecompilationContext mContext;

    public DecompilationWizard(DecompilationContext context)
    {
        super();
        mContext = context;



    }

    @Override
    public String getWindowTitle()
    {
        return "Декомпиляция элементов формы";
    }

    @Override
    public void addPages()
    {

        mPageSelectedItems = new PageSelectItems(mContext, this);
        addPage(mPageSelectedItems);

        mPagePreviewResult = new PagePreviewResult(mContext, this);
        addPage(mPagePreviewResult);
    }

    @Override
    public boolean performFinish()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
