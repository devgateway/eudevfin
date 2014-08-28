/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 4/16/14
 */
@AuthorizeInstantiation({AuthConstants.Roles.ROLE_USER,AuthConstants.Roles.ROLE_VIEWER})
@MountPath(value = "/reportsinstitutiontypeofaidfilter")
public class ReportsInstitutionTypeOfAidFilter extends CustomReportsPage {

    public ReportsInstitutionTypeOfAidFilter () {
        geography.setVisibilityAllowed(Boolean.FALSE);
        recipient.setVisibilityAllowed(Boolean.FALSE);
        sector.setVisibilityAllowed(Boolean.FALSE);
        typeOfExpenditure.setVisibilityAllowed(Boolean.FALSE);
        valueOfActivity.setVisibilityAllowed(Boolean.FALSE);
        startingYear.setVisibilityAllowed(Boolean.FALSE);
        completitionYear.setVisibilityAllowed(Boolean.FALSE);
        CoFinancingTransactionsOnly.setVisibilityAllowed(Boolean.FALSE);
        CPAOnly.setVisibilityAllowed(Boolean.FALSE);
        showRelatedBudgetCodes.setVisibilityAllowed(Boolean.FALSE);
    }

    @Override
    protected void addSubmitButton() {
        form.add(new BootstrapSubmitButton("submit", new StringResourceModel("button.submit", this, null, null)) {
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(feedbackPanel);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                CustomReportsModel customReportsModel = (CustomReportsModel)form.getModelObject();
                PageParameters pageParameters = new PageParameters();

                // Add the currency parameter. (ODAEU-257)
                // The tables should show only one currency (either national or USD based on the selection made by the user)
                String nationalCurrency = new StringResourceModel("pricesNationalCurrency", this, null, null).getObject();
                if (customReportsModel.getPricesCurrency().equals(nationalCurrency)) {
                    pageParameters.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, Boolean.TRUE);
                } else {
                    pageParameters.add(ReportsConstants.ISNATIONALCURRENCY_PARAM, Boolean.FALSE);
                }
                if (customReportsModel.getNationalInstitution() != null) {
                    pageParameters.add(ReportsConstants.INSTITUTION_PARAM, customReportsModel.getNationalInstitution().getName());
                }
                if (customReportsModel.getMultilateralAgency() != null) {
                    pageParameters.add(ReportsConstants.AGENCY_PARAM, customReportsModel.getMultilateralAgency().getName());
                }
                if (customReportsModel.getTypeOfFlowbiMulti() != null) {
                    pageParameters.add(ReportsConstants.TYPEOFFLOW_PARAM, customReportsModel.getTypeOfFlowbiMulti().getName());
                }
                if (customReportsModel.getYear() != null) {
                    pageParameters.add(ReportsConstants.YEAR_PARAM, customReportsModel.getYear());
                }
                if (customReportsModel.getHumanitarianAid() != null  && customReportsModel.getHumanitarianAid() != Boolean.FALSE) {
                    pageParameters.add(ReportsConstants.HUMANITARIANAID_PARAM, customReportsModel.getHumanitarianAid());
                }

                setResponsePage(ReportsInstitutionTypeOfAidDashboards.class, pageParameters);
            }
        });
    }
}
