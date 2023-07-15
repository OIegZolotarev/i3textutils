/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter.tasks;

import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;

/**
 * @author ozolotarev
 *
 */
public class AddRegionTask
    implements IReformatTask
{

    String mRegionName = null;

    AddRegionTask(String regionName)
    {
        mRegionName = regionName;
    }

    @Override
    public String getDescription()
    {
        return String.format("Добавить область %s", mRegionName);
    }

    @Override
    public void execute(ModuleASTTree tree)
    {
        // TODO Auto-generated method stub

    }

}
