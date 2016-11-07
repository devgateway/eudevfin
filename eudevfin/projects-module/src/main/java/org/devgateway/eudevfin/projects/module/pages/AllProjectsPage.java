/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.pages;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.projects.module.components.panels.SearchBoxPanel;
import org.devgateway.eudevfin.projects.module.components.panels.ProjectTableListPanel;
import org.devgateway.eudevfin.projects.common.entities.Project;
import org.devgateway.eudevfin.projects.module.components.util.GeneralSearchListGenerator;
import org.devgateway.eudevfin.projects.module.components.util.ProjectUtil;
import org.devgateway.eudevfin.projects.service.CustomProjectService;
import org.devgateway.eudevfin.ui.common.components.tabs.BootstrapJSTabbedPanel;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/allProjects")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class AllProjectsPage extends HeaderFooter {

    private static final long serialVersionUID = 817361502195960105L;

    @SpringBean
    protected CustomProjectService customProjectService;

    @SpringBean
    private CategoryProviderFactory categoryFactory;

    @SpringBean
    private OrganizationChoiceProvider organizationProvider;

    @SpringBean
    private AreaChoiceProvider areaProvider;

    public AllProjectsPage() {
        List<ITabWithKey> tabList = new ArrayList<>();
        boolean isAdmin = AuthUtils.currentUserHasRole(AuthConstants.Roles.ROLE_SUPERVISOR);
        
        GeneralSearchListGenerator generalSearchListGenerator = new GeneralSearchListGenerator(true, customProjectService, null);
        SearchBoxPanel searchBox = new SearchBoxPanel("search-box-panel",
                new ProjectTableListPanel<>("search-results-panel", generalSearchListGenerator),
                generalSearchListGenerator, categoryFactory, organizationProvider, areaProvider);
        
        if (!isAdmin) {
            tabList.add(ProjectTableListPanel.<Project>newTab(this, ProjectUtil.PROJECT_LAST_BY_DRAFT,
                    new GeneralSearchListGenerator(false, customProjectService, ProjectUtil.PROJECT_DRAFT)));
            tabList.add(ProjectTableListPanel.<Project>newTab(this, ProjectUtil.PROJECT_LAST_BY_CLOSED,
                    new GeneralSearchListGenerator(false, customProjectService, ProjectUtil.PROJECT_CLOSED)));
            tabList.add(ProjectTableListPanel.<Project>newTab(this, ProjectUtil.PROJECT_LAST_BY_APPROVED,
                    new GeneralSearchListGenerator(false, customProjectService, ProjectUtil.PROJECT_APPROVED)));

            BootstrapJSTabbedPanel<ITabWithKey> bc = new BootstrapJSTabbedPanel<>("tops-panel", tabList).
                    positionTabs(BootstrapJSTabbedPanel.Orientation.RIGHT);

            this.add(bc);
            searchBox.setTabPanels(bc);
        } else {
            ProjectTableListPanel panel = new ProjectTableListPanel<>("tops-panel", new GeneralSearchListGenerator(false, 
                    customProjectService, null));
            this.add(panel);
            searchBox.setProjectPanel(panel);
        }

        this.add(searchBox);
    }
}
