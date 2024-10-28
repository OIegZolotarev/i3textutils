/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums;

import com._1c.g5.v8.dt.mcore.ButtonRepresentation;

/**
 * @author ozolotarev
 *
 */
public class ButtonRepresentationEnum
    extends BuiltInEnum<ButtonRepresentation>
{
    private ButtonRepresentationEnum()
    {
        super("ОтображениеКнопки", "ButtonRepresentation");// //$NON-NLS-1$ //$NON-NLS-2$

        addValue(ButtonRepresentation.AUTO, "Авто", "Auto"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(ButtonRepresentation.PICTURE, "Картинка", "Picture"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(ButtonRepresentation.PICTURE_AND_TEXT, "КартинкаИТекст", "PictureAndText"); //$NON-NLS-1$ //$NON-NLS-2$
        addValue(ButtonRepresentation.TEXT, "Текст", "Text"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static ButtonRepresentationEnum Instance = new ButtonRepresentationEnum();
}
