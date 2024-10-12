/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.eclipse.emf.common.util.EMap;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationSettings;
import org.quiteoldorange.i3textutils.formsdecompiler.P;

import com._1c.g5.v8.dt.form.model.CommandHandlerContainer;
import com._1c.g5.v8.dt.form.model.DefaultRepresentation;
import com._1c.g5.v8.dt.form.model.FormCommand;
import com._1c.g5.v8.dt.form.model.FormCommandHandlerContainer;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class FormCommandUnit
    extends DecompilationUnit
{

    @SuppressWarnings("unused")
    private FormCommand mEDTCommand;
    private String mHandler;
    private boolean mModifiesStoredData;
    private DefaultRepresentation mRepresentation;
    private EMap<String, String> mToolTip;

    @Override
    public void decompile(StringBuilder output, DecompilationContext context)
    {
        DecompilationSettings cfg = context.getDecompilationSettings();
        boolean isRussian = cfg.scriptVariant() == ScriptVariant.RUSSIAN;

        String newCommand = cfg.getNewCommadTemplateName();
        String thisForm = cfg.getThisFormTemplateName();
        String line = null;

        line = String.format("%s = %s.%s.%s(\"%s\");\n", newCommand, thisForm, P.Commands, P.Add, mName); //$NON-NLS-1$
        output.append(line);

        if (mHandler != null)
        {
            line = String.format("%s.%s = \"%s\";\n", newCommand, P.Handler, mHandler); //$NON-NLS-1$
            output.append(line);
        }

        line = String.format("%s.%s = %s;\n", newCommand, P.Caption, serializeMultiLangualString(mTitles, cfg)); //$NON-NLS-1$
        output.append(line);

        line = String.format("%s.%s = %s;\n", newCommand, P.ModifiesStoredData, //$NON-NLS-1$
            cfg.serializeBoolean(mModifiesStoredData));
        output.append(line);

        line = String.format("%s.%s = %s;\n", newCommand, P.Representation, //$NON-NLS-1$
            serializeRepresentationProperty(isRussian));
        output.append(line);

        line =
            String.format("%s.%s = %s;\n", newCommand, P.ToolTip, serializeMultiLangualString(mToolTip, cfg)); //$NON-NLS-1$
        output.append(line);
    }

    /**
     * @param cfg
     * @return
     */
    private String serializeRepresentationProperty(boolean isRussian)
    {
        if (isRussian)
        {
            switch (mRepresentation)
            {
            case AUTO:
                return "ОтображениеКнопки.Авто"; //$NON-NLS-1$
            case PICTURE:
                return "ОтображениеКнопки.Картинка"; //$NON-NLS-1$
            case TEXT:
                return "ОтображениеКнопки.Текст"; //$NON-NLS-1$
            case TEXT_PICTURE:
                return "ОтображениеКнопки.КартинкаИТекст"; //$NON-NLS-1$
            default:
                break;
            }
        }
        else
        {
            switch (mRepresentation)
            {
            case AUTO:
                return "ButtonRepresentation.Auto"; //$NON-NLS-1$
            case PICTURE:
                return "ButtonRepresentation.Picture"; //$NON-NLS-1$
            case TEXT:
                return "ButtonRepresentation.Text"; //$NON-NLS-1$
            case TEXT_PICTURE:
                return "ButtonRepresentation.PictureAndText"; //$NON-NLS-1$
            }
        }

        return ""; //$NON-NLS-1$
    }

    public FormCommandUnit(FormCommand formCommand)
    {
        mEDTCommand = formCommand;

        mName = formCommand.getName();
        mHandler = getStringHandlerName(formCommand);
        mTitles = formCommand.getTitle();
        mModifiesStoredData = formCommand.isModifiesStoredData();
        mRepresentation = formCommand.getRepresentation();
        mToolTip = formCommand.getToolTip();

    }

    /**
     * @param formCommand
     * @return
     */
    private String getStringHandlerName(FormCommand formCommand)
    {
        CommandHandlerContainer action = formCommand.getAction();
        FormCommandHandlerContainer cont = (FormCommandHandlerContainer)action;

        if (cont != null)
            return cont.getHandler().getName();
        else
            return null;
    }

    @Override
    public String toString()
    {
        return String.format("Команда: %s", getName()); //$NON-NLS-1$
    }

}
