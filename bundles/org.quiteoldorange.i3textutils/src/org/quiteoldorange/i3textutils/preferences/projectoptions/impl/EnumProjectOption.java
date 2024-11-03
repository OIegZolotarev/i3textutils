/**
 *
 */
package org.quiteoldorange.i3textutils.preferences.projectoptions.impl;


import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.quiteoldorange.i3textutils.Tuple;
import org.quiteoldorange.i3textutils.preferences.projectoptions.IProjectOption;

/**
 * @author ozolotarev
 *
 */
public class EnumProjectOption
    extends IProjectOption
{

    private List<Tuple<String, String>> mValues = new LinkedList<>();
    private WidgetType mWidgetType;

    private Combo mComboBox = null;
    private List<Button> mRadioButtons = null;

    public static enum WidgetType
    {
        RadioGroup,
        ComboBox
    };

    /**
     * @param key
     * @param descripttion
     * @param defaultValue
     */
    protected EnumProjectOption(String key, String descripttion, String defaultValue)
    {
        super(key, descripttion, defaultValue);
        // TODO Auto-generated constructor stub
    }

    protected void addValue(String description, String value)
    {
        mValues.add(new Tuple<>(description, value));
    }

    protected void setWidgetType(WidgetType type)
    {
        mWidgetType = type;
    }

    @Override
    public void createWidget(Composite parent)
    {
        switch (mWidgetType)
        {
        case ComboBox:
            createComboBox(parent);
            break;
        case RadioGroup:
            createRadioGroup(parent);
            break;
        default:
            break;
        }
    }

    /**
     * @param parent
     */
    private void createComboBox(Composite parent)
    {
        Label label = new Label(parent, SWT.NONE);
        label.setText(getDescription() + ":"); //$NON-NLS-1$

        mComboBox = new Combo(parent, SWT.DROP_DOWN);

        int idx = 0;
        for (Tuple<String, String> val : mValues)
        {
            mComboBox.add(val.getFirst());

            if (getValue().equals(val.getSecond()))
            {
                mComboBox.select(idx);
            }

            idx++;
        }

        mComboBox.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                int idx = mComboBox.getSelectionIndex();
                Tuple<String, String> val = mValues.get(idx);

                setValue(val.getSecond());
            }
        });
    }

    /**
     * @param parent
     */
    private void createRadioGroup(Composite parent)
    {
        Group containingGroup = new Group(parent, SWT.NONE);

        GridLayout layout = new GridLayout(1, false);
        containingGroup.setLayout(layout);
        containingGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

//        containingGroup.setLayout(new RowLayout(SWT.VERTICAL));

//        Label label = new Label(containingGroup, SWT.NONE);
//        label.setText(getDescription() + ":"); //$NON-NLS-1$

        containingGroup.setText(getDescription() + ":"); //$NON-NLS-1$



        mRadioButtons = new LinkedList<>();

        for (Tuple<String, String> val : mValues)
        {
            Button button = new Button(containingGroup, SWT.RADIO);
            button.setText(val.getFirst());
            button.setData(val.getSecond());

            if (val.getSecond().equals(getValue()))
                button.setSelection(true);

            button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

            button.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    Button source = (Button)e.getSource();
                    if (source.getSelection())
                    {
                        setValue((String)source.getData());
                    }
                }
            });

            mRadioButtons.add(button);
        }

    }

    @Override
    public void updateWidgetState()
    {
        switch (mWidgetType)
        {
        case ComboBox:

            int index = 0;
            for (Tuple<String, String> val : mValues)
            {
                if (val.getSecond().equals(getValue()))
                {
                    mComboBox.select(index);
                    return;
                }

                index++;
            }

            break;
        case RadioGroup:

            for (Button btn : mRadioButtons)
            {
                String btnValue = (String)btn.getData();
                if (btnValue.equals(getValue()))
                    btn.setSelection(true);
                else
                    btn.setSelection(false);
            }

            break;
        default:
            break;

        }
    }

}
