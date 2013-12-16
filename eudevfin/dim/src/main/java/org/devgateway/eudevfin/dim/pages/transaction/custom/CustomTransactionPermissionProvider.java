/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import java.util.HashMap;

import org.devgateway.eudevfin.dim.core.permissions.RoleActionMapping;
import org.devgateway.eudevfin.dim.pages.transaction.crs.CRSTransactionPermissionProvider;

/**
 * Different permission provider for the {@link CustomTransactionPage}
 *
 * @author aartimon
 * @since 12/12/13
 */
public class CustomTransactionPermissionProvider extends CRSTransactionPermissionProvider {
    @Override
    protected HashMap<String, RoleActionMapping> initPermissions() {
        HashMap<String, RoleActionMapping> permissions = super.initPermissions();
        //TODO: add permissions
        return permissions;
    }
}
