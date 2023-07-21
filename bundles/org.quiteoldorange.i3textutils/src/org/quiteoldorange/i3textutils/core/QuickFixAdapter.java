/**
 *
 */
package org.quiteoldorange.i3textutils.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.quiteoldorange.i3textutils.Log;
import org.quiteoldorange.i3textutils.ServicesAdapter;
import org.quiteoldorange.i3textutils.qfix.movemethodtoregion.QuickFixProvider;

import com.e1c.g5.v8.dt.check.settings.CheckUid;

/**
 * @author ozolotarev
 *
 */
public class QuickFixAdapter
{
    public static void bindQuickFixes()
    {
        var workBench = ResourcesPlugin.getWorkspace();
        var root = workBench.getRoot();

        IProject[] projects = root.getProjects();

        if (projects.length < 1)
            return;

        var p = projects[0];

        rebindQuickFix(p, "module-structure-form-event-regions", i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE, //$NON-NLS-1$
            QuickFixProvider.class, "fixModuleStructureFormEventRegions"); //$NON-NLS-1$

        rebindQuickFix(p, "module-structure-method-in-regions", i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE, //$NON-NLS-1$
            QuickFixProvider.class, "fixModuleStructureMethodInRegions"); //$NON-NLS-1$

    }

    private static void rebindQuickFix(IProject dummyProj, String checkId, String checkProviderId, Class<?> fixProvider,
        String fixMethodName)
    {
        var checksRepo = ServicesAdapter.instance().getChecksRepository();
        // com.e1c.v8codestyle.bsl:extension-method-prefix


        CheckUid uid = new CheckUid(checkId, checkProviderId);

        var suid = checksRepo.getShortUid(uid, dummyProj);

        if (suid.isEmpty())
        {
            i3TextUtilsPlugin.getDefault()
                .getLog()
                .error(String.format("Failed to bind quickfix for %s:%s", checkProviderId, checkId)); //$NON-NLS-1$
            return;
        }

        try
        {
            Log.Debug("Binding %s to SUID %s", uid.toString(), suid); //$NON-NLS-1$
            changeFixBinding(suid, fixProvider, fixMethodName);
        }
        catch (NoSuchFieldException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private static void changeFixBinding(String newCheckid, Class<?> providerClass, String methodName)
        throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException
    {

        var allMethods = providerClass.getMethods();

        Method fixMethod = null;

        for(var item: allMethods)
        {
            if (item.getName().equals(methodName))
            {
                fixMethod = item;
                break;
            }
        }

        if (fixMethod == null)
        {
            throw new NoSuchMethodException();
        }


        final Fix oldAnnotation = (Fix)fixMethod.getAnnotations()[0];

        Annotation newAnnotation = new Fix()
        {

            @Override
            public String value()
            {
                return newCheckid;
            }

            @Override
            public Class<? extends Annotation> annotationType()
            {
                return oldAnnotation.annotationType();
            }
        };

        fixMethod.getDeclaredAnnotations();
        Class<?> superclass = fixMethod.getClass().getSuperclass();

        Field declaredField = superclass.getDeclaredField("declaredAnnotations");
        declaredField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Class<? extends Annotation>, Annotation> map =
            (Map<Class<? extends Annotation>, Annotation>)declaredField.get(fixMethod);

        map.put(Fix.class, newAnnotation);
    }
}
