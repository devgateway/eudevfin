/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;

import org.apache.wicket.markup.html.panel.Panel;
import org.devgateway.eudevfin.dim.core.components.TextAreaInputField;
import org.devgateway.eudevfin.dim.core.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;

/**
 * @author aartimon
 * @since 16/12/13
 */
public class AdditionalInfoTab extends Panel implements PermissionAwareComponent {
    public static final String KEY = "tabs.additional";

    public AdditionalInfoTab(String id) {
        super(id);

        TextAreaInputField activityProjectTitle = new TextAreaInputField("99otherComments", new RWComponentPropertyModel<String>("otherComments"));
        activityProjectTitle.setSize(InputBehavior.Size.XXLarge).setRows(10);
        add(activityProjectTitle);
    }


    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {

    }
}
