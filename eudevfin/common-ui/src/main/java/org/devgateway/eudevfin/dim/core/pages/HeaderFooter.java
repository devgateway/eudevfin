/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dim.core.ApplicationJavaScript;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.FixBootstrapStylesCssResourceReference;
import org.devgateway.eudevfin.dim.core.temporary.SB;
import org.devgateway.eudevfin.dim.pages.LogoutPage;
import org.devgateway.eudevfin.dim.spring.WicketSpringApplication;
import org.devgateway.eudevfin.financial.util.LocaleHelper;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownSubMenu;
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

@SuppressWarnings("WicketForgeJavaIdInspection")
public abstract class HeaderFooter extends GenericWebPage {

    

    protected HeaderFooter() {

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

     
        //NavbarButton<TransactionPage> transactionPageNavbarButton = new NavbarButton<TransactionPage>(TransactionPage.class, new StringResourceModel("navbar.newTransaction", this, null, null)).setIconType(IconType.plus);
        //MetaDataRoleAuthorizationStrategy.authorize(transactionPageNavbarButton, Component.RENDER, AuthConstants.Roles.ROLE_USER);

        Reflections reflections = new Reflections(
				ClasspathHelper.forPackage("org.devgateway.eudevfin"), new MethodAnnotationsScanner());

     
        
        
  //      NavbarButton<UsersPage> adminPageNavbarButton = new NavbarButton<UsersPage>(UsersPage.class, new StringResourceModel("navbar.admin", this, null, null)).setIconType(IconType.wrench);
//        MetaDataRoleAuthorizationStrategy.authorize(adminPageNavbarButton, Component.RENDER, AuthConstants.Roles.ROLE_SUPERVISOR);


        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                homePageNavbarButton,
                transactionPageNavbarButton,
                reportsPageNavbarButton
        ));


        DropDownButton themesDropdown = newThemesDropdown();
        NavbarDropDownButton languageDropDown = newLanguageDropdown();
        NavbarButton<LogoutPage> logoutPageNavbarButton = new NavbarButton<LogoutPage>(LogoutPage.class, new StringResourceModel("navbar.logout", this, null, null)).setIconType(IconType.off);
        MetaDataRoleAuthorizationStrategy.authorize(logoutPageNavbarButton, Component.RENDER, AuthConstants.Roles.ROLE_USER);

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,
                themesDropdown,
                languageDropDown,
                adminPageNavbarButton,
                logoutPageNavbarButton));

        return navbar;
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
        StringValue lang = pageParameters.get(Constants.LANGUAGE_PAGE_PARAM);

        LocaleHelper beanSession = ((WicketSpringApplication) getApplication()).getSpringContext().getBean("localeHelperSession", LocaleHelper.class);
        LocaleHelper beanRequest = ((WicketSpringApplication) getApplication()).getSpringContext().getBean("localeHelperRequest", LocaleHelper.class);
        if (!lang.isEmpty()) {
            //TODO: verify lang in supported languages
            Session.get().setLocale(new Locale(lang.toString()));
            if (beanRequest != null)
                beanRequest.setLocale(lang.toString());
            if (beanSession != null)
                beanSession.setLocale(lang.toString());
        } else if (beanSession != null && beanRequest != null && beanSession.getLocale() != null) {
            //THIS IS AN UGLY HACK NEEDS ANOTHER SOLUTION
            beanRequest.setLocale(beanSession.getLocale());
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
