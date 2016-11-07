/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.tabs;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.projects.common.entities.ProjectReport;
import org.devgateway.eudevfin.projects.module.components.panels.ReportsTableListPanel;
import org.devgateway.eudevfin.projects.module.components.util.ProjectReportsListGenerator;
import org.devgateway.eudevfin.projects.module.modals.ReportsTableModal;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.PreviewableFormPanel;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;

/**
 *
 * @author alcr
 */
public class ReportingTab extends PreviewableFormPanel implements PermissionAwareComponent {
    public static final String KEY = "tabs.reporting";
    public static final String WICKETID_LIST_PANEL = "listPanel";
    private ReportsTableListPanel reportTableListPanel;

    public ReportingTab(String id, PageParameters parameters) {
        super(id);
        
        Label tabDescription = new Label("reportsDescription", new StringResourceModel("reportsDescription", this, null, null));
        add(tabDescription);

        reportTableListPanel = new ReportsTableListPanel(WICKETID_LIST_PANEL, new ProjectReportsListGenerator(NewProjectPage.project.getProjectReports()));
        add(reportTableListPanel);

        final ModalWindow modal = AddModalWindow(parameters);
        add(modal);

        BootstrapSubmitButton submitButton = new BootstrapSubmitButton("addReport", new StringResourceModel("button.addReport", this, null)) {
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
    }

    private ModalWindow AddModalWindow(PageParameters parameters) {
        final ModalWindow modal = new ModalWindow("modal");
        modal.setCookieName("modal-1");

        final PageParameters myParams = parameters;

        modal.setPageCreator(new ModalWindow.PageCreator() {
            @Override
            public org.apache.wicket.Page createPage() {
                return new ReportsTableModal(myParams, ReportingTab.this.getPage().getPageReference(), modal);
            }
        });
        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                ReportsTableListPanel newComp = new ReportsTableListPanel(WICKETID_LIST_PANEL, new ProjectReportsListGenerator(NewProjectPage.project.getProjectReports()));
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

    private List<ProjectReport> getList() {
        List<ProjectReport> results = new ArrayList<>();
        results.addAll(NewProjectPage.project.getProjectReports());
        return results;
    }
}
