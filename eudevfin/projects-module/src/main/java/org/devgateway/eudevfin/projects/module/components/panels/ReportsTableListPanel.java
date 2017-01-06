/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.projects.common.entities.ProjectReport;
import static org.devgateway.eudevfin.projects.module.components.tabs.ReportingTab.WICKETID_LIST_PANEL;
import org.devgateway.eudevfin.projects.module.components.util.ProjectReportsListGenerator;
import org.devgateway.eudevfin.projects.module.modals.ReportsTableModal;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.joda.time.LocalDate;

/**
 *
 * @author alcr
 */
public class ReportsTableListPanel extends TableListPanel<ProjectReport> {

    private PageParameters pageParameters;
    private ListGeneratorInterface<ProjectReport> listGenerator;

    public ReportsTableListPanel(String id, ListGeneratorInterface<ProjectReport> listGen) {
        super(id, listGen);
        listGenerator = listGen;
    }

    @Override
    protected void populateTable() {
        final ModalWindow modal = AddModalWindow(null);

        this.itemsListView = new ListView<ProjectReport>("projectReportsList", items) {

            private static final long serialVersionUID = -8758662617501215830L;

            @Override
            protected void populateItem(ListItem<ProjectReport> listItem) {
                final ProjectReport report = listItem.getModelObject();

                AjaxLink linkToEdit = new AjaxLink("linkToEditReport") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        PageParameters parameters = new PageParameters().add(ReportsTableModal.PARAM_REPORT_ID, report.getId());
                        setParameters(parameters);
                        AddModalWindow(parameters);
                        modal.show(target);
                    }
                };
                
                Label reportTitleLabel = new Label("reportTitle", report.getReportTitle());

                String reportTypeName = new StringResourceModel(report.getType(), ReportsTableListPanel.this, null).getString();
                linkToEdit.setBody(Model.of(reportTypeName));
                LocalDate date = report.getFormattedReportDate() == null ? new LocalDate() : report.getFormattedReportDate();
                Label reportDateLabel = new Label("reportDate", date);
                
                LocalDate reportingPeriodStart = report.getFormattedReportingPeriodStart()== null ? new LocalDate() : report.getFormattedReportingPeriodStart();
                Label reportingPeriodStartLabel = new Label("reportingPeriodStart", reportingPeriodStart);
                
                LocalDate reportingPeriodEnd = report.getFormattedReportingPeriodEnd() == null ? new LocalDate() : report.getFormattedReportingPeriodEnd();
                Label reportingPeriodEndLabel = new Label("reportingPeriodEnd", reportingPeriodEnd);
                
                Label fileProvidedLabel = new Label("fileProvided", report.getFileProvided());
                Label fileNameLabel = new Label("reportFile", report.getReportFiles().toString());

                listItem.add(linkToEdit);
                listItem.add(reportTitleLabel);
                listItem.add(reportDateLabel);
                listItem.add(reportingPeriodStartLabel);
                listItem.add(reportingPeriodEndLabel);
                listItem.add(fileProvidedLabel);
                listItem.add(fileNameLabel);
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
                return new ReportsTableModal(getParameters(), ReportsTableListPanel.this.getPage().getPageReference(), modal);
            }
        });
        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                ReportsTableListPanel newComp = new ReportsTableListPanel(WICKETID_LIST_PANEL, new ProjectReportsListGenerator(NewProjectPage.project.getProjectReports()));
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
