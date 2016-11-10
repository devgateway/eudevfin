/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.modals;

import java.util.Iterator;
import java.util.Set;
import org.apache.wicket.Component;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.projects.common.entities.ProjectResult;
import org.devgateway.eudevfin.projects.module.validator.WordsValidator;
import org.devgateway.eudevfin.projects.module.components.util.ProjectUtil;
import org.devgateway.eudevfin.projects.module.pages.ModalHeaderFooter;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.projects.module.providers.StatusProvider;
import org.devgateway.eudevfin.projects.service.ProjectResultService;
import org.devgateway.eudevfin.projects.service.ProjectService;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TextAreaInputField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;

/**
 *
 * @author alcr
 */
public class ResultsTableModal extends ModalHeaderFooter {

    public static final String PARAM_RESULT_ID = "resultId";
    public static final String PARAM_NEW_VALUE = "new";
    public static final String PARAM_PROJECT_ID = "projectId";
    public static int MAX_STRING_LENGTH = 8000;
    public static int MAX_WORDS = 1000;

    @SpringBean
    private ProjectResultService projectResultService;

    @SpringBean
    private ProjectService projectService;

    public ResultsTableModal(final PageParameters parameters, final PageReference modalWindowPage, final ModalWindow window) {
        final ProjectResult result = getEditableResult(parameters);
        CompoundPropertyModel<ProjectResult> model = new CompoundPropertyModel<>(result);
        setModel(model);

        Form<ProjectResult> form = new Form<>("form");

        TextInputField<String> plannedResult = new TextInputField<>("1plannedResult", new RWComponentPropertyModel<String>(
                "result")).required().typeString();
        MetaDataRoleAuthorizationStrategy.authorize(plannedResult, Component.ENABLE, AuthConstants.Roles.ROLE_PROJECTS_MFA);
        MetaDataRoleAuthorizationStrategy.authorize(plannedResult, Component.ENABLE, AuthConstants.Roles.ROLE_SUPERVISOR);
        plannedResult.getField().add(new WordsValidator(100));

        StatusProvider statusProvider = new StatusProvider(this);
        DropDownField<String> projectStatus = new DropDownField<>("status", new RWComponentPropertyModel<String>("status"), statusProvider);
        MetaDataRoleAuthorizationStrategy.authorize(projectStatus, Component.ENABLE, AuthConstants.Roles.ROLE_PROJECTS_MFA);
        MetaDataRoleAuthorizationStrategy.authorize(projectStatus, Component.ENABLE, AuthConstants.Roles.ROLE_PROJECTS_NGO);
        MetaDataRoleAuthorizationStrategy.authorize(projectStatus, Component.ENABLE, AuthConstants.Roles.ROLE_SUPERVISOR);
        projectStatus.required();
        projectStatus.setVisible(true);

        final TextAreaInputField description = new TextAreaInputField("3description", new RWComponentPropertyModel<String>("description"));
        MetaDataRoleAuthorizationStrategy.authorize(description, Component.ENABLE, AuthConstants.Roles.ROLE_PROJECTS_MFA);
        MetaDataRoleAuthorizationStrategy.authorize(description, Component.ENABLE, AuthConstants.Roles.ROLE_PROJECTS_NGO);
        MetaDataRoleAuthorizationStrategy.authorize(description, Component.ENABLE, AuthConstants.Roles.ROLE_SUPERVISOR);
        description.setRows(ProjectUtil.MAX_AREA_ROWS);
        description.setOutputMarkupId(true);
        description.maxContentLength(MAX_STRING_LENGTH);
        description.required();
        description.getField().add(new WordsValidator(MAX_WORDS));

        form.add(plannedResult);
        form.add(projectStatus);
        form.add(description);

        form.add(new BootstrapSubmitButton("submit", new StringResourceModel("button.submit", this, null)) {

            private static final long serialVersionUID = -7833958712063599191L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                replaceResult(result);
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

    public ProjectResult getEditableResult(PageParameters parameters) {
        Long resultId = null;

        if (parameters.get(PARAM_PROJECT_ID).isNull() && parameters.get(PARAM_RESULT_ID).isNull()) {
            parameters.add(PARAM_RESULT_ID, PARAM_NEW_VALUE);
        }

        if (!parameters.get(PARAM_RESULT_ID).isNull()) {
            ProjectResult result = null;
            if (PARAM_NEW_VALUE.equals(parameters.get(PARAM_RESULT_ID).toString())) {
                result = new ProjectResult();
            } else {
                resultId = parameters.get(PARAM_RESULT_ID).toLong();
                result = findOne(resultId);
            }
            return result;
        } else {
            return new ProjectResult();
        }
    }

    private void replaceResult(ProjectResult result) {
        if (NewProjectPage.project == null) {
            return;
        }

        Set<ProjectResult> results = NewProjectPage.project.getProjectResults();

        if (result.getId() != null) {
            Iterator iterator = results.iterator();
            while (iterator.hasNext()) {
                ProjectResult temp = (ProjectResult) iterator.next();

                if (temp.getId() == result.getId()) {
                    iterator.remove();
                }
            }
        } else {
            result.setId(generateId(NewProjectPage.project.getProjectResults()));
        }

        NewProjectPage.project.addResult(result);
    }

    private ProjectResult findOne(Long resultId) {
        Set<ProjectResult> results = NewProjectPage.project.getProjectResults();
        for (ProjectResult temp : results) {
            if (resultId == temp.getId()) {
                return temp;
            }
        }
        return null;
    }

    private long generateId(Set<ProjectResult> results) {
        if (results.isEmpty()) {
            return 1;
        }

        long id = Long.MIN_VALUE;
        for (ProjectResult result : results) {
            if (result.getId() != null && result.getId() > id) {
                id = result.getId();
            }
        }

        return ++id;
    }
}
