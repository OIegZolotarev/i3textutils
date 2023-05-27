/**
 *
 */
package org.quiteoldrange.i3textutils;


import java.util.List;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.RegionPreprocessor;
import com._1c.g5.v8.dt.bsl.model.util.BslUtil;

/**
 * @author ozolotarev
 *
 */
public class ModuleRefactoringUtils
{
    /**
     * Ищет область модуля по имени
     * @param name - Имя метода
     * @param model - модель модуля
     * @return - найденная область
     */
    public RegionPreprocessor findModuleRegion(String name, Module model) {

        List<RegionPreprocessor> items = BslUtil.getAllRegionPreprocessors(model);

        for (RegionPreprocessor region : items)
        {
            if (region.getName().equals(name))
                return region;
        }

        return null;

    }

}
