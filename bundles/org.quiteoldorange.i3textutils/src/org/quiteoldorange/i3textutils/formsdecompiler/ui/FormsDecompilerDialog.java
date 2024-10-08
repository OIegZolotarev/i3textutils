/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author ozolotarev
 *
 */
public class FormsDecompilerDialog
    extends Dialog
{

    /**
     * @param parentShell
     */
    public FormsDecompilerDialog(Shell parentShell)
    {
        super(parentShell);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Декомпиляция элементов формы");

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
        mainTabs.setSize(400, 600);

        TabItem tabDecompilation = addTabPage(mainTabs, "Декомпиляция");

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

    /**
     * @param tabDecompilation
     */
    private void populateDecompilationTab(TabFolder mainTabs, TabItem tabDecompilation)
    {
        Composite area = new Composite(mainTabs, SWT.BORDER);
        tabDecompilation.setControl(area);

        // Компоненты

        Label text = new Label(area, SWT.BORDER);
        text.setText("This is page 3");

        Label text2 = new Label(area, SWT.BORDER);
        text2.setText("This is page 3");
        // text.setLayoutData(ne);

        // createTreeControl(area);

        // Настройка зоны диалога
        RowLayout layout = new RowLayout();
        layout.type = SWT.HORIZONTAL;
        area.setLayout(layout);
        area.layout();
    }

    /**
     * @param area
     */
    private void createTreeControl(Composite area)
    {
        // TreeViewer tv = new TreeViewer(area);
        final Tree tree = new Tree(area, SWT.NONE);
        tree.layout();

        for (int i = 0; i < 4; i++)
        {
            TreeItem iItem = new TreeItem(tree, 0);
            iItem.setText("TreeItem (0) -" + i);
            for (int j = 0; j < 4; j++)
            {
                TreeItem jItem = new TreeItem(iItem, 0);
                jItem.setText("TreeItem (1) -" + j);
                for (int k = 0; k < 4; k++)
                {
                    TreeItem kItem = new TreeItem(jItem, 0);
                    kItem.setText("TreeItem (2) -" + k);
                    for (int l = 0; l < 4; l++)
                    {
                        TreeItem lItem = new TreeItem(kItem, 0);
                        lItem.setText("TreeItem (3) -" + l);
                    }
                }
            }
        }
    }

    private TabItem addTabPage(TabFolder folder, String title)
    {
        TabItem tabItem = new TabItem(folder, SWT.NULL);
        tabItem.setText(title);

        return tabItem;
    }

}
