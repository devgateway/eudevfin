/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

/**
 * Simple {@link Label} that implements the {@link PermissionAwareComponent}
 *
 * @author aartimon
 * @since 28/11/13
 */
public class PermissionAwareLabel extends Label implements PermissionAwareComponent {

    private final String permissionKey;

    public PermissionAwareLabel(String id, IModel<?> model, String permissionKey) {
        super(id, model);
        this.permissionKey = permissionKey;
    }

    @Override
    public String getPermissionKey() {
        return permissionKey;
    }

    @Override
    public void enableRequired() {
        //do nothing
    }
}
