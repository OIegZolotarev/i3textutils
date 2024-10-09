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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.Attribute;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.DecompilationUnit;

/**
 * @author ozolotarev
 *
 */
public class FormsDecompilerDialog
    extends Dialog
{

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

        for (TreeItem item : mAttributesTree.getItems())
            grabSelectedAttributes(selectedAttributes, item);

        String code = mContext.generateCode(selectedAttributes);

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

    private Tree mAttributesTree;
    private DecompilationContext mContext;

    /**
     * @param parentShell
     */
    public FormsDecompilerDialog(DecompilationContext context)
    {
        super(Display.getCurrent().getActiveShell());

        mContext = context;

    }

    /**
     * @param attribute
     * @param tree
     */
    private void addAttributeTreeeNode(Attribute attribute, TreeItem tree)
    {
        TreeItem item = new TreeItem(tree, 0);
        item.setText(attribute.getName());
        item.setData(attribute);

        for (DecompilationUnit child : attribute.getChildren())
        {
            addAttributeTreeeNode((Attribute)child, item);
        }

    }

    private TabItem addTabPage(TabFolder folder, String title)
    {
        TabItem tabItem = new TabItem(folder, SWT.NULL);
        tabItem.setText(title);

        return tabItem;
    }

    /**
     * @param area
     */
    private void createAttributesTree(Composite parent)
    {
        // TreeViewer tv = new TreeViewer(area);
        mAttributesTree = new Tree(parent, SWT.CHECK);

        mAttributesTree.setSize(320, 240);
        mAttributesTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        mAttributesTree.layout();

        for (DecompilationUnit attribute : mContext.getAttributes())
        {
            TreeItem item = new TreeItem(mAttributesTree, 0);
            item.setText(((Attribute)attribute).getName());
            item.setData(attribute);


            for (DecompilationUnit child : attribute.getChildren())
            {
                addAttributeTreeeNode((Attribute)child, item);
            }
        }
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
        mainTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        TabItem tabDecompilation = addTabPage(mainTabs, Messages.FormsDecompilerDialog_DecompilationPageTitle);

        populateDecompilationTab(mainTabs, tabDecompilation);



//        TabItem tabSettings = addTabPage(mainTabs, "Настройки");

//            TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
//            tabItem.setText("Tab " + loopIndex);
//
//            Text text = new Text(tabFolder, SWT.BORDER);
//            text.setText("This is page ");
//            tabItem.setControl(text);

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

        GridLayout oneColumn = new GridLayout(1, false);

        // Компоненты

        Composite column1 = new Composite(area, SWT.NONE);

        Text titleAttributes = new Text(column1, SWT.BORDER);
        titleAttributes.setText("Реквизиты:");

        createAttributesTree(column1);

        column1.setLayout(oneColumn);
        column1.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true));

        // createTreeControl(area);

        // Настройка зоны диалога
        GridLayout layout = new GridLayout(3, false);

        area.setLayout(layout);
        area.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

    }

}
