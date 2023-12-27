/**
 *
 */
package org.quiteoldorange.i3textutils.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
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

        ImageRegistry reg = i3TextUtilsPlugin.getDefault().getImageRegistry();
        Image myImage = reg.get("images/gracefull_error.png");

        Label wIcon = new Label(container, SWT.NONE);
        wIcon.setImage(myImage);

        Label title = new Label(container, SWT.NONE);
        title.setText(mTitle);

        Label message = new Label(container, SWT.NONE);
        message.setText(mMessage);

        Label sorry = new Label(container, SWT.NONE);
        sorry.setText("Извините");


        return container;
    }


    public static void constructAndShow(String title, String message)
    {
        GracefullErrorDialog newDialog = new GracefullErrorDialog(title, message);
        newDialog.open();
    }
}
