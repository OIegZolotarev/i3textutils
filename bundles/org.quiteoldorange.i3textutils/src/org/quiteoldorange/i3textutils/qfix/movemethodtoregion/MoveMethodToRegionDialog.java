/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.movemethodtoregion;

import java.util.List;

import org.eclipse.emf.edit.ui.provider.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * @author ozolotarev
 *
 */
public class MoveMethodToRegionDialog
    extends TitleAreaDialog
{

    private TableViewer mRegionsTable;
    private List<CandidateRegion> mCandidates;

    private CandidateRegion mSelectedRegion = null;

    /**
     * @param parentShell
     */
    public MoveMethodToRegionDialog(List<CandidateRegion> candidates)
    {
        super(Display.getCurrent().getActiveShell());

        mCandidates = candidates;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        setTitle("Перемещение в область"); //$NON-NLS-1$
        setMessage("Выберите область в которую следуюет переместить метод"); //$NON-NLS-1$

        getShell().setText("Рефакторинг"); //$NON-NLS-1$

        Composite control = (Composite)super.createDialogArea(parent);
        mRegionsTable = createDataTable(control, "Области модуля", mCandidates); //$NON-NLS-1$


        return control;
    }


    private static class CustomLabelProvider
        extends LabelProvider
        implements IStyledLabelProvider
    {
        @Override
        public String getText(Object element)
        {
            CandidateRegion r = (CandidateRegion)element;
            return r.getName();
        }

        @Override
        public StyledString getStyledText(Object element)
        {
            CandidateRegion r = (CandidateRegion)element;
            String existanceFlag = r.isExists() ? "" : "*"; //$NON-NLS-1$//$NON-NLS-2$
            return new StyledString(r.getName() + existanceFlag);
        }

        @Override
        public Image getImage(Object element)
        {
            return super.getImage(element);
        }
    }

    private TableViewer createDataTable(Composite composite, String headerName, List<CandidateRegion> content)
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

    @Override
    @SuppressWarnings("unchecked")
    protected void okPressed()
    {
        StructuredSelection sel = (StructuredSelection)mRegionsTable.getSelection();
        var element = sel.getFirstElement();
        mSelectedRegion = (CandidateRegion)element;
        super.okPressed();
    }

    public CandidateRegion getSelectedRegion()
    {
        return mSelectedRegion;
    }

}
