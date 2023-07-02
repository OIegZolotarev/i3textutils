/**
 * Copyright (C) 2020, 1C-Soft LLC
 */
package org.quiteoldorange.i3textutils.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.quiteoldorange.i3textutils.QuickFixRegistrator;

import com._1c.g5.wiring.InjectorAwareServiceRegistrator;
import com._1c.g5.wiring.ServiceInitialization;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class i3TextUtilsPlugin
    extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "org.quiteoldorange.i3textutils"; //$NON-NLS-1$

    public static final String V8_CODESTYLE_BUNDLE = "com.e1c.v8codestyle.bsl"; //$NON-NLS-1$

    private static i3TextUtilsPlugin plugin;

    private BundleContext bundleContext;
    private InjectorAwareServiceRegistrator registrator;
    private Injector injector;

    /**
     * Получить экземпляр плагина. Через экземпляр плагина можно получать доступ к разнообразным механизмам Eclipse,
     * таким как:
     * <ul>
     * <li>Журнал логирования ошибок плагина</li>
     * <li>Механизм настройки свойств плагина</li>
     * <li>Механизм дескрипторов</li>
     * </ul>
     *
     * @return экземпляр плагина, никогда не должен возвращать <code>null</code>
     */
    public static i3TextUtilsPlugin getDefault()
    {
        return plugin;
    }

    /**
     * Запись статуса события в лог журнал плагина.
     *
     * @param status статус события для логирования, не может быть <code>null</code>.
     * Данный статус содержит в себе информацию о произошедшем событии (ошибка выполнения,
     * разнообразные предупреждения), которые были зафиксированы в логике работы плагина.
     */
    public static void log(IStatus status)
    {
        plugin.getLog().log(status);
    }

    /**
     * Запись исключения в лог журнал плагина
     *
     * @param throwable выкинутое исключение, не может быть <code>null</code>
     */
    public static void logError(Throwable throwable)
    {
        log(createErrorStatus(throwable.getMessage(), throwable));
    }

    /**
     * Создание записи с описанием ошибки в лог журнале плагина по выкинотому исключению и сообщению, его описывающим
     *
     * @param message описание выкинутого исключения, не может быть <code>null</code>
     * @param throwable выкинутое исключение, может быть <code>null</code>
     * @return созданное статус событие, не может быть <code>null</code>
     */
    public static IStatus createErrorStatus(String message, Throwable throwable)
    {
        return new Status(IStatus.ERROR, PLUGIN_ID, 0, message, throwable);
    }

    /**
     * Создание записи с описанием предупреждения в лог журнале плагина
     *
     * @param message описание предупреждения, не может быть <code>null</code>
     * @return созданное статус событие, не может быть <code>null</code>
     */
    public static IStatus createWarningStatus(String message)
    {
        return new Status(IStatus.WARNING, PLUGIN_ID, 0, message, null);
    }

    /**
     * Создание записи с описанием предупреждения в лог журнале плагина по выкинотому исключению и сообщению,
     * его описывающим
     *
     * @param message описание выкинутого исключения, не может быть <code>null</code>
     * @param throwable выкинутое исключение, может быть <code>null</code>
     * @return созданное статус событие, не может быть <code>null</code>
     */
    public static IStatus createWarningStatus(final String message, Exception throwable)
    {
        return new Status(IStatus.WARNING, PLUGIN_ID, 0, message, throwable);
    }

    /**
     * Данный метод является начальной точкой работы плагина
     *
     * @param bundleContext объект, создаваемый OSGi Framework, для доступа к разнообразным сервисам, например,
     * по работе с файловыми ресурсами внутри проекта
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception
    {
        super.start(bundleContext);

        this.bundleContext = bundleContext;
        plugin = this;

        registrator = new InjectorAwareServiceRegistrator(bundleContext, this::getInjector);

        ServiceInitialization.schedule(() -> {
            registrator.managedService(QuickFixRegistrator.class).activateBeforeRegistration().registerInjected();

            hackContentassistColors();

        });

    }

    /**
     *
     */
    private void hackContentassistColors()
    {
        try
        {
            Class<?> c = Class.forName("com._1c.g5.v8.dt.bsl.ui.contentassist.Messages"); //$NON-NLS-1$

            var field = c.getDeclaredField("ParametersHoverInfoControl_Description"); //$NON-NLS-1$
            field.setAccessible(true);

            String oldValue = (String)field.get(null);

            var color = contentAssistFixedColor();

            String hackColor = String.format("<style>a[style] { color: rgb(%d,%d,%d) !important;}</style>", //$NON-NLS-1$
                color.getRed(), color.getGreen(), color.getBlue());

            field.set(null, oldValue + hackColor);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    private Color contentAssistFixedColor()
    {
        //.i3TextUtilsPlugin.

        // TODO: сделать это более цивильным способом - сейчас это чуть лучше чем типовое говнище.
        return new Color(255, 120, 90);

        //return
    }

    /**
     * Данный метод вызывается при завершении работы плагина
     *
     * @param bundleContext объект, создаваемый OSGi Framework, для доступа к разнообразным сервисам, например,
     * по работе с файловыми ресурсами внутри проекта
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception
    {
        plugin = null;
        injector = null;
        super.stop(bundleContext);
    }

    /**
     * Получить объект, создаваемый OSGi Framework, для доступа к разнообразным сервисам, например, по работе с
     * файловыми ресурсами внутри проекта
     *
     * @return объект, создаваемый OSGi Framework, для доступа к разнообразным сервисам, например, по работе с
     * файловыми ресурсами внутри проекта
     */
    protected BundleContext getContext()
    {
        return bundleContext;
    }

    /**
     * Returns Guice injector for this plugin.
     *
     * @return Guice injector for this plugin, never <code>null</code>
     */
    /* package */ Injector getInjector()
    {
        Injector localInstance = injector;
        if (localInstance == null)
        {
            synchronized (i3TextUtilsPlugin.class)
            {
                localInstance = injector;
                if (localInstance == null)
                {
                    localInstance = createInjector();
                    injector = localInstance;
                }
            }
        }
        return localInstance;
    }

    private Injector createInjector()
    {
        try
        {
            return Guice.createInjector(new ExternalDependenciesModule(this));
        }
        catch (Exception e)
        {
            log(createErrorStatus("Failed to create injector for " //$NON-NLS-1$
                + getBundle().getSymbolicName(), e));
            throw new RuntimeException("Failed to create injector for " //$NON-NLS-1$
                + getBundle().getSymbolicName(), e);
        }
    }
}
