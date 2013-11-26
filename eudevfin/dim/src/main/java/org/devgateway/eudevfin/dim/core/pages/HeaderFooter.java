/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 ******************************************************************************/

package org.devgateway.eudevfin.dim.core.pages;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.ChromeFrameMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.OptimizedMobileViewportMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ITheme;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.button.DropDownAutoOpen;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.dim.core.ApplicationJavaScript;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.FixBootstrapStylesCssResourceReference;
import org.devgateway.eudevfin.dim.pages.AdminPage;
import org.devgateway.eudevfin.dim.pages.HomePage;
import org.devgateway.eudevfin.dim.pages.LogoutPage;
import org.devgateway.eudevfin.dim.pages.reports.ReportsPage;
import org.devgateway.eudevfin.dim.pages.transaction.TransactionPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class HeaderFooter extends GenericWebPage {

    private final static String LANGUAGE_PAGE_PARAM = "lang";

    public HeaderFooter() {

        add(new HtmlTag("html"));
        add(new OptimizedMobileViewportMetaTag("viewport"));
        add(new ChromeFrameMetaTag("chrome-frame"));

        //add the navigation bar
        add(createNavBar());

        //TODO: temporary placeholder for footer
        add(new WebMarkupContainer("footer"));

        add(new HeaderResponseContainer("footer-container", "footer-container"));
        add(new BootstrapBaseBehavior());
    }

    @SuppressWarnings("Convert2Diamond")
    private Component createNavBar() {
        Navbar navbar = new Navbar("navbar");
        navbar.setPosition(Navbar.Position.TOP);
        // show brand name
        navbar.brandName(Model.of("EU-DEVFIN"));


        NavbarButton<HomePage> homePageNavbarButton = new NavbarButton<HomePage>(getApplication().getHomePage(), new StringResourceModel("navbar.home", this, null, null)).setIconType(IconType.home);
        MetaDataRoleAuthorizationStrategy.authorize(homePageNavbarButton, Component.RENDER, Constants.ROLE_USER);

        NavbarButton<TransactionPage> transactionPageNavbarButton = new NavbarButton<TransactionPage>(TransactionPage.class, new StringResourceModel("navbar.newTransaction", this, null, null)).setIconType(IconType.plus);
        MetaDataRoleAuthorizationStrategy.authorize(transactionPageNavbarButton, Component.RENDER, Constants.ROLE_USER);
        NavbarButton<ReportsPage> reportsPageNavbarButton = new NavbarButton<ReportsPage>(ReportsPage.class, new StringResourceModel("navbar.reports", this, null, null)).setIconType(IconType.thlist);
        MetaDataRoleAuthorizationStrategy.authorize(reportsPageNavbarButton, Component.RENDER, Constants.ROLE_USER);

        NavbarButton<AdminPage> adminPageNavbarButton = new NavbarButton<AdminPage>(AdminPage.class, new StringResourceModel("navbar.admin", this, null, null)).setIconType(IconType.wrench);
        MetaDataRoleAuthorizationStrategy.authorize(adminPageNavbarButton, Component.RENDER, Constants.ROLE_SUPERVISOR);


        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                homePageNavbarButton,
                transactionPageNavbarButton,
                reportsPageNavbarButton
            ));


        DropDownButton themesDropdown = newThemesDropdown();
        NavbarDropDownButton languageDropDown = newLanguageDropdown();
        NavbarButton<LogoutPage> logoutPageNavbarButton = new NavbarButton<LogoutPage>(LogoutPage.class, new StringResourceModel("navbar.logout", this, null, null)).setIconType(IconType.off);
        MetaDataRoleAuthorizationStrategy.authorize(logoutPageNavbarButton, Component.RENDER, Constants.ROLE_USER);

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,
                themesDropdown,
                languageDropDown,
                adminPageNavbarButton,
                logoutPageNavbarButton));

        return navbar;
    }

    private NavbarDropDownButton newLanguageDropdown() {
        NavbarDropDownButton languageDropDown = new NavbarDropDownButton(new StringResourceModel("navbar.lang", this, null, null)) {
            private static final long serialVersionUID = 2866997914075956070L;

            @Override
            public boolean isActive(Component item) {
                return false;
            }

            @SuppressWarnings("Convert2Diamond")
            @Override
            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
                List<AbstractLink> list = new ArrayList<>();
                list.add(new MenuHeader(new StringResourceModel("navbar.lang.header", this, null, null)));
                list.add(new MenuDivider());


                //TODO: get available languages
                final List<Locale> langs = new ArrayList<>();
                langs.add(new Locale("en"));
                langs.add(new Locale("ro"));

                for (Locale l : langs) {
                    PageParameters params = new PageParameters();
                    params.set(LANGUAGE_PAGE_PARAM, l.getLanguage());
                    list.add(new MenuBookmarkablePageLink<Page>(getPageClass(), params, Model.of(l.getDisplayName())));
                }

                return list;
            }
        };
        languageDropDown.setIconType(IconType.flag);
        languageDropDown.add(new DropDownAutoOpen());
        return languageDropDown;
    }

    private DropDownButton newThemesDropdown() {
        return new NavbarDropDownButton(Model.of("Themes")) {
            @Override
            public boolean isActive(Component item) {
                return false;
            }

            @SuppressWarnings("Convert2Diamond")
            @Override
            protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
                final List<AbstractLink> subMenu = new ArrayList<AbstractLink>();
                subMenu.add(new MenuHeader(Model.of("all available themes:")));
                subMenu.add(new MenuDivider());

                final IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
                final List<ITheme> themes = settings.getThemeProvider().available();

                for (final ITheme theme : themes) {
                    PageParameters params = new PageParameters();
                    params.set("theme", theme.name());

                    subMenu.add(new MenuBookmarkablePageLink<Page>(getPageClass(), params, Model.of(theme.name())));
                }

                return subMenu;
            }
        }.setIconType(IconType.book);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(FixBootstrapStylesCssResourceReference.INSTANCE));
        response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(ApplicationJavaScript.INSTANCE), "footer-container"));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        PageParameters pageParameters = getPageParameters();
        configureLanguage(pageParameters);
        configureTheme(pageParameters);
    }

    private void configureLanguage(PageParameters pageParameters) {
        StringValue lang = pageParameters.get(LANGUAGE_PAGE_PARAM);

        if (!lang.isEmpty()) {
            //TODO: verify lang in supported languages
            Session.get().setLocale(new Locale(lang.toString()));
        }
    }

    /**
     * sets the theme for the current user.
     *
     * @param pageParameters current page parameters
     */
    private void configureTheme(PageParameters pageParameters) {
        StringValue theme = pageParameters.get("theme");

        if (!theme.isEmpty()) {
            IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
            settings.getActiveThemeProvider().setActiveTheme(theme.toString(""));
        }
    }
}
