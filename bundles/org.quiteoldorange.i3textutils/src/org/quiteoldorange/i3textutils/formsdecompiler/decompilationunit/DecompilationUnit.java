/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import java.util.LinkedList;
import java.util.List;

import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;

/**
 * @author ozolotarev
 *
 */
public abstract class DecompilationUnit
{
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
        mChildren.add(unit);
    }

    protected List<DecompilationUnit> getChildren()
    {
        return mChildren;
    }

    abstract public void decompile(StringBuilder output, DecompilationContext context);
}
