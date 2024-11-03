/**
 *
 */
package org.quiteoldorange.i3textutils.preferences.projectoptions.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.quiteoldorange.i3textutils.preferences.projectoptions.IProjectOption;

/**
 * @author ozolotarev
 *
 */
public class StringProjectOption
    extends IProjectOption
{

    private Text mWidget;

    /**
     * @param key
     * @param descritption
     * @param defaultValue
     */
    public StringProjectOption(String key, String descritption, String defaultValue)
    {
        super(key, descritption, defaultValue);
    }

    @Override
    public void createWidget(Composite parent)
    {

        if (getGroupName() == null)
        {
            Group containingGroup = new Group(parent, SWT.NONE);

            // Настройка зоны диалога
            GridLayout layout = new GridLayout(2, false);

            containingGroup.setLayout(layout);
            containingGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

            Label label = new Label(containingGroup, SWT.NONE);
            label.setText(getDescription() + ":"); //$NON-NLS-1$

            mWidget = new Text(containingGroup, SWT.SINGLE);
            mWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        }
        else
        {
            Label label = new Label(parent, SWT.NONE);
            label.setText(getDescription() + ":"); //$NON-NLS-1$

            mWidget = new Text(parent, SWT.SINGLE);
            mWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        }

        mWidget.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent e)
            {
                setValue(mWidget.getText());
            }
        });

        updateWidgetState();
    }

    @Override
    public void updateWidgetState()
    {
        mWidget.setText(getValue());
    }


}
