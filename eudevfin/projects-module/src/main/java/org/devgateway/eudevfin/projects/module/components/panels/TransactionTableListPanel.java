/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.exchange.common.service.ExchangeRateUtil;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import static org.devgateway.eudevfin.projects.module.components.tabs.ResultsTab.WICKETID_LIST_PANEL;
import org.devgateway.eudevfin.projects.module.components.util.ProjectTransactionsListGenerator;
import org.devgateway.eudevfin.projects.module.components.util.RateUtil;
import org.devgateway.eudevfin.projects.module.modals.TransactionsTableModal;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.ui.common.components.BootstrapDeleteButton;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author alcr
 */
public class TransactionTableListPanel extends TableListPanel<FinancialTransaction> {
    private PageParameters pageParameters;
    private ListGeneratorInterface<FinancialTransaction> listGenerator;
    
    @SpringBean
    private ExchangeRateUtil rateUtil;

    public TransactionTableListPanel(String id, ListGeneratorInterface<FinancialTransaction> listGen) {
        super(id, listGen);
        listGenerator = listGen;
    }

    @Override
    protected void populateTable() {
        final ModalWindow modal = AddModalWindow(null);
//        refreshItems();
        this.itemsListView = new ListView<FinancialTransaction>("projectTransactionsList", items) {

            private static final long serialVersionUID = -8758662617501215830L;

            @Override
            protected void populateItem(ListItem<FinancialTransaction> listItem) {
                final FinancialTransaction transaction = listItem.getModelObject();

                AjaxLink linkToEdit = new AjaxLink("linkToEditTransaction") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        PageParameters parameters = new PageParameters().add(TransactionsTableModal.PARAM_TRANSACTION_ID, transaction.getId());
                        setParameters(parameters);
                        AddModalWindow(parameters);
                        modal.show(target);
                    }
                };
                linkToEdit.setBody(Model.of(transaction.getShortDescription()));
                String CRSId = transaction.getCrsIdentificationNumber() == null ? "" : transaction.getCrsIdentificationNumber();
                Label CRSIdLabel = new Label("CRSId", CRSId);
                String geographicName = transaction.getRecipient() == null ? "" : transaction.getRecipient().getName();
                Label geographicFocusLabel = new Label("geographicFocus", geographicName);
                String agencyName = transaction.getExtendingAgency() == null ? "" : transaction.getExtendingAgency().getName();
                Label financingInstitutionLabel = new Label("financingInstitution", agencyName);
                Label reportingYearLabel = new Label("reportingYear", transaction.getReportingYear().getYear());

                final CustomFinancialTransaction ctx = (CustomFinancialTransaction) transaction;
                Label amountUSD = new Label("amountUSD", RateUtil.moneyToString(rateUtil.exchange(transaction.getAmountsExtended(), CurrencyUnit.USD, 
                            ctx.getFixedRate(), RateUtil.getStartOfMonth(ctx.getCommitmentDate()))));
                Label amountRON = new Label("amountRON", transaction.getAmountsExtended().getAmount().toString());
                Label amountEUR = new Label("amountEUR", RateUtil.moneyToString(rateUtil.exchange(transaction.getAmountsExtended(), CurrencyUnit.EUR, 
                            ctx.getFixedRate(), RateUtil.getStartOfMonth(ctx.getCommitmentDate()))));
                
                BootstrapDeleteButton delete = new BootstrapDeleteButton("delete", new StringResourceModel("delete", this, null)) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        NewProjectPage.project.removeTransaction(transaction);
                        TransactionTableListPanel newComp = new TransactionTableListPanel(WICKETID_LIST_PANEL, 
                            new ProjectTransactionsListGenerator(NewProjectPage.project.getProjectTransactions()));
                        newComp.add(new AttributeAppender("class", "budget-table"));
                        TransactionTableListPanel.this.replaceWith(newComp);
                        target.add(newComp);
                    }
                };
                
                delete.add(AttributeModifier.remove("class"));
                delete.add(AttributeModifier.prepend("class", "round_delete"));
              
                listItem.add(linkToEdit);
                listItem.add(CRSIdLabel);
                listItem.add(geographicFocusLabel);
                listItem.add(financingInstitutionLabel);
                listItem.add(reportingYearLabel);
                listItem.add(amountUSD);
                listItem.add(amountRON);
                listItem.add(amountEUR);
                listItem.add(delete);
            }
        };
        itemsListView.setOutputMarkupId(true);
        this.add(modal);
        this.add(itemsListView);

    }

    private ModalWindow AddModalWindow(PageParameters parameters) {

        if (parameters == null) {
            parameters = new PageParameters();
        }

        final ModalWindow modal = new ModalWindow("modal");

        modal.setCookieName("modal-1");
        modal.setPageCreator(new ModalWindow.PageCreator() {
            @Override
            public org.apache.wicket.Page createPage() {
                return new TransactionsTableModal(getParameters(), TransactionTableListPanel.this.getPage().getPageReference(), modal);
            }
        });
        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                TransactionTableListPanel newComp = new TransactionTableListPanel(WICKETID_LIST_PANEL, 
                        new ProjectTransactionsListGenerator(NewProjectPage.project.getProjectTransactions()));
                newComp.add(new AttributeAppender("class", "budget-table"));
                getParent().replace(newComp);
                target.add(newComp);
            }
        });
        modal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                return true;
            }
        });

        return modal;
    }

    public PageParameters getParameters() {
        return pageParameters;
    }

    public void setParameters(PageParameters params) {
        this.pageParameters = params;
    }
}
