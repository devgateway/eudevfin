/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.ui.pages;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Select2Choice;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.ApplicationJavaScript;
import org.devgateway.eudevfin.ui.common.FixBootstrapStylesCssResourceReference;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.CheckBoxField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.RadioChoiceField;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.PredefinedStringProvider;
import org.devgateway.eudevfin.ui.common.providers.UsedAreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.UsedOrganizationChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.YearProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author idobre
 * @since 4/1/14
 */

@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public abstract class CustomReportsPage extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(CustomReportsPage.class);

    @SpringBean
    private UsedAreaChoiceProvider usedAreaProvider;

    @SpringBean
    private CategoryProviderFactory categoryFactory;

    @SpringBean
    private UsedOrganizationChoiceProvider usedOrganizationProvider;

    @SpringBean
    private CustomFinancialTransactionService txService;

    @SpringBean
    private CategoryDaoImpl catDao;

    protected final NotificationPanel feedbackPanel;

    protected IFormValidator typeOfFlowValidator;

    protected final Form form;

    protected CheckBoxField humanitarianAid;

    protected DropDownField<Category> geography;

    protected DropDownField<Area> recipient;

    protected DropDownField<Organization> nationalInstitution;

    protected DropDownField<ChannelCategory> multilateralAgency;

    protected DropDownField<Category> typeOfFlowBiMulti;

    protected DropDownField<Category> sector;

    protected DropDownField<String> typeOfExpenditure;

    protected DropDownField<String> valueOfActivity;

    protected DropDownField<Integer> year;

    protected DropDownField<Integer> startingYear;

    protected DropDownField<Integer> completitionYear;

    protected CheckBoxField CoFinancingTransactionsOnly;

    protected CheckBoxField CPAOnly;

    protected CheckBoxField showRelatedBudgetCodes;

    protected RadioChoiceField<String> pricesCurrency;

    protected CustomReportsModel customReportsModel;

    public CustomReportsPage () {
        form = new Form("form");

        customReportsModel = new CustomReportsModel();
        CompoundPropertyModel<CustomReportsModel> model = new CompoundPropertyModel<>(customReportsModel);
        form.setModel(model);

        Label info = new Label("info", new StringResourceModel("reports.info", this, null, null));
        add(info);

        geography = new DropDownField<>("geography",
                new RWComponentPropertyModel<Category>("geography"), categoryFactory.getUsedGeographyProvider());

        recipient = new DropDownField<>("recipient",
                new RWComponentPropertyModel<Area>("recipient"), usedAreaProvider);

        nationalInstitution = new DropDownField<>("nationalInstitution",
                new RWComponentPropertyModel<Organization>("nationalInstitution"), usedOrganizationProvider);

        multilateralAgency = new DropDownField<>("multilateralAgency",
                new RWComponentPropertyModel<ChannelCategory>("multilateralAgency"), (ChoiceProvider) categoryFactory.getUsedChannelProvider());

        typeOfFlowBiMulti = new DropDownField<>("typeOfFlowbiMulti",
                new RWComponentPropertyModel<Category>("typeOfFlowbiMulti"), categoryFactory.getUsedTypeOfFlowBiMultiProvider());

        sector = new DropDownField<>("sector", new RWComponentPropertyModel<Category>("sector"),
                categoryFactory.getUsedSectorProvider());

        // there are only 2 predefined options for this field
        // and we take them with StringResourceModel
        List<String> typeOfExpenditureOptions = new ArrayList<>();
        typeOfExpenditureOptions.add(new StringResourceModel("commitment", this, null, null).getObject());
        typeOfExpenditureOptions.add(new StringResourceModel("disbursement", this, null, null).getObject());
        typeOfExpenditure = new DropDownField<>("typeOfExpenditure", new RWComponentPropertyModel<String>("typeOfExpenditure"),
                new PredefinedStringProvider(typeOfExpenditureOptions));
        customReportsModel.setTypeOfExpenditure(new StringResourceModel("disbursement", this, null, null).getObject());

        year = new DropDownField<>("year", new RWComponentPropertyModel<Integer>("year"),
                new YearProvider(txService.findDistinctReportingYears()));

        startingYear = new DropDownField<>("startingYear", new RWComponentPropertyModel<Integer>("startingYear"),
                new YearProvider(txService.findDistinctStartingYears()));

        completitionYear = new DropDownField<>("completitionYear", new RWComponentPropertyModel<Integer>("completitionYear"),
                new YearProvider(txService.findDistinctCompletitionYears()));

        List<String> valueOfActivityOptions = new ArrayList<>();
        valueOfActivityOptions.add(new StringResourceModel("allAmount", this, null, null).getObject());
        valueOfActivityOptions.add(new StringResourceModel("lowerThanAmount", this, null, null).getObject());
        valueOfActivityOptions.add(new StringResourceModel("moreThanAmount", this, null, null).getObject());
        valueOfActivity = new DropDownField<>("valueOfActivity", new RWComponentPropertyModel<String>("valueOfActivity"),
                new PredefinedStringProvider(valueOfActivityOptions));
        customReportsModel.setValueOfActivity(new StringResourceModel("allAmount", this, null, null).getObject());

        CoFinancingTransactionsOnly = new CheckBoxField("cofinancingtransactionsonly", new RWComponentPropertyModel<Boolean>("coFinancingTransactionsOnly"));
        CPAOnly = new CheckBoxField("cpaonly", new RWComponentPropertyModel<Boolean>("CPAOnly"));
        humanitarianAid = new CheckBoxField("humanitarianAid", new RWComponentPropertyModel<Boolean>("humanitarianAid"));
        showRelatedBudgetCodes = new CheckBoxField("showRelatedBudgetCodes", new RWComponentPropertyModel<Boolean>("showRelatedBudgetCodes"));

        List<String> currencyTypes = Arrays
                .asList(new String[]{
                        new StringResourceModel("pricesNationalCurrency", this, null, null).getObject(),
                        new StringResourceModel("pricesUSDCurrency", this, null, null).getObject()
                });
        pricesCurrency = new RadioChoiceField("pricesCurrency", new RWComponentPropertyModel<String>("pricesCurrency"), currencyTypes);
        pricesCurrency.getField().setChoices(currencyTypes);
        // select first National Currency
        customReportsModel.setPricesCurrency(new StringResourceModel("pricesNationalCurrency", this, null, null).getObject());

        typeOfFlowValidator = new IFormValidator() {
            @Override
            public FormComponent<Category>[] getDependentFormComponents() {
                List<FormComponent<Category>> list = new ArrayList<FormComponent<Category>>();
                list.add(CustomReportsPage.this.typeOfFlowBiMulti.getField());

                return list.toArray(new FormComponent[0]);
            }

            @Override
            public void validate(Form<?> form) {
                FormComponent<Category>[] components = this.getDependentFormComponents();
                Select2Choice<Category> typeOfFlowComp = (Select2Choice<Category>) components[0];

                if (typeOfFlowComp.getModelObject() == null) {
                    ValidationError error = new ValidationError();
                    error.addKey("typeOfFlowbiMulti.error");
                    typeOfFlowComp.error(error);
                }
            }
        };

        geography.getField().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Category targetValue = geography.getField().getModel().getObject();

                // set recipient model as null in order to reset the dropdown values
                customReportsModel.setRecipient(null);

                if (targetValue != null) {
                    usedAreaProvider.setGeography(targetValue.getName());
                } else {
                    usedAreaProvider.setGeography(null);
                }

                target.add(recipient);
            }
        });

        typeOfFlowBiMulti.getField().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Category typeOfFlow = typeOfFlowBiMulti.getField().getModel().getObject();

                if (typeOfFlow != null) {
                    // the flow is bilateral
                    if (typeOfFlow.getCode().equals(CategoryConstants.BiMultilateral.BI_MULTILATERAL_1)) {
                        customReportsModel.setMultilateralAgency(null);
                        multilateralAgency.setEnabled(Boolean.FALSE);
                    } else {
                        // the flow is multilateral
                        if (typeOfFlow.getCode().equals(CategoryConstants.BiMultilateral.BI_MULTILATERAL_2)) {
                            multilateralAgency.setEnabled(Boolean.TRUE);
                        }
                    }
                } else {
                    multilateralAgency.setEnabled(Boolean.TRUE);
                }

                target.add(multilateralAgency);
            }
        });

        multilateralAgency.getField().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Category channel = multilateralAgency.getField().getModel().getObject();

                if (channel != null) {
                    customReportsModel.setTypeOfFlowbiMulti(catDao.findByCode("BI_MULTILATERAL##2").get(0));
                } else {
                    // do nothing;
                }

                target.add(typeOfFlowBiMulti);
            }
        });

        // Needed for Ajax to update it
        recipient.setOutputMarkupId(true);
        recipient.setRenderBodyOnly(false);

        multilateralAgency.setOutputMarkupId(true);
        multilateralAgency.setRenderBodyOnly(false);

        typeOfFlowBiMulti.setOutputMarkupId(true);
        typeOfFlowBiMulti.setRenderBodyOnly(false);

        form.add(typeOfFlowValidator);
        form.add(geography);
        form.add(recipient);
        form.add(nationalInstitution);
        form.add(multilateralAgency);
        form.add(typeOfFlowBiMulti);
        form.add(sector);
        form.add(year);
        form.add(typeOfExpenditure);
        form.add(startingYear);
        form.add(completitionYear);
        form.add(valueOfActivity);
        form.add(CoFinancingTransactionsOnly);
        form.add(CPAOnly);
        form.add(humanitarianAid);
        form.add(showRelatedBudgetCodes);
        form.add(pricesCurrency);

        addSubmitButton();

        add(form);

        // also add the feedback panel
        feedbackPanel = new NotificationPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.hideAfter(Duration.seconds(3));
        add(feedbackPanel);
    }

    protected abstract void addSubmitButton ();

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(FixBootstrapStylesCssResourceReference.INSTANCE));
        response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(ApplicationJavaScript.INSTANCE),
                "footer-container"));
    }
}
