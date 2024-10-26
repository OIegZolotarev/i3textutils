/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop;

import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationSettings;

import com._1c.g5.v8.dt.mcore.Font;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class V8Font
{
    public static String serialize(Font f, DecompilationSettings cfg)
    {
        String construction = getConstructionString(cfg.scriptVariant());

        return String.format("%s(%s, %d, %s, %s, %s, %s, %d)", construction, f.faceName(), f.height(), //$NON-NLS-1$
            cfg.serializeBoolean(f.bold()),
            cfg.serializeBoolean(f.italic()), cfg.serializeBoolean(f.underline()), cfg.serializeBoolean(f.strikeout()),
            f.scale());

    }

    /**
     * @param variant
     * @return
     */
    private static String getConstructionString(ScriptVariant variant)
    {
        switch (variant)
        {
        case ENGLISH:
            return "New Font"; //$NON-NLS-1$
        case RUSSIAN:
            return "Новый Шрифт"; //$NON-NLS-1$
        default:
            return "Новый Шрифт"; //$NON-NLS-1$
        }
    }
}
