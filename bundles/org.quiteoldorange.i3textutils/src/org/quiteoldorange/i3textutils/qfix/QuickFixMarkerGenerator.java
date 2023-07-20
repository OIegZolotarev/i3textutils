package org.quiteoldorange.i3textutils.qfix;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.bsl.model.impl.MethodImpl;
import com._1c.g5.v8.dt.bsl.validation.CustomValidationMessageAcceptor;
import com._1c.g5.v8.dt.bsl.validation.IExternalBslValidator;

public class QuickFixMarkerGenerator
    implements IExternalBslValidator
{

    @Override
    public void validate(EObject object, CustomValidationMessageAcceptor messageAcceptor)
    {
        var f = object.eClass().getEStructuralFeature("name");


        messageAcceptor.info(object.getClass().getName(), // Message
            object,  // Object
            f, // Feature
            0, // Index
            "I3Marker" // Code
        );
    }

    public QuickFixMarkerGenerator()
    {
        // TODO Auto-generated constructor stub
    }

    @Deprecated
    @Override
    public boolean needValidation(EObject object)
    {
        // TODO Auto-generated method stub
        return object instanceof MethodImpl;
    }

}
