/**
 * *****************************************************************************
 * Copyright (c) 2014 Development Gateway. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************
 */
package org.devgateway.eudevfin.mcm;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownSubMenu;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.button.DropDownAutoOpen;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.mcm.pages.EditNonFlowItemsPage;
import org.devgateway.eudevfin.mcm.pages.EditPersistedUserPage;
import org.devgateway.eudevfin.mcm.pages.ListHistoricalExchangeRatePage;
import org.devgateway.eudevfin.mcm.pages.ListOrganizationsPage;
import org.devgateway.eudevfin.mcm.pages.ListPersistedUserGroupsPage;
import org.devgateway.eudevfin.mcm.pages.ListPersistedUsersPage;
import org.devgateway.eudevfin.mcm.pages.OnlineExchangeRatePage;
import org.devgateway.eudevfin.mcm.pages.PublishReports;
import org.devgateway.eudevfin.ui.common.WicketNavbarComponentInitializer;
import org.devgateway.eudevfin.ui.common.components.RepairedNavbarDropDownButton;
import org.devgateway.eudevfin.ui.common.pages.LogoutPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Class holding static methods that initialize the wicket {@link Navbar}
 * components.
 *
 * @author mihai
 * @see WicketNavbarComponentInitializer
 * @see org.devgateway.eudevfin.ui.common.pages.HeaderFooter
 */
public final class NavbarInitializer {

    @WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT, order = 7)
    public static Component newAdminNavbarButton(final Page page) {
        final NavbarDropDownButton navbarDropDownButton = new RepairedNavbarDropDownButton(new StringResourceModel("navbar.admin",
                page, null, null)) {
                    @Override
                    public boolean isActive(final Component item) {
                        return false;
                    }

                    @Override
                    protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
                        final List<AbstractLink> list = new ArrayList<>();
                        list.add(new MenuHeader(new StringResourceModel("navbar.admin.header", this, null, null)));
                        list.add(new MenuDivider());

                        list.add(new MenuBookmarkablePageLink<ListPersistedUsersPage>(ListPersistedUsersPage.class, null,
                                        new StringResourceModel("navbar.admin.users", this, null, null)).setIconType(IconType.thlist));

                        list.add(new MenuBookmarkablePageLink<ListPersistedUserGroupsPage>(ListPersistedUserGroupsPage.class,
                                        null, new StringResourceModel("navbar.admin.groups", this, null, null)).setIconType(IconType.list));

                        list.add(new MenuBookmarkablePageLink<ListOrganizationsPage>(ListOrganizationsPage.class, null,
                                        new StringResourceModel("navbar.admin.orgs", this, null, null)).setIconType(IconType.leaf));

                        list.add(new MenuBookmarkablePageLink<EditNonFlowItemsPage>(EditNonFlowItemsPage.class, null,
                                        new StringResourceModel("navbar.admin.nonflow", this, null, null)).setIconType(IconType.globe));

                        DropDownSubMenu publishReports = new DropDownSubMenu(new StringResourceModel("navbar.reports.publish", this, null, null)) {
                            @Override
                            public boolean isActive(Component item) {
                                return false;
                            }

                            @Override
                            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
                                List<AbstractLink> list = new ArrayList<>();

                                PageParameters paramsAQ = new PageParameters();
                                paramsAQ.set("reportType", "aq");
                                list.add(new MenuBookmarkablePageLink<PublishReports>(PublishReports.class, paramsAQ, new StringResourceModel("navbar.reports.export.aq", this, null, null)));

                                PageParameters paramsDAC1 = new PageParameters();
                                paramsDAC1.set("reportType", "dac1");
                                list.add((AbstractLink) new MenuBookmarkablePageLink<PublishReports>(PublishReports.class, paramsDAC1, new StringResourceModel("navbar.reports.export.dac1", this, null, null)));

                                PageParameters paramsDAC2a = new PageParameters();
                                paramsDAC2a.set("reportType", "dac2a");
                                list.add((AbstractLink) new MenuBookmarkablePageLink<PublishReports>(PublishReports.class, paramsDAC2a, new StringResourceModel("navbar.reports.export.dac2a", this, null, null)));

                                return list;
                            }

                        };
                        publishReports.setIconType(IconType.inbox);
                        MetaDataRoleAuthorizationStrategy.authorize(publishReports,
                                Component.RENDER, AuthConstants.Roles.ROLE_SUPERVISOR);
                        list.add(publishReports);

                        DropDownSubMenu adminRates = new DropDownSubMenu(new StringResourceModel("navbar.admin.rates", this, null, null)) {
                            @Override
                            public boolean isActive(Component item) {
                                return false;
                            }

                            @Override
                            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
                                List<AbstractLink> list = new ArrayList<>();

                                list.add(new MenuBookmarkablePageLink<ListHistoricalExchangeRatePage>(
                                                ListHistoricalExchangeRatePage.class, null, new StringResourceModel(
                                                        "navbar.admin.rates.historical", this, null, null)).
                                        setIconType(IconType.folderclose));

                                list.add(new MenuBookmarkablePageLink<OnlineExchangeRatePage>(
                                                OnlineExchangeRatePage.class, null, new StringResourceModel(
                                                        "navbar.admin.rates.online", this, null, null)).
                                        setIconType(IconType.random));

                                return list;
                            }
                        };
                        adminRates.setIconType(IconType.retweet);
                        list.add(adminRates);

                        /* hide the system menu for the moment
                         //				list.add((AbstractLink) new MenuBookmarkablePageLink<SystemMaintenance>(
                         //						SystemMaintenance.class, null, new StringResourceModel("navbar.admin.maintenance", this, null,
                         //								null)).setIconType(IconType.wrench).setEnabled(false));
                         */
                        return list;
                    }

                };
        navbarDropDownButton.setIconType(IconType.eyeopen);
        navbarDropDownButton.add(new DropDownAutoOpen());
        MetaDataRoleAuthorizationStrategy.authorize(navbarDropDownButton, Component.RENDER,
                AuthConstants.Roles.ROLE_SUPERVISOR);

        return navbarDropDownButton;
    }

	//
    //	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT, order = 30)
    //	public static Component accountNavbarButton(final Page page) {
    //		final NavbarButton<LogoutPage> accountNavbarButton = new NavbarButton<LogoutPage>(EditPersistedUserPage.class,
    //				new StringResourceModel("navbar.account", page, null, null)).setIconType(IconType.user);
    //		MetaDataRoleAuthorizationStrategy.authorize(accountNavbarButton, Component.RENDER,
    //				AuthConstants.Roles.ROLE_USER);
    //		return accountNavbarButton;
    //	}
    @WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.RIGHT, order = 30)
    public static Component accountButton(final Page page) {

        final NavbarDropDownButton accountMenu = new RepairedNavbarDropDownButton(new StringResourceModel(
                "navbar.account.user", page, null, null)) {

                    @Override
                    public boolean isActive(final Component item) {
                        return false;
                    }

                    @Override
                    protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
                        final List<AbstractLink> list = new ArrayList<>();
                        final MenuBookmarkablePageLink<EditPersistedUserPage> accountNavbarButton = new MenuBookmarkablePageLink<EditPersistedUserPage>(EditPersistedUserPage.class,
                                new StringResourceModel("navbar.account", page, null, null));
                        accountNavbarButton.setIconType(IconType.edit);
                        MetaDataRoleAuthorizationStrategy.authorize(accountNavbarButton, Component.RENDER,
                                AuthConstants.Roles.ROLE_USER);
                        MetaDataRoleAuthorizationStrategy.authorize(accountNavbarButton, Component.RENDER,
                                AuthConstants.Roles.ROLE_VIEWER);
                        list.add(accountNavbarButton);

                        final MenuBookmarkablePageLink<LogoutPage> logoutPageNavbarButton = new MenuBookmarkablePageLink<LogoutPage>(LogoutPage.class,
                                new StringResourceModel("navbar.logout", page, null, null));
                        logoutPageNavbarButton.setIconType(IconType.off);
                        MetaDataRoleAuthorizationStrategy.authorize(logoutPageNavbarButton, Component.RENDER,
                                AuthConstants.Roles.ROLE_USER);
                        MetaDataRoleAuthorizationStrategy.authorize(logoutPageNavbarButton, Component.RENDER,
                                AuthConstants.Roles.ROLE_VIEWER);
                        list.add(logoutPageNavbarButton);

                        return list;
                    }
                };
        accountMenu.setIconType(IconType.user);
        MetaDataRoleAuthorizationStrategy.authorize(accountMenu, Component.RENDER,
                AuthConstants.Roles.ROLE_USER);
        MetaDataRoleAuthorizationStrategy.authorize(accountMenu, Component.RENDER,
                AuthConstants.Roles.ROLE_VIEWER);
        return accountMenu;
    }

}
