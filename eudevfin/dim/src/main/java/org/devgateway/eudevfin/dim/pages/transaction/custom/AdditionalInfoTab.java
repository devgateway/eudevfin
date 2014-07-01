/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanel;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;

/**
 * @author aartimon
 * @since 16/12/13
 */
public class AdditionalInfoTab extends PreviewableFormPanel implements PermissionAwareComponent {
    public static final String KEY = "tabs.additional";
	private PageParameters parameters;

    public AdditionalInfoTab(String id,PageParameters parameters) {
        super(id);
        this.parameters=parameters;
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
