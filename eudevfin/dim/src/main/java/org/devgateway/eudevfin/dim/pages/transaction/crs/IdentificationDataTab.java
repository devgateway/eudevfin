/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.dim.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DateInputField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.joda.time.LocalDateTime;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class IdentificationDataTab extends Panel implements PermissionAwareComponent {
	protected PageParameters parameters;
    public static final String KEY = "tabs.identification";

    @SpringBean
    private OrganizationChoiceProvider organizationProvider;
    @SpringBean
    private CategoryProviderFactory categoryFactory;


    public IdentificationDataTab(String id,PageParameters parameters) {
        super(id);
        this.parameters=parameters;
        addComponents();
    }

    private void addComponents() {
        TextInputField<Integer> reportingYear = new TextInputField<>("1reportingYear", new YearToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("reportingYear")));
        reportingYear.typeInteger().required().range(1900, 2099).decorateMask("9999");
        add(reportingYear);

        DateInputField commitmentDate = new DateInputField("1bCommitmentDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("commitmentDate")));
        add(commitmentDate);

        DropDownField<Organization> extendingAgency = new DropDownField<>("3extendingAgency",
                new RWComponentPropertyModel<Organization>("extendingAgency"), organizationProvider);
        extendingAgency.required();

        //see TransactionPage#initializeFinancialTransaction
        extendingAgency.getField().setEnabled(false);

        add(extendingAgency);

        TextInputField<Integer> crsId = new TextInputField<>("4crsId", new RWComponentPropertyModel<Integer>("crsIdentificationNumber"));
        crsId.typeInteger();
        add(crsId);

        TextInputField<String> donorProjectNumber = new TextInputField<>("5donorProjectNumber",
                new RWComponentPropertyModel<String>("donorProjectNumber"));
        donorProjectNumber.typeString();
        add(donorProjectNumber);

        DropDownField<Category> natureOfSubmission = new DropDownField<>("6natureSubmission",
                new RWComponentPropertyModel<Category>("natureOfSubmission"), categoryFactory.get(CategoryConstants.NATURE_OF_SUBMISSION_TAG));
        natureOfSubmission.required();
        add(natureOfSubmission);
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }
}
