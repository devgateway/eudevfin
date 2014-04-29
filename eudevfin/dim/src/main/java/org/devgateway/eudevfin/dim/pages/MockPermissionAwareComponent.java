/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages;

import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

/**
 * @author Alexandru Artimon
 * @since 29/04/14
 */
public class MockPermissionAwareComponent implements PermissionAwareComponent {
    private boolean required = false;
    private String id;

    public MockPermissionAwareComponent(String id) {
        this.id = id;
    }

    @Override
    public String getPermissionKey() {
        return id;
    }

    @Override
    public void enableRequired() {
        required = true;
    }

    public boolean isRequired() {
        return required;
    }
}
