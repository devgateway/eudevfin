/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.permissions;

import java.util.HashMap;

/**
 * Pages that provide permissions for their pages will implement the @see getPermissions method
 * This is used in conjunction with the {@see org.devgateway.eudevfin.dim.core.permissions.PermissionAuthorizationStrategy}
 *
 * @author aartimon
 * @since 28/11/13
 */
public interface PermissionAwarePage {

    /**
     * Method gets the permissions for the current page
     *
     * @return a HashMap between the component's id for which we're retrieving the {@see org.devgateway.eudevfin.dim.core.permissions.RoleActionMapping}
     */
    public HashMap<String, RoleActionMapping> getPermissions();
}
