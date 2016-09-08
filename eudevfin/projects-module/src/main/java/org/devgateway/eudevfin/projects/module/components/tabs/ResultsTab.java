/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.tabs;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.projects.common.entities.ProjectResult;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanel;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.projects.module.components.util.ProjectResultsListGenerator;
import org.devgateway.eudevfin.projects.module.components.panels.ResultsTableListPanel;
import org.devgateway.eudevfin.projects.module.modals.ResultsTableModal;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;

/**
 *
 * @author alcr
 */
public class ResultsTab extends PreviewableFormPanel implements PermissionAwareComponent {

    public static final String KEY = "tabs.results";
    public static final String WICKETID_LIST_PANEL = "listPanel";

    private ResultsTableListPanel resultTableListPanel;

    public ResultsTab(String id, PageParameters parameters) {
        super(id);

        Label tabDescription = new Label("resultsDescription", new StringResourceModel("resultsDescription", this, null, null));
        add(tabDescription);
        
        resultTableListPanel = new ResultsTableListPanel(WICKETID_LIST_PANEL, new ProjectResultsListGenerator(NewProjectPage.project.getProjectResults()));
        add(resultTableListPanel);

        final ModalWindow modal = AddModalWindow(parameters);
        add(modal);

        BootstrapSubmitButton submitButton = new BootstrapSubmitButton("addResult", new StringResourceModel("button.addResult", this, null)) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                modal.show(target);
            }
        };

        MetaDataRoleAuthorizationStrategy.authorize(submitButton, Component.ENABLE, AuthConstants.Roles.ROLE_PROJECTS_MFA);
        MetaDataRoleAuthorizationStrategy.authorize(submitButton, Component.ENABLE, AuthConstants.Roles.ROLE_SUPERVISOR);
        submitButton.setDefaultFormProcessing(false);
        add(submitButton);
    }

    private ModalWindow AddModalWindow(PageParameters parameters) {
        final ModalWindow modal = new ModalWindow("modal");
        modal.setCookieName("modal-1");

        final PageParameters myParams = parameters;

        modal.setPageCreator(new ModalWindow.PageCreator() {
            @Override
            public org.apache.wicket.Page createPage() {
                return new ResultsTableModal(myParams, ResultsTab.this.getPage().getPageReference(), modal);
            }
        });
        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                ResultsTableListPanel newComp = new ResultsTableListPanel(WICKETID_LIST_PANEL, new ProjectResultsListGenerator(NewProjectPage.project.getProjectResults()));
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

    private List<ProjectResult> getList() {
        List<ProjectResult> results = new ArrayList<>();
        results.addAll(NewProjectPage.project.getProjectResults());
        return results;
    }
}
