/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.ui;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TreeItem;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.Attribute;
import org.quiteoldorange.i3textutils.formsdecompiler.ui.fragments.DecompilationItemsSelector;

/**
 * @author ozolotarev
 *
 */
public class FormsDecompilerDialog
    extends Dialog
{

    private final static GridData sFillAll = new GridData(SWT.FILL, SWT.FILL, true, true);


    @Override
    protected void cancelPressed()
    {
        // TODO Auto-generated method stub
        super.cancelPressed();
    }

    @Override
    protected void okPressed()
    {

        List<Attribute> selectedAttributes = new LinkedList<>();

//        for (TreeItem item : mAttributesTree.getItems())
//            grabSelectedAttributes(selectedAttributes, item);
//
//        String code = mContext.generateCode(selectedAttributes);

        super.okPressed();
    }

    /**
     * @param selectedAttributes
     * @param attributesTree
     */
    private void grabSelectedAttributes(List<Attribute> selectedAttributes, TreeItem attributesTree)
    {
        for (TreeItem item : attributesTree.getItems())
        {
            if (item.getChecked())
                selectedAttributes.add((Attribute)item.getData());

            grabSelectedAttributes(selectedAttributes, item);
        }
    }

    private DecompilationContext mContext;
    private DecompilationItemsSelector mItemsSelector;

    /**
     * @param parentShell
     */
    public FormsDecompilerDialog(DecompilationContext context)
    {
        super(Display.getCurrent().getActiveShell());

        mContext = context;

    }


    private TabItem addTabPage(TabFolder folder, String title)
    {
        TabItem tabItem = new TabItem(folder, SWT.NULL);
        tabItem.setText(title);

        return tabItem;
    }



    @Override
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText(Messages.FormsDecompilerDialog_DialogTitle);
        Composite dialogArea = (Composite)super.createDialogArea(parent);

        createTabs(dialogArea);

        dialogArea.setSize(800, 600);
        dialogArea.setLayout(new GridLayout(1, false));
        dialogArea.layout();




        return dialogArea;
    }

    /**
     * @param dialogArea
     */
    private void createTabs(Composite dialogArea)
    {
        TabFolder mainTabs = new TabFolder(dialogArea, SWT.BORDER);
        mainTabs.setLayoutData(sFillAll);

        TabItem tabDecompilation = addTabPage(mainTabs, Messages.FormsDecompilerDialog_DecompilationPageTitle);

        populateDecompilationTab(mainTabs, tabDecompilation);


        mainTabs.layout();


    }

    @Override
    protected Point getInitialSize()
    {
        final Point size = super.getInitialSize();

        int charsWidth = 150;
        int charsHeight = 30;

        size.x = convertWidthInCharsToPixels(charsWidth);
        size.y = convertHeightInCharsToPixels(charsHeight);

        return size;
    }

    @Override
    protected boolean isResizable()
    {
        return true;
    }

    /**
     * @param tabDecompilation
     */
    private void populateDecompilationTab(TabFolder mainTabs, TabItem tabDecompilation)
    {
        Composite area = new Composite(mainTabs, SWT.BORDER);
        tabDecompilation.setControl(area);

        mItemsSelector = new DecompilationItemsSelector(area, mContext);

    }



}
