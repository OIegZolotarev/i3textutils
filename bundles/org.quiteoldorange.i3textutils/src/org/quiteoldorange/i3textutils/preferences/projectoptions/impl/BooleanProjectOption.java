/**
 *
 */
package org.quiteoldorange.i3textutils.preferences.projectoptions.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.quiteoldorange.i3textutils.preferences.projectoptions.IProjectOption;

/**
 * @author ozolotarev
 *
 */
public class BooleanProjectOption
    extends IProjectOption
{

    private Button mWidget;

    /**
     * @param key
     * @param descritption
     * @param defaultValue
     */
    public BooleanProjectOption(String key, String descritption, Boolean defaultValue)
    {
        super(key, descritption, defaultValue.toString());
    }

    @Override
    public void createWidget(Composite parent)
    {
        mWidget = new Button(parent, SWT.CHECK);
        mWidget.setText(getDescription());

        mWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        mWidget.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (mWidget.getSelection())
                    setValue("true"); //$NON-NLS-1$
                else
                    setValue("false"); //$NON-NLS-1$
            }
        });

        updateWidgetState();
    }

    @Override
    public void updateWidgetState()
    {
        Boolean b = Boolean.valueOf(getValue());
        mWidget.setSelection(b);
    }

    /**
     * @return
     */
    public boolean getValueBoolean()
    {
        return Boolean.valueOf(getValue());
    }

}
