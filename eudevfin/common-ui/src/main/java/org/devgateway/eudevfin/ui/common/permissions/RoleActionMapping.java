/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.permissions;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.ui.common.Constants;

/**
 * Used by the {@link PermissionAwarePage} to return a hashmap with the permissions for the components in the
 * current page
 *
 * @author aartimon
 * @since 27/11/13
 */
public class RoleActionMapping {
    private static final Logger logger = Logger.getLogger(RoleActionMapping.class);

    //simple mapping between role and permission
    private HashMap<String, String> mapping;


    public RoleActionMapping() {
        mapping = new HashMap<>();
    }

    public RoleActionMapping render(String[] roles) {
        for (String role : roles)
            render(role);
        return this;
    }

    public RoleActionMapping render(String role) {
        return render(role, false);
    }

    public RoleActionMapping render(String role, boolean override) {
        if (mapping.containsKey(role)) {
            if (override)
                logger.warn("Overriding permission for role:" + role);
            else
                throw new AssertionError("Overlapping permissions");
        }
        mapping.put(role, Constants.ACTION_RENDER);
        return this;
    }

    /**
     * See {@link org.devgateway.eudevfin.ui.common.permissions.RoleActionMapping#notCollected(String)}
     *
     * @param roles set of roles to be removed
     * @return this instance
     */
    public RoleActionMapping notCollected(String[] roles) {
        for (String role : roles)
            notCollected(role);
        return this;
    }

    /**
     * Method used to remove roles when overriding an existing permission scheme
     *
     * @param role to be removed
     * @return this instance
     */
    public RoleActionMapping notCollected(String role) {
        if (!mapping.containsKey(role))
            throw new AssertionError("Role not found:" + role);
        mapping.remove(role);
        return this;
    }

    public RoleActionMapping required(String[] roles) {
        for (String role : roles)
            required(role);
        return this;
    }

    public RoleActionMapping required(String role) {
        if (mapping.containsKey(role))
            throw new AssertionError("Overlapping permissions for role:" + role);
        mapping.put(role, Constants.ACTION_REQUIRED);
        return this;
    }

    public String getAction(String role) {
        return mapping.get(role);
    }

}
