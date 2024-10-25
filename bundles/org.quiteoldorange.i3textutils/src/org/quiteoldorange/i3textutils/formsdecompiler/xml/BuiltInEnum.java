/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.xml;

import java.util.HashMap;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class BuiltInEnum<T>
{
    String mNameRU;
    String mNameEN;

    protected HashMap<T, Value> mValues;

    /**
     *
     */
    public BuiltInEnum(String nameRu, String nameEn)
    {
        mNameRU = nameRu;
        mNameEN = nameEn;

        mValues = new HashMap<>();
    }

    void addValue(T value, String valueRu, String valueEn)
    {
        Value v = new Value(valueRu, valueEn);
        mValues.put(value, v);
    }

    private String getName(ScriptVariant scriptVariant)
    {
        switch (scriptVariant)
        {
        case ENGLISH:
            return mNameEN;
        case RUSSIAN:
            return mNameRU;
        default:
            return mNameRU;
        }
    }

    public String serialize(T value, ScriptVariant scriptVariant)
    {
        if (mValues.containsKey(value))
        {
            Value v = mValues.get(value);
            return String.format("%s.%s", getName(scriptVariant), v.getString(scriptVariant)); //$NON-NLS-1$
        }
        else
        {
            return ""; //$NON-NLS-1$
        }

    }

    public static class Value
    {
        private String mValueRU;
        private String mValueEN;

        public Value(String nameRu, String nameEn)
        {
            mValueRU = nameRu;
            mValueEN = nameEn;
        }

        public String getString(ScriptVariant variant)
        {
            switch (variant)
            {
            case ENGLISH:
                return mValueEN;
            case RUSSIAN:
                return mValueRU;
            default:
                return mValueRU;
            }
        }
    }

}
