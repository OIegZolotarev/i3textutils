/**
 *
 */
package org.quiteoldorange.i3textutils.core;

import java.awt.AWTException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.quiteoldorange.i3textutils.Log;
import org.quiteoldorange.i3textutils.ServicesAdapter;
import org.quiteoldorange.i3textutils.qfix.WrapObjectModuleWithPreprocessorDefinitions;
import org.quiteoldorange.i3textutils.qfix.methodmissingpragmas.MissingMethodPragmaQuickFixProvider;
import org.quiteoldorange.i3textutils.qfix.movemethodtoregion.ModuleRegionQuickFixProvider;

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

        // TODO
        // Если проект является проектом внешней обработки - то getShortUid выдает NULL
        // Нужно цепляться где-то тут за проект конфигурации (?)
        // И в целом надо предельно осторожным быть в фазе загрузки ресурсов -
        // любой экспешн роняет весь проект и делается полный ребилд (очень долго)
        var p = projects[0];


        assert (p != null);

        rebindQuickFix(p, "module-structure-event-regions", i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE, //$NON-NLS-1$
            ModuleRegionQuickFixProvider.class, "fixModuleStructureFormEventRegions"); //$NON-NLS-1$

        rebindQuickFix(p, "module-structure-form-event-regions", i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE, //$NON-NLS-1$
            ModuleRegionQuickFixProvider.class, "fixModuleStructureFormEventRegions"); //$NON-NLS-1$

        rebindQuickFix(p, "module-structure-method-in-regions", i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE, //$NON-NLS-1$
            ModuleRegionQuickFixProvider.class, "fixModuleStructureMethodInRegions"); //$NON-NLS-1$

        rebindQuickFix(p, "form-module-missing-pragma", i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE, //$NON-NLS-1$
            MissingMethodPragmaQuickFixProvider.class, "run"); //$NON-NLS-1$

        rebindQuickFix(p, "module-accessibility-at-client", i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE, //$NON-NLS-1$
            WrapObjectModuleWithPreprocessorDefinitions.class, "run"); //$NON-NLS-1$

        // А эта проверка использует старую систему, лол
        //rebindQuickFix(p, "function-should-return-value", i3TextUtilsPlugin.V8_CODESTYLE_BUNDLE, //$NON-NLS-1$
        //ConvertFunctionToProcedure.class, "run"); //$NON-NLS-1$


        TrayIconDemo td = new TrayIconDemo();
        try
        {
            td.displayTray();
        }
        catch (AWTException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static synchronized void rebindQuickFix(IProject dummyProj, String checkId, String checkProviderId,
        Class<?> fixProvider,
        String fixMethodName)
    {
        var checksRepo = ServicesAdapter.instance().getChecksRepository();
        // com.e1c.v8codestyle.bsl:extension-method-prefix


        CheckUid uid = new CheckUid(checkId, checkProviderId);
        String suid = null;

        try
        {
            suid = checksRepo.getShortUid(uid, dummyProj);
        }
        catch (Exception e)
        {
            return;
        }

        if (suid == null || suid.isEmpty())
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

        Field declaredField = superclass.getDeclaredField("declaredAnnotations"); //$NON-NLS-1$
        declaredField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Class<? extends Annotation>, Annotation> map =
            (Map<Class<? extends Annotation>, Annotation>)declaredField.get(fixMethod);

        map.put(Fix.class, newAnnotation);
    }
}
