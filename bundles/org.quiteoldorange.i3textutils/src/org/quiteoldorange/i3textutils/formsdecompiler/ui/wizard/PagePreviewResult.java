/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.ui.fragments.DecompilationItemsSelector;

/**
 * @author ozolotarev
 *
 */
public class PagePreviewResult
    extends WizardPage
{

    @Override
    public void setVisible(boolean visible)
    {

        if (visible)
        {
            mWizard.updateSelectedItemsDialogResult();
            mEditor.setText(mWizard.generatePreviewSourceCode());
        }

        super.setVisible(visible);
    }

    private DecompilationContext mContext;
    private DecompilationItemsSelector mItemsSelector;
    private Composite mContainer;
    private DecompilationWizard mWizard;
    private Text mEditor;

    protected PagePreviewResult(DecompilationContext context, DecompilationWizard wizard)
    {
        super("Предварительный просмот");

        setTitle("Предварительный просмотр результата");
        setDescription("Укажите как генерировать код");
        mContext = context;
        mWizard = wizard;
    }

    @Override
    public Composite getControl()
    {
        return mContainer;
    }

    @Override
    public void createControl(Composite parent)
    {
        mContainer = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        mContainer.setLayout(layout);

//
//        BslXtextEditor editor = new BslXtextEditor();
//        editor.createPartControl(mContainer);

        mEditor = new Text(mContainer, SWT.MULTI | SWT.BORDER);

        mEditor.setText("Привет мир!");

        GridData sFillAll = new GridData(SWT.FILL, SWT.FILL, true, true);
        mEditor.setLayoutData(sFillAll);


        setControl(mContainer);

    }

}
