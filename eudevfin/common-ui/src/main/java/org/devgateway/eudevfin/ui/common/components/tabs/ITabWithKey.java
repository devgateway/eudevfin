/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.components.tabs;

import org.apache.wicket.extensions.markup.html.tabs.ITab;

/**
 * Adds a key to be used with {@link org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent}
 *
 * @author aartimon
 * @since 28/11/13
 */
public interface ITabWithKey extends ITab {

    /**
     * The key is used in the permission HashMap returned by the {@link org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent}
     *
     * @return key for the current component
     */
    public String getKey();
}
