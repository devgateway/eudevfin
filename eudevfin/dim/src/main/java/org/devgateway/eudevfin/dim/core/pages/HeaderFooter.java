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

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.ChromeFrameMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.OptimizedMobileViewportMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.*;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.button.DropDownAutoOpen;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
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
import org.devgateway.eudevfin.dim.pages.HomePage;
import org.devgateway.eudevfin.dim.pages.LogoutPage;
import org.devgateway.eudevfin.dim.pages.ReportsPage;
import org.devgateway.eudevfin.dim.pages.TransactionPage;
import org.devgateway.eudevfin.dim.core.ApplicationJavaScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@AuthorizeInstantiation("ROLE_SUPERVISOR")
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

    private Component createNavBar() {
        Navbar navbar = new Navbar("navbar");
        navbar.setPosition(Navbar.Position.TOP);
        // show brand name
        navbar.brandName(Model.of("EU-DEVFIN"));


        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                new NavbarButton<HomePage>(getApplication().getHomePage(), new StringResourceModel("navbar.home", this, null, null)).setIconType(IconType.home),
                new NavbarButton<TransactionPage>(TransactionPage.class, new StringResourceModel("navbar.newTransaction", this, null, null)).setIconType(IconType.plus),
                new NavbarButton<ReportsPage>(ReportsPage.class, new StringResourceModel("navbar.reports", this, null, null)).setIconType(IconType.thlist)
            ));

        NavbarDropDownButton languageDropDown = new NavbarDropDownButton(new StringResourceModel("navbar.lang", this, null, null)) {
            private static final long serialVersionUID = 2866997914075956070L;

            @Override
            public boolean isActive(Component item) {
                return false;
            }

            @Override
            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
                List<AbstractLink> list = new ArrayList<AbstractLink>();
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

        navbar.addComponents(new ImmutableNavbarComponent(languageDropDown, Navbar.ComponentPosition.RIGHT));

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,
                new NavbarButton<LogoutPage>(LogoutPage.class, new StringResourceModel("navbar.logout", this, null, null)).setIconType(IconType.off)));

        return navbar;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(ApplicationJavaScript.INSTANCE), "footer-container"));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        configureLanguage(getPageParameters());
    }

    private void configureLanguage(PageParameters pageParameters) {
        StringValue lang = pageParameters.get(LANGUAGE_PAGE_PARAM);

        if (!lang.isEmpty()) {
            //TODO: verify lang in supported languages
            Session.get().setLocale(new Locale(lang.toString()));
        }

    }
}
