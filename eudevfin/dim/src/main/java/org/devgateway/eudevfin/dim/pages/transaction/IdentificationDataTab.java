/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.dim.core.RWComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.components.DateInputField;
import org.devgateway.eudevfin.dim.core.components.DropDownField;
import org.devgateway.eudevfin.dim.core.components.TextInputField;
import org.devgateway.eudevfin.dim.core.components.tabs.AbstractTabWithKey;
import org.devgateway.eudevfin.dim.core.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.dim.core.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.dim.core.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.dim.core.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.dim.core.temporary.SB;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.Organization;
import org.joda.time.LocalDateTime;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class IdentificationDataTab extends Panel implements PermissionAwareComponent {

    public static final String KEY = "tabs.identification";

    private IdentificationDataTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        TextInputField<Integer> reportingYear = new TextInputField<>("1reportingYear", new YearToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("reportingYear")));
        reportingYear.typeInteger().required().range(1900, 2099).decorateMask("9999");
        add(reportingYear);

        DateInputField commitmentDate = new DateInputField("1bCommitmentDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("commitmentDate")));
        add(commitmentDate);

        DropDownField<Organization> reportingCountry = new DropDownField<>("2reportingCountry",
                new RWComponentPropertyModel<Organization>("reportingOrganization"), SB.organizationProvider);
        reportingCountry.required();
        add(reportingCountry);

        DropDownField<Organization> extendingAgency = new DropDownField<>("3extendingAgency",
                new RWComponentPropertyModel<Organization>("extendingAgency"), SB.organizationProvider);
        extendingAgency.required();
        add(extendingAgency);

        TextInputField<Integer> crsId = new TextInputField<>("4crsId", new RWComponentPropertyModel<Integer>("crsIdentificationNumber"));
        crsId.typeInteger();
        add(crsId);

        TextInputField<String> donorProjectNumber = new TextInputField<>("5donorProjectNumber",
                new RWComponentPropertyModel<String>("donorProjectNumber"));
        add(donorProjectNumber);

        DropDownField<Category> natureOfSubmission = new DropDownField<>("6natureSubmission",
                new RWComponentPropertyModel<Category>("natureOfSubmission"), SB.categoryProvider);
        natureOfSubmission.required();
        add(natureOfSubmission);
    }

    public static ITabWithKey newTab(Component askingComponent) {
        return new AbstractTabWithKey(new StringResourceModel(KEY, askingComponent, null), KEY) {
            private static final long serialVersionUID = -724508987522388955L;

            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new IdentificationDataTab(panelId);
            }
        };
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }
}
