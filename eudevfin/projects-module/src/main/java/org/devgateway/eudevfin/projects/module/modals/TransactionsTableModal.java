/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.modals;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Predicate;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.service.AreaService;
import org.devgateway.eudevfin.metadata.common.service.OrganizationService;
import org.devgateway.eudevfin.projects.module.components.util.TransactionSearchModel;
import org.devgateway.eudevfin.projects.module.pages.ModalHeaderFooter;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.projects.module.providers.FinancingInstitutionChoiceProvider;
import org.devgateway.eudevfin.projects.module.providers.GeographicFocusChoiceProvider;
import org.devgateway.eudevfin.projects.module.providers.TransactionChoiceProvider;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.events.CurrencyUpdateBehavior;
import org.devgateway.eudevfin.ui.common.models.BigMoneyModel;
import org.devgateway.eudevfin.ui.common.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.providers.YearProvider;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;

/**
 *
 * @author alcr
 */
public class TransactionsTableModal extends ModalHeaderFooter {

    public static final String PARAM_TRANSACTION_ID = "transactionId";
    public static final String PARAM_REPORT_ID_VALUE_NEW = "new";
    public static final String PARAM_PROJECT_ID = "projectId";

    public TextInputField<String> amountUSD;
    public TextInputField<String> amountRON;
    public TextInputField<String> amountEUR;

    @SpringBean
    private CustomFinancialTransactionService customTransactionService;

    @SpringBean
    private OrganizationService organizationService;

    @SpringBean
    private AreaService areaService;

    public TransactionsTableModal(final PageParameters parameters) {
        if (parameters == null) {
            return;
        }
        AddComponents(parameters, null, null);
    }

    public TransactionsTableModal(final PageParameters parameters, final PageReference modalWindowPage, final ModalWindow window) {
        AddComponents(parameters, modalWindowPage, window);
    }

    public final void AddComponents(PageParameters parameters, final PageReference modalWindowPage, final ModalWindow window) {
        final TransactionSearchModel transactionSearch = new TransactionSearchModel();
        CompoundPropertyModel<TransactionSearchModel> model = new CompoundPropertyModel<>(transactionSearch);
        setModel(model);

        final Form<FinancialTransaction> form = new Form<>("form");

        final ComponentPropertyModel<CurrencyUnit> readOnlyCurrencyModel = new ComponentPropertyModel<>("currency");

        // 1. Geographic focus    
        //In the DB, table AreaTranslation is data only  for locale = "en" or "fr"
        //If someday, all the available languages will be added in the DB, the call should be changed to:
        //List<Area> areas = areaService.findUsedAreaAsList(Session.get().getLocale().getLanguage());
        List<Area> areas = areaService.findUsedAreaAsList("en");
        GeographicFocusChoiceProvider geograficFocusProvider = new GeographicFocusChoiceProvider(areas, areaService);
        DropDownField<Area> geographicFocus;
        geographicFocus = new DropDownField<Area>("geographicFocus", new RWComponentPropertyModel<Area>("geographicFocus"), geograficFocusProvider) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);

                if (transactionSearch.getGeographicFocus() != null) {
                    List<Organization> orgs = organizationService.findUsedOrgByGeographicFocus(transactionSearch.getGeographicFocus().getCode());

                    FinancingInstitutionChoiceProvider financingInstitutionProvider = new FinancingInstitutionChoiceProvider(orgs, organizationService);
                    DropDownField<Organization> financingInstitution;
                    financingInstitution = new DropDownField<Organization>("financingInstitution", new RWComponentPropertyModel<Organization>("financingInstitution"), financingInstitutionProvider) {

                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            super.onUpdate(target);

                            List<Integer> repYears = customTransactionService.findDistinctReportingYears();
                            DropDownField<Integer> reportingYear;
                            reportingYear = new DropDownField<Integer>("reportingYear", new YearToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("reportingYear")), new YearProvider(repYears)) {

                                @Override
                                protected void onUpdate(AjaxRequestTarget target) {
                                    super.onUpdate(target);

                                    if (transactionSearch.getGeographicFocus() != null && null != transactionSearch.getFinancingInstitution() && transactionSearch.getReportingYear() != null) {
                                        String geographicFocus = transactionSearch.getGeographicFocus().getCode();
                                        String financingInstitution = transactionSearch.getFinancingInstitution().getCode();
                                        LocalDateTime reportingYear = transactionSearch.getReportingYear();

                                        List<CustomFinancialTransaction> transactions = customTransactionService.findByReportingYearAndDraftFalse(reportingYear.getYear());

                                        List<CustomFinancialTransaction> foundTrs = new ArrayList<>();
                                        for (CustomFinancialTransaction trs : transactions) {
                                            if (trs.getRecipient() != null && trs.getRecipient().getCode() == geographicFocus) {
                                                foundTrs.add(trs);
                                            }
                                        }

                                        DropDownField<CustomFinancialTransaction> trsDropDown = new DropDownField<CustomFinancialTransaction>("projectTitle", new RWComponentPropertyModel<CustomFinancialTransaction>("transaction"), new TransactionChoiceProvider(foundTrs, customTransactionService)) {
                                            @Override
                                            protected void onUpdate(AjaxRequestTarget target) {
                                                super.onUpdate(target);

                                                CustomFinancialTransaction transaction = transactionSearch.getTransaction();

                                                if (transaction != null) {
                                                    CurrencyUnit currency = transaction.getCurrency();
                                                    String amount = transactionSearch.getTransaction().getAmountsExtended().getAmount().toString();

                                                    if (null != currency.toString()) {
                                                        switch (currency.toString()) {
                                                            case "RON":
                                                                amountUSD.getField().getModel().setObject(amount);
                                                                amountRON.getField().getModel().setObject(amount);
                                                                amountEUR.getField().getModel().setObject(amount);
                                                                break;
                                                            case "EUR":
                                                                // ToDo Refactor When We figure it out the exchange rate
                                                                amountUSD.getField().getModel().setObject(amount);
                                                                amountRON.getField().getModel().setObject(amount);
                                                                amountEUR.getField().getModel().setObject(amount);
                                                                break;
                                                            case "USD":
                                                                // ToDo Refactor When We figure it out the exchange rate
                                                                amountUSD.getField().getModel().setObject(amount);
                                                                amountRON.getField().getModel().setObject(amount);
                                                                amountEUR.getField().getModel().setObject(amount);
                                                                break;
                                                        }
                                                    }
                                                    getParent().replace(amountUSD);
                                                    getParent().replace(amountEUR);
                                                    getParent().replace(amountRON);
                                                    target.add(amountUSD);
                                                    target.add(amountEUR);
                                                    target.add(amountRON);
                                                }
                                            }

                                        };
                                        trsDropDown.required();
                                        trsDropDown.getField().setEnabled(true);
                                        getParent().replace(trsDropDown);
                                        target.add(trsDropDown);
                                    }
                                }
                            };
                            reportingYear.setSize(InputBehavior.Size.Medium);
                            reportingYear.getField().setEnabled(true);
                            getParent().replace(reportingYear);
                            target.add(reportingYear);

                        }
                    };
                    financingInstitution.required();
                    financingInstitution.getField().setEnabled(true);
                    getParent().replace(financingInstitution);
                    target.add(financingInstitution);
                }
            }
        };

        // 2. Financing Institution        
        List<Organization> orgs = new ArrayList<>();
        DropDownField<Organization> financingInstitution = new DropDownField<>("financingInstitution", new RWComponentPropertyModel<Organization>("financingInstitution"), new FinancingInstitutionChoiceProvider(orgs, organizationService));
        financingInstitution.required();
        financingInstitution.getField().setEnabled(false);

        // 3. Reporting year
        List<Integer> years = new ArrayList<>();
        DropDownField<Integer> reportingYear = new DropDownField<>("reportingYear", new YearToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("reportingYear")), new YearProvider(years));
        reportingYear.setSize(InputBehavior.Size.Medium);
        reportingYear.getField().setEnabled(false);

        // 4. Project title  
        List<CustomFinancialTransaction> trans = new ArrayList<>();
        DropDownField<CustomFinancialTransaction> projectTitle = new DropDownField<>("projectTitle", new RWComponentPropertyModel<CustomFinancialTransaction>("transaction"), new TransactionChoiceProvider(trans, customTransactionService));
        projectTitle.required();
        projectTitle.getField().setEnabled(false);

        // 5. Amount USD   
        amountUSD = new TextInputField<>("amountUSD", new RWComponentPropertyModel<String>("amountUSD"));
        amountUSD.typeString();
        amountUSD.getField().setEnabled(false);

        // 6. Amount RON   
        amountRON = new TextInputField<>("amountRON", new RWComponentPropertyModel<String>("amountRON"));
        amountRON.typeString();
        amountRON.getField().setEnabled(false);

        // 7. Amount EURO   
        amountEUR = new TextInputField<>("amountEUR", new RWComponentPropertyModel<String>("amountEUR"));
        amountEUR.typeString();
        amountEUR.getField().setEnabled(false);

        form.add(geographicFocus);
        form.add(financingInstitution);
        form.add(reportingYear);
        form.add(projectTitle);
        form.add(amountUSD);
        form.add(amountRON);
        form.add(amountEUR);

        form.add(new BootstrapSubmitButton("submit", new StringResourceModel("button.submit", this, null)) {

            private static final long serialVersionUID = -7833958712063599191L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (transactionSearch.getTransaction() != null) {
                    NewProjectPage.project.addTransactions(transactionSearch.getTransaction());
                }
                if (window != null) {
                    window.close(target);
                }
            }

        });

        BootstrapCancelButton cancelButton = new BootstrapCancelButton("cancel", new StringResourceModel("button.cancel", this, null)) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (window != null) {
                    window.close(target);
                }
            }

        };
        cancelButton.setDefaultFormProcessing(false);
        form.add(cancelButton);

        add(form);
    }
}
