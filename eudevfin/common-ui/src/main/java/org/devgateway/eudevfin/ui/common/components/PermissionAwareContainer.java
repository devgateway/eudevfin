/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

/**
 * Simple {@link org.apache.wicket.markup.html.TransparentWebMarkupContainer} that implements the {@link PermissionAwareComponent}
 *
 * @author aartimon
 * @since 16/12/13
 */
public class PermissionAwareContainer extends WebMarkupContainer implements PermissionAwareComponent {
    public PermissionAwareContainer(String id) {
        super(id);
        this.setRenderBodyOnly(true);
    }

    @Override
    public String getPermissionKey() {
        return getId();
    }

    @Override
    public void enableRequired() {
    }
}
