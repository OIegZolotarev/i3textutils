/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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

        return dialogArea;
    }

    /**
     * @param dialogArea
     */
    private void createTabs(Composite dialogArea)
    {
        TabFolder mainTabs = new TabFolder(dialogArea, SWT.BORDER);
        mainTabs.setSize(800, 600);

        TabItem tabDecompilation = addTabPage(mainTabs, "Декомпиляция");
        Composite comp = new Composite(mainTabs, SWT.BORDER);
        comp.setSize(640, 480);
        tabDecompilation.setControl(comp);

        populateDecompilationTab(comp);

        TabItem tabSettings = addTabPage(mainTabs, "Настройки");

//            TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
//            tabItem.setText("Tab " + loopIndex);
//
//            Text text = new Text(tabFolder, SWT.BORDER);
//            text.setText("This is page ");
//            tabItem.setControl(text);

    }

    /**
     * @param tabDecompilation
     */
    private void populateDecompilationTab(Composite area)
    {
        final Tree tree = new Tree(area, SWT.BORDER);

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
