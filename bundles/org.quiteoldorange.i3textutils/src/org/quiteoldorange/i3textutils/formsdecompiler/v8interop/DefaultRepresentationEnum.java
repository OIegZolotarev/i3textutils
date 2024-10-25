/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop;

import com._1c.g5.v8.dt.form.model.DefaultRepresentation;

/**
 * @author ozolotarev
 *
 */
public class DefaultRepresentationEnum
    extends BuiltInEnum<DefaultRepresentation>
{

    /**
     * @param nameRu
     * @param nameEn
     */
    private DefaultRepresentationEnum()
    {
        super("ОтображениеКнопки", "ButtonRepresentation"); //$NON-NLS-1$ //$NON-NLS-2$

        addValue(DefaultRepresentation.AUTO, "Авто", "Auto");
        addValue(DefaultRepresentation.PICTURE, "Картинка", "Picture");
        addValue(DefaultRepresentation.TEXT, "Текст", "Text");
        addValue(DefaultRepresentation.TEXT_PICTURE, "КартинкаИТекст", "PictureAndText");

    }

    public static DefaultRepresentationEnum Instance = new DefaultRepresentationEnum();

}
