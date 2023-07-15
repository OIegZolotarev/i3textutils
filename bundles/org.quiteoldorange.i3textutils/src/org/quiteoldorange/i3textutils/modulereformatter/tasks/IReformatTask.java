/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter.tasks;

import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;

/**
 * @author ozolotarev
 *
 */
public interface IReformatTask
{
    abstract String getDescription();

    abstract void execute(ModuleASTTree tree);
}
