package org.quiteoldorange.i3textutils.qfix.quickactions;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.bsl.model.impl.FunctionImpl;
import com._1c.g5.v8.dt.bsl.model.impl.MethodImpl;
import com._1c.g5.v8.dt.bsl.model.impl.ProcedureImpl;
import com._1c.g5.v8.dt.bsl.validation.CustomValidationMessageAcceptor;
import com._1c.g5.v8.dt.bsl.validation.IExternalBslValidator;

@SuppressWarnings("deprecation")
public class MethodQuickActionsMarkerGenerator
    implements IExternalBslValidator
{

    @Override
    public void validate(EObject object, CustomValidationMessageAcceptor messageAcceptor)
    {
        var f = object.eClass().getEStructuralFeature("name");


        if (object instanceof ProcedureImpl)
        {
            messageAcceptor.acceptInfo("Выберите быстрые действия", // Message
            object,  // Object
            f, // Feature
            0, // Index
                "MethodQuickActionsProcedure" // Code
            );
        }
        else
        {
            if (object instanceof FunctionImpl)
            {
                messageAcceptor.acceptInfo("Выберите быстрые действия", // Message
                    object, // Object
                    f, // Feature
                    0, // Index
                    "MethodQuickActionsFunction" // Code
                );
            }
        }
    }

    public MethodQuickActionsMarkerGenerator()
    {
        // TODO Auto-generated constructor stub
    }

    @Deprecated
    @Override
    public boolean needValidation(EObject object)
    {
        return object instanceof MethodImpl;
    }

}
