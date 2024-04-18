/**
 *
 */
package org.quiteoldorange.i3textutils.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.quiteoldorange.i3textutils.modulereformatter.ModuleReformatter;

/**
 * @author ozolotarev
 *
 */
public class ReformatModule
    extends AbstractHandler
{

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        ModuleReformatter reformatter = ModuleReformatter.construct(event);

        if (reformatter == null)
            return null;

        reformatter.scheduleAll();
        reformatter.run();

        return null;

    }

}
