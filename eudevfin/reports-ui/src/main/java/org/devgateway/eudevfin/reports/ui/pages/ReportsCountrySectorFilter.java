package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.wicketstuff.annotation.mount.MountPath;


/**
 * @author idobre
 * @since 4/3/14
 */

@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
@MountPath(value = "/reportscountrysectorfilter")
public class ReportsCountrySectorFilter extends CustomReportsPage {
    private static final Logger logger = Logger.getLogger(ReportsCountrySectorFilter.class);

    public ReportsCountrySectorFilter () {
        Label title = new Label("title", new StringResourceModel("reportsCountrySector", this, null, null));
        add(title);

        nationalInstitution.setVisibilityAllowed(Boolean.FALSE);
        multilateralAgency.setVisibilityAllowed(Boolean.FALSE);
        typeOfFlowBiMulti.setVisibilityAllowed(Boolean.FALSE);
        typeOfAid.setVisibilityAllowed(Boolean.FALSE);
        typeOfExpenditure.setVisibilityAllowed(Boolean.FALSE);
        valueOfActivity.setVisibilityAllowed(Boolean.FALSE);
        startingYear.setVisibilityAllowed(Boolean.FALSE);
        completitionYear.setVisibilityAllowed(Boolean.FALSE);
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
                if (customReportsModel.getGeography() != null) {
                    pageParameters.add(ReportsConstants.GEOGRAPHY_PARAM, customReportsModel.getGeography().getName());
                }
                if (customReportsModel.getRecipient() != null) {
                    pageParameters.add(ReportsConstants.RECIPIENT_PARAM, customReportsModel.getRecipient().getName());
                }
                if (customReportsModel.getSector() != null) {
                    pageParameters.add(ReportsConstants.SECTOR_PARAM, customReportsModel.getSector().getName());
                    if (customReportsModel.getSector().getParentCategory().getName().toLowerCase().
                            equals(ReportsConstants.ROOT_SECTOR.toLowerCase())) {
                        pageParameters.add(ReportsConstants.ISROOTSECTOR_PARAM, Boolean.TRUE);
                    }
                }
                if (customReportsModel.getYear() != null) {
                    pageParameters.add(ReportsConstants.YEAR_PARAM, customReportsModel.getYear());
                }
                if (customReportsModel.getCoFinancingTransactionsOnly() != null && customReportsModel.getCoFinancingTransactionsOnly() != Boolean.FALSE) {
                    pageParameters.add(ReportsConstants.COFINANCING_PARAM, customReportsModel.getCoFinancingTransactionsOnly());
                }
                if (customReportsModel.getCPAOnly() != null && customReportsModel.getCPAOnly() != Boolean.FALSE) {
                    pageParameters.add(ReportsConstants.CPAONLY_PARAM, customReportsModel.getCPAOnly());
                }

                setResponsePage(ReportsCountrySectorDashboards.class, pageParameters);
            }
        });
    }
}
