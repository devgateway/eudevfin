/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.modals;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.financial.FileWrapper;
import org.devgateway.eudevfin.projects.common.entities.ProjectReport;
import static org.devgateway.eudevfin.projects.module.modals.ResultsTableModal.PARAM_NEW_VALUE;
import org.devgateway.eudevfin.projects.module.pages.ModalHeaderFooter;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.projects.module.providers.ReportTypeProvider;
import org.devgateway.eudevfin.projects.module.validator.WordsValidator;
import org.devgateway.eudevfin.projects.service.ProjectReportService;
import org.devgateway.eudevfin.projects.service.ProjectService;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.CheckBoxField;
import org.devgateway.eudevfin.ui.common.components.DateInputField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.MultiFileUploadField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.models.DateToLocalDateTimeModel;
import org.joda.time.LocalDateTime;

/**
 *
 * @author alcr
 */
public class ReportsTableModal extends ModalHeaderFooter {

    public static final String PARAM_REPORT_ID = "reportId";
    public static final String PARAM_REPORT_ID_VALUE_NEW = "new";
    public static final String PARAM_PROJECT_ID = "projectId";

    @SpringBean
    private ProjectReportService projectReportService;

    @SpringBean
    private ProjectService projectService;

    public ReportsTableModal(final PageParameters parameters) {
        if (parameters == null) {
            return;
        }
        AddComponents(parameters, null, null);
    }

    public ReportsTableModal(final PageParameters parameters, final PageReference modalWindowPage, final ModalWindow window) {
        AddComponents(parameters, modalWindowPage, window);
    }

    public final void AddComponents(final PageParameters parameters, final PageReference modalWindowPage, final ModalWindow window) {
        final ProjectReport report = getEditableReport(parameters);
        CompoundPropertyModel<ProjectReport> model = new CompoundPropertyModel<>(report);
        setModel(model);

        Form<ProjectReport> form = new Form<>("form");

        ReportTypeProvider typeProvider = new ReportTypeProvider(this);
        DropDownField<String> reportType = new DropDownField<>("1reportType", new RWComponentPropertyModel<String>("type"), typeProvider);
        reportType.required();
        reportType.setVisible(true);
        
        TextInputField<String> reportTitle = new TextInputField<>("2reportTitle", new RWComponentPropertyModel<String>(
                "reportTitle")).required().typeString();       
        reportTitle.getField().add(new WordsValidator(30));
        
        DateInputField reportingPeriodStart = new DateInputField("3reportingPeriodStart", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("reportingPeriodStart")));
        reportingPeriodStart.required();
        
        DateInputField reportingPeriodEnd = new DateInputField("3reportingPeriodEnd", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("reportingPeriodEnd")));
        reportingPeriodEnd.required();

        DateInputField reportDate = new DateInputField("4reportDate", new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("reportDate")));
        reportDate.required();

        CheckBoxField fileProvided = new CheckBoxField("5fileProvided", new RWComponentPropertyModel<Boolean>("fileProvided"));

        MultiFileUploadField reportFiles = new MultiFileUploadField("6reportFiles", new RWComponentPropertyModel<Collection<FileWrapper>>("reportFiles"));
        reportFiles.maxFiles(10);
        add(reportFiles);

        form.add(reportType);
        form.add(reportTitle);
        form.add(reportingPeriodStart);
        form.add(reportingPeriodEnd);
        form.add(reportDate);
        form.add(fileProvided);
        form.add(reportFiles);

        form.add(new BootstrapSubmitButton("submit", new StringResourceModel("button.submit", this, null, null)) {

            private static final long serialVersionUID = -7833958712063599191L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                replaceReport(report);
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

    public ProjectReport getEditableReport(PageParameters parameters) {
        Long resultId = null;

        if (parameters.get(PARAM_PROJECT_ID).isNull() && parameters.get(PARAM_REPORT_ID).isNull()) {
            parameters.add(PARAM_REPORT_ID, PARAM_NEW_VALUE);
        }

        if (!parameters.get(PARAM_REPORT_ID).isNull()) {
            ProjectReport result = null;
            if (PARAM_REPORT_ID_VALUE_NEW.equals(parameters.get(PARAM_REPORT_ID).toString())) {
                result = new ProjectReport();
            } else {
                resultId = parameters.get(PARAM_REPORT_ID).toLong();
                result = findOne(resultId);
            }
            return result;
        } else {
            return new ProjectReport();
        }
    }

    private void replaceReport(ProjectReport report) {
        if (NewProjectPage.project == null) {
            return;
        }

        Set<ProjectReport> reports = NewProjectPage.project.getProjectReports();

        if (report.getId() != null) {
            Iterator iterator = reports.iterator();
            while (iterator.hasNext()) {
                ProjectReport temp = (ProjectReport) iterator.next();
                if (temp.getId() == report.getId()) {
                    iterator.remove();
                }
            }
        } else {
            report.setId(generateId(NewProjectPage.project.getProjectReports()));
        }
        
        NewProjectPage.project.addReport(report);
    }

    private ProjectReport findOne(Long resultId) {
        Set<ProjectReport> results = NewProjectPage.project.getProjectReports();
        for (ProjectReport temp : results) {
            if (resultId == temp.getId()) {
                return temp;
            }
        }
        return null;
    }
    
    private long generateId(Set<ProjectReport> reports) {
        if (reports.isEmpty())
            return 1;
        
        long id = Long.MIN_VALUE;
        for (ProjectReport result : reports) {
            if (result.getId() != null && result.getId() > id) {
                id = result.getId();
            } 
        }
        
        return ++id;
    }
}
