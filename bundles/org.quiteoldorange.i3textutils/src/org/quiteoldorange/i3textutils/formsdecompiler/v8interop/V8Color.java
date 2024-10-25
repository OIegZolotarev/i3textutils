/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop;

import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationSettings;

import com._1c.g5.v8.dt.mcore.Color;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class V8Color
{
    public static String serialize(Color value, DecompilationSettings cfg)
    {
        String constructionString = getConstructionString(cfg.scriptVariant());
        return String.format("%s(%d,%d,%d)", constructionString, value.red(), value.green(), value.blue()); //$NON-NLS-1$
    }

    /**
     * @param scriptVariant
     * @return
     */
    private static String getConstructionString(ScriptVariant scriptVariant)
    {
        switch (scriptVariant)
        {
        case ENGLISH:
            return "New Color"; //$NON-NLS-1$
        case RUSSIAN:
            return "Новый Цвет"; //$NON-NLS-1$
        default:
            return "Новый Цвет"; //$NON-NLS-1$
        }
    }
}
