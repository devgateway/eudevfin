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
 * @author aartimon
 * @since 12/12/13
 */
public abstract class PermissionProvider {

    private HashMap<String, RoleActionMapping> permissions;

    protected PermissionProvider() {
        permissions = initPermissions();
    }

    protected abstract HashMap<String, RoleActionMapping> initPermissions();

    public HashMap<String, RoleActionMapping> permissions() {
        return permissions;
    }
}
