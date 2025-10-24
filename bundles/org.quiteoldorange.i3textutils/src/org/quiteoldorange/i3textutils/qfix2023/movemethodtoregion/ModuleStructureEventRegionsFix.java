/**
 *
 */
package org.quiteoldorange.i3textutils.qfix2023.movemethodtoregion;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.xtext.resource.XtextResource;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;

import com.e1c.g5.v8.dt.bsl.check.qfix.IXtextBslModuleFixModel;
import com.e1c.g5.v8.dt.bsl.check.qfix.SingleVariantXtextBslModuleFix;
import com.e1c.g5.v8.dt.bsl.check.qfix.SingleVariantXtextBslModuleFixContext;
import com.e1c.g5.v8.dt.check.qfix.FixVariantDescriptor;
import com.e1c.g5.v8.dt.check.qfix.IFixChange;
import com.e1c.g5.v8.dt.check.qfix.IFixSession;
import com.e1c.g5.v8.dt.check.qfix.IFixVariant;
import com.e1c.g5.v8.dt.check.qfix.components.QuickFix;

/**
 * @author ozolotarev
 *
 */

@QuickFix(checkId = "module-structure-form-event-regions", supplierId = i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE)
public class ModuleStructureEventRegionsFix
    extends SingleVariantXtextBslModuleFix
{


    @Override
    public Collection<IFixVariant<SingleVariantXtextBslModuleFixContext>> getVariants(
        SingleVariantXtextBslModuleFixContext context, IFixSession session)
    {

        List<IFixVariant<SingleVariantXtextBslModuleFixContext>> result = new LinkedList<>();

        IFixVariant<SingleVariantXtextBslModuleFixContext> variant = new IFixVariant<>()
        {

            @Override
            public FixVariantDescriptor describeChanges(SingleVariantXtextBslModuleFixContext context,
                IFixSession session)
            {

                // Обработчик события "Отмена" следует разместить в области "ОбработчикиКомандФормы"
                // arg0.getIssue().getMessage()

                FixVariantDescriptor fd =
                    new FixVariantDescriptor("Переместить в область \"ОбработчикиКомандФормы\"", "details");

                return fd;
            }

            @Override
            public Collection<IFixChange> prepareChanges(SingleVariantXtextBslModuleFixContext context,
                IFixSession session)
            {
                // TODO Auto-generated method stub
                return null;
            }
        };

        result.add(variant);

        return result;
    }

    @Override
    protected TextEdit fixIssue(XtextResource arg0, IXtextBslModuleFixModel arg1) throws BadLocationException
    {
        // TODO Auto-generated method stub
        return null;
    }

}
