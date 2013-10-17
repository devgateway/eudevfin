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
package org.devgateway.eudevfin.dim.spring;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.core.markup.html.references.BootstrapPrettifyCssReference;
import de.agilecoders.wicket.core.markup.html.references.BootstrapPrettifyJavaScriptReference;
import de.agilecoders.wicket.core.markup.html.references.ModernizrJavaScriptReference;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Html5PlayerCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Html5PlayerJavaScriptReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.OpenWebIconsCssReference;
import de.agilecoders.wicket.themes.markup.html.bootstrap3.Bootstrap3Theme;
import de.agilecoders.wicket.themes.markup.html.google.GoogleTheme;
import de.agilecoders.wicket.themes.markup.html.metro.MetroTheme;
import de.agilecoders.wicket.themes.markup.html.wicket.WicketTheme;
import de.agilecoders.wicket.themes.settings.BootswatchThemeProvider;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.devgateway.eudevfin.dim.core.FixBootstrapStylesCssResourceReference;
import org.devgateway.eudevfin.dim.pages.HomePage;
import org.devgateway.eudevfin.dim.pages.LoginPage;
import org.devgateway.eudevfin.dim.core.ApplicationJavaScript;
import org.devgateway.eudevfin.dim.core.JQueryUICoreJavaScriptReference;
import org.devgateway.eudevfin.financial.Person;
import org.devgateway.eudevfin.financial.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import java.util.Calendar;

public class WicketSpringApplication extends AuthenticatedWebApplication {

	@Autowired
	private PersonService personService;
	
	@Override
	protected void init() {
		super.init();

        configureBootstrap();
        configureResourceBundles();

        setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator());

        //mount annotated pages
        new AnnotatedMountScanner().scanPackage("org.devgateway.eudevfin.dim").mount(this);

        getComponentInstantiationListeners().add(
                new SpringComponentInjector(this));


        createPerson();
    }

    private void createPerson() {
        //test populating one person bean upon startup
        final Person person = new Person();
        Calendar createdDateTime = Calendar.getInstance();
        createdDateTime.set(1980, 0, 1);
        person.setCreatedDateTime(createdDateTime.getTime());
        person.setName("bubu");

        //personService.createPerson(person);
    }

    /**
     * configure all resource bundles (css and js)
     */
    private void configureResourceBundles() {
        getResourceBundles().addJavaScriptBundle(WicketSpringApplication.class, "core.js",
                (JavaScriptResourceReference) getJavaScriptLibrarySettings().getJQueryReference(),
                (JavaScriptResourceReference) getJavaScriptLibrarySettings().getWicketEventReference(),
                (JavaScriptResourceReference) getJavaScriptLibrarySettings().getWicketAjaxReference(),
                (JavaScriptResourceReference) ModernizrJavaScriptReference.INSTANCE
        );

        getResourceBundles().addJavaScriptBundle(WicketSpringApplication.class, "bootstrap.js",
                (JavaScriptResourceReference) Bootstrap.getSettings().getJsResourceReference(),
                (JavaScriptResourceReference) BootstrapPrettifyJavaScriptReference.INSTANCE,
                ApplicationJavaScript.INSTANCE
        );

        getResourceBundles().addJavaScriptBundle(WicketSpringApplication.class, "bootstrap-extensions.js",
                JQueryUICoreJavaScriptReference.instance(),
                Html5PlayerJavaScriptReference.instance()
        );

        getResourceBundles().addCssBundle(WicketSpringApplication.class, "bootstrap-extensions.css",
                Html5PlayerCssReference.instance(),
                OpenWebIconsCssReference.instance()
        );

        getResourceBundles().addCssBundle(WicketSpringApplication.class, "application.css",
                (CssResourceReference) BootstrapPrettifyCssReference.INSTANCE,
                FixBootstrapStylesCssResourceReference.INSTANCE
        );
    }

    private void configureBootstrap() {
        final ThemeProvider themeProvider = new BootswatchThemeProvider() {{
            add(new MetroTheme());
            add(new GoogleTheme());
            add(new WicketTheme());
            add(new Bootstrap3Theme());
            defaultTheme("wicket");
        }};

        final BootstrapSettings settings = new BootstrapSettings();
        settings.setJsResourceFilterName("footer-container")
                .setThemeProvider(themeProvider);
        Bootstrap.install(this, settings);
    }

    @Override
	public Class getHomePage() {
		return HomePage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return SpringWicketWebSession.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}
}
