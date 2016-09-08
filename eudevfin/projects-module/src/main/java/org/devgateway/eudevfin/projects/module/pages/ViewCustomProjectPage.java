/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.devgateway.eudevfin.projects.module.components.tabs.ConstraintsTab;
import org.devgateway.eudevfin.projects.module.components.tabs.IdentificationDataTab;
import org.devgateway.eudevfin.projects.module.components.tabs.MonitoringVisibilityTab;
import org.devgateway.eudevfin.projects.module.components.tabs.ObjectivesTab;
import org.devgateway.eudevfin.projects.module.components.tabs.ReportingTab;
import org.devgateway.eudevfin.projects.module.components.tabs.ResultsTab;
import org.devgateway.eudevfin.projects.module.components.tabs.TransactionsTab;
import org.devgateway.eudevfin.projects.service.ProjectService;
import org.devgateway.eudevfin.ui.common.components.tabs.DefaultTabWithKey;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.ui.common.components.tabs.PreviewTabbedPannel;
import org.devgateway.eudevfin.ui.common.pages.PrintableHeaderFooter;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwarePage;
import org.devgateway.eudevfin.ui.common.permissions.RoleActionMapping;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/viewCustomProject")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ViewCustomProjectPage extends PrintableHeaderFooter<Project> implements PermissionAwarePage {

//    private static final CustomTransactionPermissionProvider componentPermissions = new CustomTransactionPermissionProvider();
    public static final String PARAM_PROJECT_ID = "projectId";
    protected Form form;

    @SpringBean
    private ProjectService projectService;

    public ViewCustomProjectPage(final PageParameters parameters) {
        super(parameters);

        List<ITabWithKey> tabList = populateTabList(parameters);

        long projectId = parameters.get(PARAM_PROJECT_ID).toLong();

        Project project = (Project) projectService.findOne(projectId).getEntity();

        CompoundPropertyModel<Project> model = new CompoundPropertyModel<Project>(project);
        setModel(model);

        form = new Form("form");
        add(form);

        PreviewTabbedPannel<ITabWithKey> bc = new PreviewTabbedPannel<>("bc", tabList);
        form.add(bc);
    }

    @Override
    public HashMap<String, RoleActionMapping> getPermissions() {
//        return componentPermissions.permissions();
        return null;
    }

    protected List<Class<? extends Panel>> getTabs() {
        List<Class<? extends Panel>> tabList = new ArrayList<>();
        tabList.add(IdentificationDataTab.class);
        tabList.add(ObjectivesTab.class);
        tabList.add(ConstraintsTab.class);
        tabList.add(MonitoringVisibilityTab.class);
//        tabList.add(ReportingTab.class);
//        tabList.add(ResultsTab.class);
//        tabList.add(TransactionsTab.class);
        return tabList;
    }

    private List<ITabWithKey> populateTabList(PageParameters parameters) {
        List<Class<? extends Panel>> tabClasses = getTabs();
        ArrayList<ITabWithKey> tabs = new ArrayList<>();
        for (final Class<? extends Panel> p : tabClasses) {
            tabs.add(DefaultTabWithKey.of(p, this, parameters));
        }
        return tabs;
    }
}
