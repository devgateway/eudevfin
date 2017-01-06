/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.tabs;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.projects.common.entities.ProjectFileWrapper;
import org.devgateway.eudevfin.projects.module.components.fields.MultiProjectFileUploadField;
import org.devgateway.eudevfin.projects.module.components.panels.TransactionTableListPanel;
import org.devgateway.eudevfin.projects.module.components.util.ProjectTransactionsListGenerator;
import org.devgateway.eudevfin.projects.module.modals.TransactionsTableModal;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanel;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

/**
 *
 * @author alcr
 */
public class TransactionsTab extends PreviewableFormPanel implements PermissionAwareComponent {

    public static final String KEY = "tabs.transactions";
    public static final String WICKETID_LIST_PANEL = "listPanel";
    private TransactionTableListPanel transactionTableListPanel;

    private static final Logger logger = Logger.getLogger(ResultsTab.class);

    public TransactionsTab(String id, PageParameters parameters) {
        super(id);
        
        Label tabDescription = new Label("budgetDescription", new StringResourceModel("budgetDescription", this, null, null));
        add(tabDescription);

        transactionTableListPanel = new TransactionTableListPanel(WICKETID_LIST_PANEL, new ProjectTransactionsListGenerator(NewProjectPage.project.getProjectTransactions()));
        transactionTableListPanel.add(new AttributeAppender("class", "budget-table"));
        add(transactionTableListPanel);

        final ModalWindow modal = AddModalWindow(parameters);
        add(modal);

        BootstrapSubmitButton submitButton = new BootstrapSubmitButton("addTransaction", new StringResourceModel("button.addTransaction", this, null)) {
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                modal.show(target);
            }
        };
        submitButton.setDefaultFormProcessing(false);
        add(submitButton);  
        
        //todo: cast to transaction file wrapper
        MultiProjectFileUploadField transactionDocumentation = new MultiProjectFileUploadField("transactionDocumentation", new RWComponentPropertyModel<Collection<ProjectFileWrapper>>("transactionDocumentation"));
        transactionDocumentation.maxFiles(10);
        transactionDocumentation.add(new AttributeAppender("class", "remove-left-margin"));
        add(transactionDocumentation);
    }

    private ModalWindow AddModalWindow(PageParameters parameters) {
        final ModalWindow modal = new ModalWindow("modal");
        modal.setCookieName("modal-1");

        final PageParameters myParams = parameters;

        modal.setPageCreator(new ModalWindow.PageCreator() {
            @Override
            public org.apache.wicket.Page createPage() {
                //ResultsTab.this.getPage().getPageReference()
                return new TransactionsTableModal(myParams, TransactionsTab.this.getPage().getPageReference(), modal);
            }
        });
        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                TransactionTableListPanel newComp = new TransactionTableListPanel(WICKETID_LIST_PANEL, new ProjectTransactionsListGenerator(NewProjectPage.project.getProjectTransactions()));
                newComp.add(new AttributeAppender("class", "budget-table"));
                replace(newComp);
                target.add(newComp);
            }
        });
        modal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                // Change the passValue variable when modal window is closed.
                //setPassValue("Modal window is closed by user.");
                return true;
            }
        });

        return modal;
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }
}
