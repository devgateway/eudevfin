/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.button.DropDownAutoOpen;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.projects.module.pages.AllProjectsPage;
import org.devgateway.eudevfin.projects.module.pages.NewProjectPage;
import org.devgateway.eudevfin.ui.common.WicketNavbarComponentInitializer;
import org.devgateway.eudevfin.ui.common.components.RepairedNavbarDropDownButton;

public class NavbarInitializer {

    public static class ProjectsRedirectPage extends RedirectPage {

        private static final long serialVersionUID = -750983217518258463L;

        public ProjectsRedirectPage() {
            super(WebApplication.get().getServletContext().getContextPath() + "/projects");
        }
    }

    @WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.LEFT, order = 2)
    public static Component newProjectsNavbarMenu(final Page page) {

        final NavbarDropDownButton navbarDropDownButton = new RepairedNavbarDropDownButton(new StringResourceModel(
                "navbar.projects", page, null, null)) {
                    @Override
                    public boolean isActive(final Component item) {
                        return false;
                    }

                    @SuppressWarnings("Convert2Diamond")
                    @Override
                    protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
                        final List<AbstractLink> list = new ArrayList<>();
                        list.add(new MenuHeader(new StringResourceModel("navbar.projects.header", this, null)));
                        list.add(new MenuDivider());

                        final MenuBookmarkablePageLink<NewProjectPage> newProject = new MenuBookmarkablePageLink<NewProjectPage>(NewProjectPage.class, null, new StringResourceModel("navbar.projects.newproject", page, null));
                        newProject.setIconType(IconType.plus);
                        MetaDataRoleAuthorizationStrategy.authorize(newProject, Component.RENDER, AuthConstants.Roles.ROLE_PROJECTS_MFA);
                        MetaDataRoleAuthorizationStrategy.authorize(newProject, Component.RENDER, AuthConstants.Roles.ROLE_SUPERVISOR);

                        list.add(newProject);

                        final MenuBookmarkablePageLink<AllProjectsPage> allProjects = new MenuBookmarkablePageLink<AllProjectsPage>(AllProjectsPage.class, null, new StringResourceModel("navbar.projects.allprojects", page, null));
                        allProjects.setIconType(IconType.list);
                        MetaDataRoleAuthorizationStrategy.authorize(allProjects, Component.RENDER, AuthConstants.Roles.ROLE_PROJECTS_MFA);
                        MetaDataRoleAuthorizationStrategy.authorize(allProjects, Component.RENDER, AuthConstants.Roles.ROLE_PROJECTS_NGO);
                        MetaDataRoleAuthorizationStrategy.authorize(allProjects, Component.RENDER, AuthConstants.Roles.ROLE_SUPERVISOR);

                        list.add(allProjects);

                        return list;
                    }
                };
        navbarDropDownButton.setIconType(IconType.plus);
        navbarDropDownButton.add(new DropDownAutoOpen());
        MetaDataRoleAuthorizationStrategy.authorize(navbarDropDownButton, Component.RENDER, AuthConstants.Roles.ROLE_PROJECTS_MFA);
        MetaDataRoleAuthorizationStrategy.authorize(navbarDropDownButton, Component.RENDER, AuthConstants.Roles.ROLE_PROJECTS_NGO);
        MetaDataRoleAuthorizationStrategy.authorize(navbarDropDownButton, Component.RENDER, AuthConstants.Roles.ROLE_SUPERVISOR);
        return navbarDropDownButton;
    }
}
