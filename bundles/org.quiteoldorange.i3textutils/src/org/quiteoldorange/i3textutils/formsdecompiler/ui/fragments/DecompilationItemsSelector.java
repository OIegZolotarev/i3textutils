/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.ui.fragments;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.DecompilationUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.FormCommandUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.FormItemUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.ui.DecompilationDialogResult;

/**
 * @author ozolotarev
 *
 */
public class DecompilationItemsSelector
    extends Composite
{

    private final static GridData sFillAll = new GridData(SWT.FILL, SWT.FILL, true, true);
    private final static GridLayout sOneColumn = new GridLayout(1, false);
    private Tree mAttributesTree;

    private Tree mCommandsList;
    private DecompilationContext mContext;

    private Tree mFormItemsTree;

    public DecompilationItemsSelector(Composite parent, DecompilationContext context)
    {
        super(parent, SWT.NONE);

        mContext = context;

        // Компоненты

        // Первая колонка

        Composite column1 = new Composite(this, SWT.NONE);

        Label titleAttributes = new Label(column1, SWT.BORDER);
        titleAttributes.setText("Реквизиты:");

        createAttributesTree(column1);

        column1.setLayout(sOneColumn);
        column1.setLayoutData(sFillAll);

        // Вторая колонка

        Composite column2 = new Composite(this, SWT.NONE);

        Label titleCommands = new Label(column2, SWT.BORDER);
        titleCommands.setText("Команды:");

        createCommandsList(column2);

        column2.setLayout(sOneColumn);
        column2.setLayoutData(sFillAll);

        // Третья колонка

        Composite column3 = new Composite(this, SWT.NONE);

        Label formItems = new Label(column3, SWT.BORDER);
        formItems.setText("Элементы:");

        createFormItemsTree(column3);

        column3.setLayout(sOneColumn);
        column3.setLayoutData(sFillAll);

        // Настройка зоны диалога
        GridLayout layout = new GridLayout(3, false);

        setLayout(layout);
        setLayoutData(sFillAll);
    }

    /**
     * @param attribute
     * @param tree
     */
    private void addAttributeTreeNode(DecompilationUnit attribute, TreeItem tree)
    {
        TreeItem item = new TreeItem(tree, 0);
        item.setText(attribute.getName());
        item.setData(attribute);

        for (DecompilationUnit child : attribute.getChildren())
        {
            addAttributeTreeNode(child, item);
        }

    }

    /**
     * @param child
     * @param item
     */
    private void addFormItemTreeNode(DecompilationUnit node, TreeItem tree)
    {
        TreeItem item = new TreeItem(tree, 0);
        item.setText(node.getName());
        item.setData(node);

        for (DecompilationUnit child : node.getChildren())
        {
            addFormItemTreeNode(child, item);
        }
    }

    /**
     * @param area
     */
    private void createAttributesTree(Composite parent)
    {
        // TreeViewer tv = new TreeViewer(area);
        mAttributesTree = new Tree(parent, SWT.CHECK);
        mAttributesTree.setLayoutData(sFillAll);
        mAttributesTree.layout();

        for (DecompilationUnit attribute : mContext.getAttributes())
        {
            TreeItem item = new TreeItem(mAttributesTree, 0);
            item.setText(attribute.getName());
            item.setData(attribute);

            for (DecompilationUnit child : attribute.getChildren())
            {
                addAttributeTreeNode(child, item);
            }
        }
    }

    private void createCommandsList(Composite parent)
    {
        mCommandsList = new Tree(parent, SWT.CHECK);
        mCommandsList.setLayoutData(sFillAll);
        mCommandsList.layout();

        for (FormCommandUnit cmdUnit : mContext.getCommands())
        {
            TreeItem item = new TreeItem(mCommandsList, 0);
            item.setText(cmdUnit.getName());
            item.setData(cmdUnit);
        }
    }

    /**
     * @param column3
     */
    private void createFormItemsTree(Composite parent)
    {
        // TreeViewer tv = new TreeViewer(area);
        mFormItemsTree = new Tree(parent, SWT.CHECK);
        mFormItemsTree.setLayoutData(sFillAll);
        mFormItemsTree.layout();

        for (FormItemUnit formItem : mContext.getFormItems())
        {
            TreeItem item = new TreeItem(mFormItemsTree, 0);
            item.setText(formItem.getName());
            item.setData(formItem);

            for (DecompilationUnit child : formItem.getChildren())
            {
                addFormItemTreeNode(child, item);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private <T> void grabSelectedItems(List<T> selectedItems, TreeItem itemsTree)
    {
        for (TreeItem item : itemsTree.getItems())
        {
            if (item.getChecked())
            {
                selectedItems.add((T)item);
            }

            grabSelectedItems(selectedItems, item);
        }
    }

    public void updateDialogResult(DecompilationDialogResult result)
    {
        grabSelectedItems(result.getSelectedAttributes(), mAttributesTree.getTopItem());
    }

}
