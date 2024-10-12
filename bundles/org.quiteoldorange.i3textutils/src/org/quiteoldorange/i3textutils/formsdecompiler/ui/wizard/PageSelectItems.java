/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.ui.DecompilationDialogResult;
import org.quiteoldorange.i3textutils.formsdecompiler.ui.fragments.DecompilationItemsSelector;

/**
 * @author ozolotarev
 *
 */
public class PageSelectItems
    extends WizardPage
{

    @Override
    public void setVisible(boolean visible)
    {

        super.setVisible(visible);
    }

    private DecompilationContext mContext;
    private DecompilationItemsSelector mItemsSelector;
    private DecompilationWizard mWizard;

    public void updateDialogResult(DecompilationDialogResult dlgResult)
    {
        mItemsSelector.updateDialogResult(dlgResult);
    }

    protected PageSelectItems(DecompilationContext context, DecompilationWizard wizard)
    {
        super("Выбор компонентов формы");

        setTitle("Выбор компонентов формы");
        setDescription("Укажите какие реквизиты, команды и элементы формы следует декомпилировать");
        mContext = context;
        mWizard = wizard;
    }

    @Override
    public Composite getControl()
    {
        return mItemsSelector;
    }

    @Override
    public void createControl(Composite parent)
    {
        mItemsSelector = new DecompilationItemsSelector(parent, mContext);


        // https://www.vogella.com/tutorials/EclipseWizards/article.html
        // "required to avoid an error in the system"
        // Какие-то костыли
        setControl(mItemsSelector);
        // setPageComplete(false);
    }

}
