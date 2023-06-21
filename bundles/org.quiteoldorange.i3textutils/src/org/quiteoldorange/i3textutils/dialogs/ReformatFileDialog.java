/**
 *
 */
package org.quiteoldorange.i3textutils.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.ui.provider.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author ozolotarev
 *
 */
public class ReformatFileDialog
    extends TitleAreaDialog
{

    /**
     * @param parentShell
     */
    public ReformatFileDialog(Shell parentShell)
    {
        super(parentShell);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        setTitle("Размещение методов модуля по областям");
        setMessage("Настройте стратегию переформатирования модуля");

        getShell().setText("Переформатирование модуля");

        Composite control = (Composite)super.createDialogArea(parent);

        var items = new ArrayList<String>();
        items.add("Привет");
        items.add("Мир");

        createDataTable(control, "Процедуры модуля", items);


        return control;
    }


    private static class CustomLabelProvider
        extends LabelProvider
        implements IStyledLabelProvider
    {
        @Override
        public String getText(Object element)
        {
            return super.getText(element);
        }

        @Override
        public StyledString getStyledText(Object element)
        {
            return new StyledString(super.getText(element));
        }

        @Override
        public Image getImage(Object element)
        {
            return super.getImage(element);
        }
    }

    private TableViewer createDataTable(Composite composite, String headerName, List<String> content)
    {
        final TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
        int height = tableViewer.getTable().getItemHeight() * 5;
        tableViewer.getTable().setHeaderVisible(true);
        GridDataFactory.fillDefaults().hint(SWT.DEFAULT, height).grab(true, false).applyTo(tableViewer.getTable());

        TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.BORDER);
        column.getColumn().setText(headerName);
        column.setLabelProvider(new DelegatingStyledCellLabelProvider(new CustomLabelProvider()));
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        tableViewer.setInput(content);

        column.getColumn().pack();
        return tableViewer;
    }

}
