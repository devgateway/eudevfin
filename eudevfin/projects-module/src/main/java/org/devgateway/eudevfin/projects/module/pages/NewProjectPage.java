/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.pages;

import org.devgateway.eudevfin.projects.module.components.tabs.TransactionsTab;
import org.devgateway.eudevfin.projects.module.components.tabs.ResultsTab;
import org.devgateway.eudevfin.projects.module.components.tabs.ReportingTab;
import org.devgateway.eudevfin.projects.module.components.tabs.MonitoringVisibilityTab;
import org.devgateway.eudevfin.projects.module.components.tabs.ObjectivesTab;
import org.devgateway.eudevfin.projects.module.components.tabs.IdentificationDataTab;
import org.devgateway.eudevfin.projects.module.components.tabs.ConstraintsTab;
import org.apache.log4j.Logger;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.visit.IVisit;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.projects.module.components.util.ProjectUtil;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.devgateway.eudevfin.projects.common.entities.ProjectReport;
import org.devgateway.eudevfin.projects.common.entities.ProjectResult;
import org.devgateway.eudevfin.projects.module.components.tabs.CrossCuttingTab;
import org.devgateway.eudevfin.projects.module.components.tabs.ProjectExtensionTab;
import org.devgateway.eudevfin.projects.service.ProjectReportService;
import org.devgateway.eudevfin.projects.service.ProjectResultService;
import org.devgateway.eudevfin.projects.service.ProjectService;
import org.devgateway.eudevfin.ui.common.AttributePrepender;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapDeleteButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.tabs.BootstrapJSTabbedPanel;
import org.devgateway.eudevfin.ui.common.components.tabs.DefaultTabWithKey;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.ui.common.components.util.MondrianCDACacheUtil;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/newProject")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class NewProjectPage extends HeaderFooter<Project> {

    private static final long serialVersionUID = 817361512195961105L;

    private static final Logger logger = Logger.getLogger(NewProjectPage.class);

    public static final String PARAM_PROJECT_ID = "projectId";
    protected final NotificationPanel feedbackPanel;

    protected Form form;
    protected Label note;
    protected Label subnote;

    public static Project project;

    public static String projectResults;
    public static String projectReports;
    public String onUnloadScript;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private ProjectResultService projectResultService;

    @SpringBean
    private ProjectReportService projectReportService;

    @SpringBean
    MondrianCDACacheUtil mondrianCacheUtil;

    public NewProjectPage(final PageParameters parameters) {
        super(parameters);

        if (!parameters.get(PARAM_PROJECT_ID).isNull()) {
            long projectId = parameters.get(PARAM_PROJECT_ID).toLong();
            project = projectService.findOne(projectId).getEntity();
        } else {
            project = new Project();
            initializeProject(project, parameters);
        }

        CompoundPropertyModel<Project> model = new CompoundPropertyModel<>(project);
        setModel(model);

        form = new Form("form");
        InitializePage();

        AddTabList(parameters);

        AddSubmitButton();
        AddSaveButton();
        AddDeleteButton();
        AddCancelButton();

        add(form);

        feedbackPanel = new NotificationPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.hideAfter(Duration.seconds(5));
        add(feedbackPanel);
    }

    public final void initializeProject(Project project, PageParameters parameters) {
        PersistedUser currentUser = AuthUtils.getCurrentUser();
        String contactDetails = "";
      
        if (currentUser.getFirstName() != null)
            contactDetails += currentUser.getFirstName();
        
        if (currentUser.getLastName() != null)
            contactDetails += ", " + currentUser.getLastName();
        
        if (currentUser.getPhone() != null)
            contactDetails += ", " + currentUser.getPhone();
            
        ProjectUtil.initializeProject(project, AuthUtils.getOrganizationForCurrentUser(), currentUser.getEmail(), contactDetails);
    }

    @Override
    protected void onAfterRenderChildren() {
        super.onAfterRenderChildren();

    }

//    @Override
//    public HashMap<String, RoleActionMapping> getPermissions() {
//        return componentPermissions.permissions();
//    }
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(OnDomReadyHeaderItem.forScript(onUnloadScript));
    }

    private void InitializePage() {
        onUnloadScript = "window.onbeforeunload = function(e) {\n"
                + "   var message = '" + new StringResourceModel("leaveMessage", this, null).getObject() + "';\n"
                + "   e = e || window.event;\n"
                + "   if(e) {\n"
                + "       e.returnValue = message;\n" + // For IE 8 and old Firefox
                "   }\n"
                + "   return message;\n"
                + "};";
    }

    private void AddTabList(PageParameters parameters) {
        List<ITabWithKey> tabList = populateTabList(parameters);

        BootstrapJSTabbedPanel<ITabWithKey> bc = new BootstrapJSTabbedPanel<>("bc", tabList).positionTabs(BootstrapJSTabbedPanel.Orientation.RIGHT);
        form.add(bc);
    }

    private List<ITabWithKey> populateTabList(PageParameters parameters) {
        List<Class<? extends Panel>> tabClasses = getTabs();
        ArrayList<ITabWithKey> tabs = new ArrayList<>();
        for (final Class<? extends Panel> p : tabClasses) {
            tabs.add(DefaultTabWithKey.of(p, this, parameters));
        }
        return tabs;
    }

    protected List<Class<? extends Panel>> getTabs() {
        List<Class<? extends Panel>> tabList = new ArrayList<>();
        tabList.add(IdentificationDataTab.class);
        tabList.add(ObjectivesTab.class);
        tabList.add(ResultsTab.class);
        tabList.add(ConstraintsTab.class);
        tabList.add(CrossCuttingTab.class);
        tabList.add(ReportingTab.class);
        tabList.add(ProjectExtensionTab.class);
        tabList.add(TransactionsTab.class);
        tabList.add(MonitoringVisibilityTab.class);
        return tabList;
    }

    private void AddSubmitButton() {
        ProjectPageSubmitButton submitButton = new ProjectPageSubmitButton("submit", new StringResourceModel("button.submit", this, null));
        submitButton.add(new AttributePrepender("onclick", new Model<>("window.onbeforeunload = null;"), " "));
        form.add(submitButton);
    }

    private void AddSaveButton() {
        ProjectPageSaveButton saveButton = new ProjectPageSaveButton("save", new StringResourceModel("button.save", this, null));
        saveButton.setDefaultFormProcessing(false);
        form.add(saveButton);
    }

    private void AddDeleteButton() {
        ProjectPageDeleteButton deleteButton = new ProjectPageDeleteButton("delete", new StringResourceModel("button.delete", this, null));
        MetaDataRoleAuthorizationStrategy.authorize(deleteButton, Component.ENABLE, AuthConstants.Roles.ROLE_TEAMLEAD);
        form.add(deleteButton);
    }

    private void AddCancelButton() {
        BootstrapCancelButton cancelButton = new BootstrapCancelButton("cancel", new StringResourceModel("button.cancel", this, null)) {
            private static final long serialVersionUID = -3097577464142022353L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                removeSaved();
                setResponsePage(AllProjectsPage.class);
            }
        };
        form.add(cancelButton);
    }

    public class ProjectPageSubmitButton extends BootstrapSubmitButton {

        private static final long serialVersionUID = -8310280845870280505L;

        public ProjectPageSubmitButton(String id, IModel<String> model) {
            super(id, model);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            try {
                Project formProject = (Project) form.getInnermostModel().getObject();
                formProject.setProjectReports(project.getProjectReports());
                formProject.setProjectResults(project.getProjectResults());
                formProject.setProjectTransactions(project.getProjectTransactions());
                Project saved = projectService.save(formProject).getEntity();
                saveMinorEntities(saved);

                mondrianCacheUtil.flushMondrianCDACache();

                info(new NotificationMessage(new StringResourceModel("notification.saved", NewProjectPage.this, null)));
                target.add(feedbackPanel);
            } catch (Exception e) {
                logger.error("Exception while trying to save:", e);
                error(new NotificationMessage(new StringResourceModel("notification.error", NewProjectPage.this, null)).hideAfter(Duration.NONE));
                target.add(feedbackPanel);
                return;
            }
            logger.info("Saved ok!");

            setResponsePage(AllProjectsPage.class);
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
            super.onError(target, form);
            error(new NotificationMessage(new StringResourceModel("notification.validationerrors", NewProjectPage.this, null)));
            target.add(feedbackPanel);
            target.appendJavaScript(onUnloadScript);
        }

        @Override
        public void componentVisitor(AjaxRequestTarget target, FormComponent component, IVisit<Void> visit) {
            super.componentVisitor(target, component, visit);
            if (!component.isValid()) {
                target.focusComponent(component);
                target.appendJavaScript("$('#" + component.getMarkupId()
                        + "').parents('[class~=\"tab-pane\"]').siblings().attr(\"class\", \"tab-pane\");");
                target.appendJavaScript("$('#" + component.getMarkupId()
                        + "').parents('[class~=\"tab-pane\"]').attr(\"class\", \"tab-pane active\");");

                target.appendJavaScript("$('#" + component.getMarkupId()
                        + "').parents('[class~=\"tabbable\"]').children('ul').find('li').attr('class', '');");
                target.appendJavaScript("var idOfSection = $('#"
                        + component.getMarkupId()
                        + "').parents('[class~=\"tab-pane\"]').attr('id');$('#"
                        + component.getMarkupId()
                        + "').parents('[class~=\"tabbable\"]').children('ul').find('a[href=\"#' + idOfSection + '\"]').parent().attr('class', 'active');");

            }
        }
    }

    public class ProjectPageSaveButton extends BootstrapSubmitButton {

        private static final long serialVersionUID = -8310280845870280505L;

        public ProjectPageSaveButton(String id, IModel<String> model) {
            super(id, model);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//            Project project = (Project) form.getInnermostModel().getObject();
            java.util.logging.Logger.getLogger("LOG").log(Level.INFO, project.toString());

            try {
                Project formProject = (Project) form.getInnermostModel().getObject();
                formProject.setProjectReports(project.getProjectReports());
                formProject.setProjectResults(project.getProjectResults());
                formProject.setProjectTransactions(project.getProjectTransactions());
                formProject.setAreas(project.getAreas());
                Project saved = projectService.save(formProject).getEntity();
                saveMinorEntities(saved);
                NewProjectPage.this.getModel().setObject(saved);
                // clear the mondrian cache
                mondrianCacheUtil.flushMondrianCDACache();

                info(new NotificationMessage(new StringResourceModel("notification.saved", NewProjectPage.this, null)));
                target.add(feedbackPanel);
            } catch (Exception e) {
                logger.error("Exception while trying to save:", e);
                error(new NotificationMessage(new StringResourceModel("notification.error", NewProjectPage.this, null)).hideAfter(Duration.NONE));
                target.add(feedbackPanel);
                return;
            }
            logger.info("Saved ok!");
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
            super.onError(target, form);
            error(new NotificationMessage(new StringResourceModel("notification.validationerrors", NewProjectPage.this, null)));
            target.add(feedbackPanel);
        }

        @Override
        public void componentVisitor(AjaxRequestTarget target, FormComponent component, IVisit<Void> visit) {
            super.componentVisitor(target, component, visit);
            if (!component.isValid()) {
                target.focusComponent(component);
                target.appendJavaScript("$('#" + component.getMarkupId()
                        + "').parents('[class~=\"tab-pane\"]').siblings().attr(\"class\", \"tab-pane\");");
                target.appendJavaScript("$('#" + component.getMarkupId()
                        + "').parents('[class~=\"tab-pane\"]').attr(\"class\", \"tab-pane active\");");

                target.appendJavaScript("$('#" + component.getMarkupId()
                        + "').parents('[class~=\"tabbable\"]').children('ul').find('li').attr('class', '');");
                target.appendJavaScript("var idOfSection = $('#"
                        + component.getMarkupId()
                        + "').parents('[class~=\"tab-pane\"]').attr('id');$('#"
                        + component.getMarkupId()
                        + "').parents('[class~=\"tabbable\"]').children('ul').find('a[href=\"#' + idOfSection + '\"]').parent().attr('class', 'active');");

            }
        }
    }

    public class ProjectPageDeleteButton extends BootstrapDeleteButton {

        private static final long serialVersionUID = 1076134119844959564L;

        public ProjectPageDeleteButton(String id, IModel<String> model) {
            super(id, model);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            Project project = (Project) form.getInnermostModel().getObject();
            setResponsePage(AllProjectsPage.class);

            try {
                if (project.getId() == null) {
                    return;
                }
                removeSaved();
                projectService.delete(project);

                info(new NotificationMessage(new StringResourceModel("notification.deleted", NewProjectPage.this, null)));
                target.add(feedbackPanel);
                // clear the mondrian cache
                mondrianCacheUtil.flushMondrianCDACache();
            } catch (Exception e) {
                logger.error("Exception while trying to delete:", e);
                return;
            }
            logger.info("Deleted ok!");
        }
    }

    private void removeSaved() {
        if (project.getTempReports().size() > 0) {
            for (ProjectReport report : project.getTempReports()) {
                projectReportService.delete(report);
            }
        }

        if (project.getTempResults().size() > 0) {
            for (ProjectResult result : project.getTempResults()) {
                projectResultService.delete(result);
            }
        }
    }

    /**
     * Because there is OneToMany & ManyToOne relation between Project and
     * Results, Reports and Transactions we need to save manually all the child
     * entities.
     *
     * @param saved project that will be passed as the father
     */
    private void saveMinorEntities(Project saved) {
        if (project.getTempReports().size() > 0) {
            for (ProjectReport report : project.getTempReports()) {
                report.setProject(saved);
                projectReportService.save(report);
            }
        }

        if (project.getTempResults().size() > 0) {
            for (ProjectResult result : project.getTempResults()) {
                result.setProject(saved);
                projectResultService.save(result);
            }
        }
    }
}
