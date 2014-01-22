/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.permissions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.request.component.IRequestableComponent;
import org.devgateway.eudevfin.ui.common.Constants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Implements an authorization strategy that works with the permission scheme
 *
 * @author aartimon
 * @see IAuthorizationStrategy
 * @since 28/11/13
 */
public class PermissionAuthorizationStrategy implements IAuthorizationStrategy {
    /**
     * {@inheritDoc IAuthorizationStrategy#isInstantiationAuthorized}
     */
    @Override
    public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass) {
        //we're not adding permissions on instantiation yet
        return true;
    }

    /**
     * {@inheritDoc IAuthorizationStrategy#isActionAuthorized}
     */
    @Override
    public boolean isActionAuthorized(Component component, Action action) {
        if (action == Component.ENABLE)
            return true; // we don't have permissions for enable, yet
        if (!(component instanceof PermissionAwareComponent))
            return true;
        if (action != Component.RENDER)
            throw new AssertionError("was assuming that action is render from this step forward");
        PermissionAwareComponent pwc = (PermissionAwareComponent) component;
        Page page = component.getPage();
        if (page == null || !(page instanceof PermissionAwarePage))
            return true; //not a permission aware page => other strategies decide
        HashMap<String, RoleActionMapping> permissions = ((PermissionAwarePage) page).getPermissions();
        if (permissions == null)
            return true; //no permissions for page => let other strategies decide
        RoleActionMapping roleMapping = permissions.get(pwc.getPermissionKey());
        if (roleMapping == null)
            return true; //if we haven't got any permissions defined for the current component then we let the other strategies decide
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String transactionType = httpServletRequest.getParameter(Constants.PARAM_TRANSACTION_TYPE);
        if (transactionType == null || transactionType.isEmpty())
            return true; //not a transaction aware scope => others decide

        String allowedAction = roleMapping.getAction(transactionType); //the transaction type is used like a context role
        /**
         * We're currently only having 2 actions in the permission scheme {@link Constants.ACTION_RENDER} and {@link Constants.ACTION_REQUIRED}
         * both of them imply that rendering is allowed
         */

        if (allowedAction != null) {
            if (allowedAction.equals(Constants.ACTION_REQUIRED))
                pwc.enableRequired();
            return true;
        }

        return false; //no action found for current role then we can assume it's not allowed to render
    }
}
