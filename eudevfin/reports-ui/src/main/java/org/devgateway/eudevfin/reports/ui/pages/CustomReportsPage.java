package org.devgateway.eudevfin.reports.ui.pages;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Select2Choice;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
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
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.CheckBoxField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.PredefinedStringProvider;
import org.devgateway.eudevfin.ui.common.providers.UsedAreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.UsedOrganizationChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.YearProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author idobre
 * @since 4/1/14
 */

@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public abstract class CustomReportsPage extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(CustomReportsPage.class);

    @SpringBean
    private UsedAreaChoiceProvider usedAreaProvider;

    @SpringBean
    private CategoryProviderFactory categoryFactory;

    @SpringBean
    private UsedOrganizationChoiceProvider usedOrganizationProvider;

    @SpringBean
    private CustomFinancialTransactionService txService;

    protected final NotificationPanel feedbackPanel;

    protected IFormValidator geographyValidator;

    protected final Form form;

    protected CheckBoxField humanitarianAid;

    protected DropDownField<Category> geography;

    protected DropDownField<Area> recipient;

    protected DropDownField<Organization> nationalInstitution;

    protected DropDownField<ChannelCategory> multilateralAgency;

    protected DropDownField<Category> typeOfFlowBiMulti;

    protected DropDownField<Category> typeOfAid;

    protected DropDownField<Category> sector;

    protected DropDownField<String> typeOfExpenditure;

    protected DropDownField<String> valueOfActivity;

    protected DropDownField<Integer> year;

    protected DropDownField<Integer> startingYear;

    protected DropDownField<Integer> completitionYear;

    protected CheckBoxField CoFinancingTransactionsOnly;

    protected CheckBoxField CPAOnly;

    protected CheckBoxField showRelatedBudgetCodes;

    protected CheckBoxField pricesNationalCurrency;

    protected CheckBoxField pricesEURCurrency;

    protected CustomReportsModel customReportsModel;

    public CustomReportsPage () {
        form = new Form("form");

        customReportsModel = new CustomReportsModel();
        CompoundPropertyModel<CustomReportsModel> model = new CompoundPropertyModel<>(customReportsModel);
        form.setModel(model);

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

        typeOfAid = new DropDownField<>("typeOfAid",
                new RWComponentPropertyModel<Category>("typeOfAid"), categoryFactory.getUsedTypeOfAidProvider());

        sector = new DropDownField<>("sector", new RWComponentPropertyModel<Category>("sector"),
                categoryFactory.getUsedSectorProvider());

        // there are only 2 predefined options for this field
        // and we take them with StringResourceModel
        List<String> typeOfExpenditureOptions = new ArrayList<>();
        typeOfExpenditureOptions.add(new StringResourceModel("commitment", this, null, null).getObject());
        typeOfExpenditureOptions.add(new StringResourceModel("disbursement", this, null, null).getObject());
        typeOfExpenditure = new DropDownField<>("typeOfExpenditure", new RWComponentPropertyModel<String>("typeOfExpenditure"),
                new PredefinedStringProvider(typeOfExpenditureOptions));

        year = new DropDownField<>("year", new RWComponentPropertyModel<Integer>("year"),
                new YearProvider(txService.findDistinctReportingYears()));

        startingYear = new DropDownField<>("startingYear", new RWComponentPropertyModel<Integer>("startingYear"),
                new YearProvider(txService.findDistinctStartingYears()));

        completitionYear = new DropDownField<>("completitionYear", new RWComponentPropertyModel<Integer>("completitionYear"),
                new YearProvider(txService.findDistinctCompletitionYears()));

        List<String> valueOfActivityOptions = new ArrayList<>();
        valueOfActivityOptions.add(new StringResourceModel("lowerThanAmount", this, null, null).getObject());
        valueOfActivityOptions.add(new StringResourceModel("moreThanAmount", this, null, null).getObject());
        valueOfActivity = new DropDownField<>("valueOfActivity", new RWComponentPropertyModel<String>("valueOfActivity"),
                new PredefinedStringProvider(valueOfActivityOptions));

        // TODO fields
        // humanitarian - which is a sector
        // show related budget codes?

        CoFinancingTransactionsOnly = new CheckBoxField("cofinancingtransactionsonly", new RWComponentPropertyModel<Boolean>("coFinancingTransactionsOnly"));
        CPAOnly = new CheckBoxField("cpaonly", new RWComponentPropertyModel<Boolean>("CPAOnly"));
        humanitarianAid = new CheckBoxField("humanitarianAid", new RWComponentPropertyModel<Boolean>("humanitarianAid"));
        showRelatedBudgetCodes = new CheckBoxField("showRelatedBudgetCodes", new RWComponentPropertyModel<Boolean>("showRelatedBudgetCodes"));

        Label choiceOfCurrency = new Label("choiceOfCurrency", new StringResourceModel("choiceOfCurrency", this, null, null));
        choiceOfCurrency.setVisibilityAllowed(Boolean.FALSE);

        pricesNationalCurrency = new CheckBoxField("pricesNationalCurrency", new RWComponentPropertyModel<Boolean>("pricesNationalCurrency"));
        pricesEURCurrency = new CheckBoxField("pricesEURCurrency", new RWComponentPropertyModel<Boolean>("pricesEURCurrency"));

        // add geography&recipient validator
        // (ODAEU-238) a country could not be selected if I want to have only regional aggregates
        geographyValidator = new IFormValidator() {
            @Override
            public FormComponent<Category>[] getDependentFormComponents() {
                List<FormComponent<Category>> list = new ArrayList<FormComponent<Category>>();
                list.add(CustomReportsPage.this.geography.getField());

                return list.toArray(new FormComponent[0]);
            }

            @Override
            public void validate(Form<?> form) {
                FormComponent<Category>[] components = this.getDependentFormComponents();
                Select2Choice<Category> geographyComp = (Select2Choice<Category>) components[0];

                Area recipient = ((CustomReportsModel) form.getInnermostModel().getObject()).getRecipient();

                if (geographyComp.getModelObject() != null && recipient != null) {
                    ValidationError error = new ValidationError();
                    error.addKey("geography.error");
                    geographyComp.error(error);
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
                    usedAreaProvider.setGeography(geography.getField().getModel().getObject().getName());
                } else {
                    usedAreaProvider.setGeography(null);
                }

                target.add(recipient);
            }
        });

        // Needed for Ajax to update it
        recipient.setOutputMarkupId(true);
        recipient.setRenderBodyOnly(false);

        // form.add(geographyValidator);
        form.add(geography);
        form.add(recipient);
        form.add(nationalInstitution);
        form.add(multilateralAgency);
        form.add(typeOfFlowBiMulti);
        form.add(typeOfAid);
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
        form.add(choiceOfCurrency);
        form.add(pricesNationalCurrency);
        form.add(pricesEURCurrency);

        addSubmitButton();

        add(form);

        // also add the feedback panel
        feedbackPanel = new NotificationPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.hideAfter(Duration.seconds(3));
        add(feedbackPanel);
    }

    protected abstract void addSubmitButton ();
}
