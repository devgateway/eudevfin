/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.permissions;

/**
 * Used by the {@link PermissionAuthorizationStrategy} to get the map key for the current component's permissions
 * and to set the current component as required if the permission for the current role allows it
 *
 * @author aartimon
 * @since 28/11/13
 */
public interface PermissionAwareComponent {
    /**
     * The corresponding key for the permission mapping
     * Usually return the {@link org.apache.wicket.Component#getId()} or some other mapping for components that might
     * reuse a placeholder id (eg. the tabs)
     *
     * @return the key for the current component
     */
    public String getPermissionKey();

    /**
     * Allows the {@link PermissionAuthorizationStrategy} to set the current component's {@link org.apache.wicket.markup.html.form.FormComponent}
     * to be set as required
     */
    public void enableRequired();
}
