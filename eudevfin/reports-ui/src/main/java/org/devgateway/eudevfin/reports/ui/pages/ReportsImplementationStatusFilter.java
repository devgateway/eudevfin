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
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
@MountPath(value = "/reportsimplementationstatusfilter")
public class ReportsImplementationStatusFilter extends CustomReportsPage {

    public ReportsImplementationStatusFilter () {
        geography.setVisibilityAllowed(Boolean.FALSE);
        recipient.setVisibilityAllowed(Boolean.FALSE);
        year.setVisibilityAllowed(Boolean.FALSE);
        sector.setVisibilityAllowed(Boolean.FALSE);
        nationalInstitution.setVisibilityAllowed(Boolean.FALSE);
        multilateralAgency.setVisibilityAllowed(Boolean.FALSE);
        typeOfFlowBiMulti.setVisibilityAllowed(Boolean.FALSE);
        CPAOnly.setVisibilityAllowed(Boolean.FALSE);
        humanitarianAid.setVisibilityAllowed(Boolean.FALSE);
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
                if (customReportsModel.getTypeOfExpenditure() != null &&
                        customReportsModel.getTypeOfExpenditure().equals(new StringResourceModel("commitment", this, null, null).getObject())) {
                    pageParameters.add(ReportsConstants.EXPENDITURE_PARAM, Boolean.TRUE);
                }
                if (customReportsModel.getStartingYear() != null) {
                    pageParameters.add(ReportsConstants.STARTINGYEAR_PARAM, customReportsModel.getStartingYear());
                }
                if (customReportsModel.getCompletitionYear() != null) {
                    pageParameters.add(ReportsConstants.COMPLETIONYEAR_PARAM, customReportsModel.getCompletitionYear());
                }
                if (customReportsModel.getValueOfActivity() != null) {
                    if (customReportsModel.getValueOfActivity().equals(new StringResourceModel("moreThanAmount", this, null, null).getObject())) {
                        pageParameters.add(ReportsConstants.VALUE_PARAM, Boolean.TRUE);
                    } else {
                        if (customReportsModel.getValueOfActivity().equals(new StringResourceModel("lowerThanAmount", this, null, null).getObject())) {
                            pageParameters.add(ReportsConstants.VALUE_PARAM, Boolean.FALSE);
                        }
                    }
                }
                if (customReportsModel.getCoFinancingTransactionsOnly() != null && customReportsModel.getCoFinancingTransactionsOnly() != Boolean.FALSE) {
                    pageParameters.add(ReportsConstants.COFINANCING_PARAM, customReportsModel.getCoFinancingTransactionsOnly());
                }
                if (customReportsModel.getShowRelatedBudgetCodes() != null && customReportsModel.getShowRelatedBudgetCodes() != Boolean.FALSE) {
                    pageParameters.add(ReportsConstants.BUDGETCODES_PARAM, customReportsModel.getShowRelatedBudgetCodes());
                }

                setResponsePage(ReportsImplementationStatusDashboards.class, pageParameters);
            }
        });
    }
}
