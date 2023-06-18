/*******************************************************************************
 * Copyright (C) 2021, 1C-Soft LLC and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     1C-Soft LLC - initial API and implementation
 *******************************************************************************/
package org.quiteoldorange.i3textutils.core;


import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

/**
 * Guice module aware executable extension factory for plugin.
 *
 * @author Dmitriy Marmyshev
 */
@SuppressWarnings("restriction")
public class ExecutableExtensionFactory
    extends AbstractGuiceAwareExecutableExtensionFactory
{
    @Override
    protected Bundle getBundle()
    {
        return Activator.getDefault().getBundle();
    }

    @Override
    protected Injector getInjector()
    {
        return Activator.getDefault().getInjector();
    }
}
