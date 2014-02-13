/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.spring;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.authorization.strategies.CompoundAuthorizationStrategy;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.devutils.debugbar.IDebugBarContributor;
import org.apache.wicket.devutils.debugbar.InspectorDebugPanel;
import org.apache.wicket.devutils.debugbar.SessionSizeDebugPanel;
import org.apache.wicket.devutils.debugbar.VersionDebugContributor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.devgateway.eudevfin.ui.common.ApplicationCss;
import org.devgateway.eudevfin.ui.common.ApplicationJavaScript;
import org.devgateway.eudevfin.ui.common.FixBootstrapStylesCssResourceReference;
import org.devgateway.eudevfin.ui.common.JQueryUICoreJavaScriptReference;
import org.devgateway.eudevfin.ui.common.converters.CustomBigDecimalConverter;
import org.devgateway.eudevfin.ui.common.pages.LoginPage;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAuthorizationStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import com.google.javascript.jscomp.CompilationLevel;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.core.markup.html.references.BootstrapPrettifyCssReference;
import de.agilecoders.wicket.core.markup.html.references.BootstrapPrettifyJavaScriptReference;
import de.agilecoders.wicket.core.markup.html.references.ModernizrJavaScriptReference;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.extensions.javascript.GoogleClosureJavaScriptCompressor;
import de.agilecoders.wicket.extensions.javascript.YuiCssCompressor;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Html5PlayerCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Html5PlayerJavaScriptReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.OpenWebIconsCssReference;
import de.agilecoders.wicket.themes.markup.html.bootstrap3.Bootstrap3Theme;
import de.agilecoders.wicket.themes.markup.html.google.GoogleTheme;
import de.agilecoders.wicket.themes.markup.html.metro.MetroTheme;
import de.agilecoders.wicket.themes.markup.html.wicket.WicketTheme;
import de.agilecoders.wicket.themes.settings.BootswatchThemeProvider;


public class WicketSpringApplication extends AuthenticatedWebApplication implements ApplicationContextAware {

    private ApplicationContext springContext;

    /**
     * Add custom coverters
     */
    @Override
    protected IConverterLocator newConverterLocator() {    
    	ConverterLocator locator =(ConverterLocator) super.newConverterLocator();
    	locator.set(BigDecimal.class, new CustomBigDecimalConverter());
    	return locator;
    }
    
    @Override
    protected void init() {
        super.init();

        configureBootstrap();
        configureResourceBundles();

        setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator());

        //mount annotated pages
        new AnnotatedMountScanner().scanPackage("org.devgateway.eudevfin").mount(this);

        //implemented ApplicationContextAware and added context as a parameter to help JUnit tests work
        getComponentInstantiationListeners().add(
                new SpringComponentInjector(this, springContext, true));

        if (getSecuritySettings().getAuthorizationStrategy() instanceof CompoundAuthorizationStrategy) {
            CompoundAuthorizationStrategy cas = (CompoundAuthorizationStrategy) getSecuritySettings().getAuthorizationStrategy();
            cas.add(new PermissionAuthorizationStrategy());
        }

        getResourceSettings().setUseMinifiedResources(false); //prevents bug in wicket-bootstrap that's trying to minify a png       

        //DEPLOYMENT MODE?
		if (RuntimeConfigurationType.DEPLOYMENT.equals(this.getConfigurationType())) {
			//compress resources
			getResourceSettings().setJavaScriptCompressor(
					new GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
			getResourceSettings().setCssCompressor(new YuiCssCompressor());			
		} else {
			//see https://issues.apache.org/jira/browse/WICKET-5388
			//we do not use SessionSizeDebugPanel
			List<IDebugBarContributor> debugContributors=new ArrayList<>();
        	debugContributors.add(VersionDebugContributor.DEBUG_BAR_CONTRIB);
        	debugContributors.add(InspectorDebugPanel.DEBUG_BAR_CONTRIB);
        	debugContributors.add(SessionSizeDebugPanel.DEBUG_BAR_CONTRIB);
            DebugBar.setContributors(debugContributors,this);
		}
		
		//set response for session timeout:
		getApplicationSettings().setPageExpiredErrorPage(LoginPage.class);

        //add the navbar 
//        Navbar navbar = new Navbar("navbar");
//        navbar.setPosition(Navbar.Position.TOP);
//        // show brand name
//        navbar.brandName(Model.of("EU-DEVFIN"));
//        //register the navbar as a spring bean
//        springContext.getAutowireCapableBeanFactory().initializeBean(navbar, "wicketNavbar");
        
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
                ApplicationCss.INSTANCE,
                (CssResourceReference) BootstrapPrettifyCssReference.INSTANCE,
                FixBootstrapStylesCssResourceReference.INSTANCE,
                new CssResourceReference(ApplicationCss.class, "fix-select2.css")
        );
    }

    private void configureBootstrap() {
        final ThemeProvider themeProvider = new BootswatchThemeProvider() {{
            add(new MetroTheme());
            add(new GoogleTheme());
            add(new WicketTheme());
            add(new Bootstrap3Theme());
            defaultTheme("bootstrap-responsive");
        }};

        final BootstrapSettings settings = new BootstrapSettings();
        settings.setJsResourceFilterName("footer-container")
                .setThemeProvider(themeProvider);
        Bootstrap.install(this, settings);
    }

    @SuppressWarnings("unchecked")
	@Override
    public Class<? extends Page> getHomePage() {
        try {
			return  (Class<? extends Page>) Class.forName("org.devgateway.eudevfin.dim.pages.HomePage");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
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
        this.springContext = applicationContext;
    }

    public ApplicationContext getSpringContext() {
        return springContext;
    }
}
