/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.components.tabs;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.model.StringResourceModel;

/**
 * Basic {@link org.apache.wicket.extensions.markup.html.tabs.AbstractTab} that provides a key which can be used to
 * identify the tab in the html DOM
 *
 * @author aartimon
 * @see ITabWithKey
 * @since 28/11/13
 */
public abstract class AbstractTabWithKey extends AbstractTab implements ITabWithKey {
    private final String tabKey;

    /**
     * {@inheritDoc}
     * @param title IModel used to represent the title of the tab. Must contain a string
     * @param tabKey unique tab key in the current container
     */
    public AbstractTabWithKey(StringResourceModel title, String tabKey) {
        super(title);
        this.tabKey = tabKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return tabKey;
    }
}
