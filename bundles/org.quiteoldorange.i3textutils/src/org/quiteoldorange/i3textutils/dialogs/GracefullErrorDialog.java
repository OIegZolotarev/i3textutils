/**
 *
 */
package org.quiteoldorange.i3textutils.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;




/**
 * @author ozolotarev
 *
 */
public class GracefullErrorDialog
    extends Dialog
{

    private String mTitle;
    private String mMessage;


    public GracefullErrorDialog(String title, String message)
    {
        super(Display.getCurrent().getActiveShell());

        mTitle = title;
        mMessage = message;

    }

    @Override
    protected void configureShell(Shell shell)
    {
        super.configureShell(shell);
        shell.setText("Произошла ошибка");
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite container = (Composite)super.createDialogArea(parent);



        RowLayout rwH = new RowLayout(SWT.HORIZONTAL);
        container.setLayout(rwH);

        RowLayout rwV = new RowLayout(SWT.VERTICAL);
        container.setLayout(rwH);

        MakeLeftColumn(container, rwV);
        MakeRightColumn(container, rwV);

        return container;
    }

    private void MakeRightColumn(Composite container, RowLayout rwV)
    {
        Composite col2 = new Composite(container, SWT.NONE);
        col2.setLayout(rwV);

        Label title = new Label(col2, SWT.NONE);
        title.setText(mTitle);

        Label message = new Label(col2, SWT.NONE);
        message.setText(mMessage);

        Label sorry = new Label(col2, SWT.NONE);
        sorry.setText("Извините");
    }

    private void MakeLeftColumn(Composite container, RowLayout rwV)
    {
        Composite col1 = new Composite(container, SWT.NONE);
        col1.setLayout(rwV);

        ImageRegistry reg = i3TextUtilsPlugin.getDefault().getImageRegistry();
        Image myImage = reg.get("images/gracefull_error.png");

        Label wIcon = new Label(col1, SWT.NONE);
        wIcon.setImage(myImage);
    }


    public static void constructAndShow(String title, String message)
    {
        GracefullErrorDialog newDialog = new GracefullErrorDialog(title, message);
        newDialog.open();
    }
}
