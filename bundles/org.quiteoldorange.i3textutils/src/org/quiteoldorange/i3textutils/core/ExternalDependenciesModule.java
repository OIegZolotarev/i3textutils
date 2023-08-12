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

import org.eclipse.core.runtime.Plugin;

import com._1c.g5.v8.dt.bsl.ui.BslDocumentationProvider;
import com._1c.g5.v8.dt.core.handle.IV8Model;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.form.service.item.IFormItemManagementService;
import com._1c.g5.v8.dt.form.service.item.IFormItemMovementService;
import com._1c.g5.v8.dt.form.service.item.IFormItemTypeManagementService;
import com._1c.g5.v8.dt.validation.marker.IMarkerManager;
import com._1c.g5.wiring.AbstractServiceAwareModule;
import com.e1c.g5.v8.dt.check.qfix.IFixRepository;
import com.e1c.g5.v8.dt.check.settings.ICheckRepository;

/**
 * External services bindings for plugin.
 *
 *
 *
 */
public class ExternalDependenciesModule
    extends AbstractServiceAwareModule
{

    /**
     * @param plugin
     */
    public ExternalDependenciesModule(Plugin plugin)
    {
        super(plugin);
    }

    @Override
    protected void doConfigure()
    {
        // Какая-то java-хуйня
        bind(IV8ProjectManager.class).toService();
        bind(ICheckRepository.class).toService();
        bind(IFixRepository.class).toService();
        bind(IMarkerManager.class).toService();
        bind(IBmModelManager.class).toService();
        bind(IFormItemTypeManagementService.class).toService();
        bind(IFormItemManagementService.class).toService();
        bind(IFormItemMovementService.class).toService();
        bind(IV8Model.class).toService();
        bind(BslDocumentationProvider.class).toService();

    }

}
