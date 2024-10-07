/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;

/**
 * @author ozolotarev
 *
 */
public abstract class DecompilationUnit
{
    abstract public void Decompile(StringBuilder output, DecompilationContext context);
}
