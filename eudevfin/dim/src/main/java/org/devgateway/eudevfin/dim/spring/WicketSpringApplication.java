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

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.devgateway.eudevfin.dim.core.ApplicationJavaScript;
import org.devgateway.eudevfin.dim.core.FixBootstrapStylesCssResourceReference;
import org.devgateway.eudevfin.dim.core.JQueryUICoreJavaScriptReference;
import org.devgateway.eudevfin.dim.pages.HomePage;
import org.devgateway.eudevfin.dim.pages.LoginPage;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

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
<<<<<<< Upstream, based on origin/master
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.devgateway.eudevfin.dim.core.ApplicationJavaScript;
import org.devgateway.eudevfin.dim.core.FixBootstrapStylesCssResourceReference;
import org.devgateway.eudevfin.dim.core.JQueryUICoreJavaScriptReference;
import org.devgateway.eudevfin.dim.pages.HomePage;
import org.devgateway.eudevfin.dim.pages.LoginPage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

=======
>>>>>>> f087f18 cleanup old beans

public class WicketSpringApplication extends AuthenticatedWebApplication implements ApplicationContextAware {

<<<<<<< Upstream, based on origin/master
    private ApplicationContext ctx;

    @Override
=======
	
	@Override
>>>>>>> f087f18 cleanup old beans
	protected void init() {
		super.init();

        configureBootstrap();
        configureResourceBundles();

        setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator());

        //mount annotated pages
        new AnnotatedMountScanner().scanPackage("org.devgateway.eudevfin.dim").mount(this);

        //implemented ApplicationContextAware and added context as a parameter to help JUnit tests work
        getComponentInstantiationListeners().add(
                new SpringComponentInjector(this, ctx, true));

    }
<<<<<<< Upstream, based on origin/master
=======

>>>>>>> f087f18 cleanup old beans

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
