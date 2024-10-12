/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EMap;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationSettings;

/**
 * @author ozolotarev
 *
 */
public abstract class DecompilationUnit
{
    protected EMap<String, String> mTitles;
    protected String mName;

    private DecompilationUnit mParent = null;
    private boolean mShouldBeDecompiled = false;

    private List<DecompilationUnit> mChildren = new LinkedList<>();

    protected void setShouldBeDecompiledFlag(boolean flagValue)
    {
        mShouldBeDecompiled = flagValue;
    }

    protected boolean shouldBeDecompiled()
    {
        return mShouldBeDecompiled;
    }

    protected void addChildren(DecompilationUnit unit)
    {
        // TODO: заглушка для недоделанных алгоритмов
        if (unit == null)
        {
            return;
        }

        unit.mParent = this;
        mChildren.add(unit);
    }

    protected DecompilationUnit getParent()
    {
        return mParent;
    }

    public List<DecompilationUnit> getChildren()
    {
        return mChildren;
    }

    abstract public void decompile(StringBuilder output, DecompilationContext context);

    /**
     * @param cfg
     * @return
     */
    protected String serializeMultiLangualString(EMap<String, String> multiLangualString, DecompilationSettings cfg)
    {
        // Если заголовок не указан, то сваливаемся в имя по умолчанию
        if (mTitles.size() == 0)
            return String.format("\"%s\"", mName); //$NON-NLS-1$

        // https://its.1c.ru/db/v8std/content/761/hdoc
        // Оказывается НСтр надо лепить везде в интерфейсных текстах.
        // Поэтому так делать не комильфо.

        //        if (mTitles.size() == 1)
        //        {
        //            return mTitles.get(0).getValue();
        //        }

        // "ru = 'Здравствуйте'; en = 'Hello world'"

        String result = ""; //$NON-NLS-1$
        boolean firstEntry = true;

        ListIterator<Entry<String, String>> iter = mTitles.listIterator();

        while (iter.hasNext())
        {
            Entry<String, String> entry = iter.next();
            String langDescr = String.format("%s = '%s'", entry.getKey(), entry.getValue()); //$NON-NLS-1$

            if (!firstEntry)
                result = result + ";" + langDescr; //$NON-NLS-1$
            else
                result = langDescr;

        }

        return String.format("%s(\"%s\")", cfg.getNStrExpression(), result); //$NON-NLS-1$

    }

    /**
     * @return
     */
    public String getName()
    {
        return mName;
    }
}
