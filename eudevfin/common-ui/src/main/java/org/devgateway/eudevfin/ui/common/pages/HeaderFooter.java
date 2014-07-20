/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.pages;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.ChromeFrameMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.OptimizedMobileViewportMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import org.apache.wicket.Component;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.common.locale.LocaleHelper;
import org.devgateway.eudevfin.ui.common.ApplicationJavaScript;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.FixBootstrapStylesCssResourceReference;
import org.devgateway.eudevfin.ui.common.WicketNavbarComponentInitializer;
import org.devgateway.eudevfin.ui.common.exceptions.InvalidNavbarComponentPositionOrderException;
import org.devgateway.eudevfin.ui.common.spring.WicketSpringApplication;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("WicketForgeJavaIdInspection")
public abstract class HeaderFooter<T> extends GenericWebPage<T> {

	private static final long serialVersionUID = -5670950856779087691L;
	@SpringBean(name = "commonProperties")
	protected Properties commonProperties;
	protected Label pageTitle;

	protected HeaderFooter() {
		initialize();
	}

	protected HeaderFooter(PageParameters parameters) {
		super(parameters);
		initialize();
	}

	protected void initialize() {

		add(new HtmlTag("html"));
		add(new OptimizedMobileViewportMetaTag("viewport"));
		add(new ChromeFrameMetaTag("chrome-frame"));

		// add the navigation bar
		add(createNavBar());

		add(new HeaderResponseContainer("footer-container", "footer-container"));
		add(new BootstrapBaseBehavior());

		add(new Label("eudevfin-version", Model.of(commonProperties.getProperty("eudevfin.version"))));

        try {
            // check if the key is missing in the resource file
            getString(getClassName() + ".page.title");
            pageTitle = new Label("pageTitle", new StringResourceModel(getClassName() + ".page.title", this, null, null));
        } catch (MissingResourceException mre) {
            pageTitle = new Label("pageTitle", new StringResourceModel("page.title", this, null, null));
        }
		add(pageTitle);

		if (RuntimeConfigurationType.DEVELOPMENT.equals(this.getApplication().getConfigurationType())) {
			DebugBar debugBar = new DebugBar("dev");
			add(debugBar);
		} else {
			add(new EmptyPanel("dev").setVisible(false));
		}

		// add footer image
		add(new Image("eclogo", new ContextRelativeResource("/images/ec-logo-english.gif")));

	}

	@SuppressWarnings("Convert2Diamond")
	private Component createNavBar() {
		Navbar navbar = new Navbar("navbar");
		navbar.setPosition(Navbar.Position.TOP);
		// show brand name
		navbar.brandName(Model.of("EU-DEVFIN"));

		Reflections reflections = new Reflections(ClasspathHelper.forPackage("org.devgateway.eudevfin"),
				new MethodAnnotationsScanner());

		Set<Method> navbarInitMethods = reflections.getMethodsAnnotatedWith(WicketNavbarComponentInitializer.class);

		// create comparator for comparing methods based on
		// WicketNavbarComponentInitializer#position
		Comparator<Method> methodComparator = new Comparator<Method>() {
			@Override
			public int compare(Method m1, Method m2) {
				Integer v1=Integer.valueOf(m1.getAnnotation(WicketNavbarComponentInitializer.class).order());
			    Integer v2=Integer.valueOf(m2.getAnnotation(WicketNavbarComponentInitializer.class).order());
			    if(v1.compareTo(v2)==0 && !m1.equals(m2)) throw new InvalidNavbarComponentPositionOrderException("Methods "+m1.getDeclaringClass()+"#"+m1.getName()+" and "+m2.getDeclaringClass()+"#"+m2.getName()+ " have the same order index!");
			    return v1.compareTo(v2);
			}
		};

		// create a new ordered set based on #position
		TreeSet<Method> orderedNavbarInitMethods = new TreeSet<Method>(methodComparator);
		orderedNavbarInitMethods.addAll(navbarInitMethods);

		for (Method method : orderedNavbarInitMethods) {
			WicketNavbarComponentInitializer navbarAnnotation = method
					.getAnnotation(WicketNavbarComponentInitializer.class);
			if (navbarAnnotation.disabled())
				continue;
			Component navBarComponent = null;
			try {
				navBarComponent = (Component) method.invoke(null, this);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			navbar.addComponents(NavbarComponents.transform(navbarAnnotation.position(), navBarComponent));
		}

		return navbar;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(CssHeaderItem.forReference(FixBootstrapStylesCssResourceReference.INSTANCE));
		response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(ApplicationJavaScript.INSTANCE),
				"footer-container"));
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

		LocaleHelper beanSession = ((WicketSpringApplication) getApplication()).getSpringContext().getBean(
				"localeHelperSession", LocaleHelper.class);
		LocaleHelper beanRequest = ((WicketSpringApplication) getApplication()).getSpringContext().getBean(
				"localeHelperRequest", LocaleHelper.class);
		if (!lang.isEmpty()) {
			// TODO: verify lang in supported languages
			Session.get().setLocale(new Locale(lang.toString()));
			if (beanRequest != null)
				beanRequest.setLocale(lang.toString());
			if (beanSession != null)
				beanSession.setLocale(lang.toString());
		} else if (beanSession != null && beanRequest != null && beanSession.getLocale() != null) {
			// THIS IS AN UGLY HACK NEEDS ANOTHER SOLUTION
			beanRequest.setLocale(beanSession.getLocale());
		}
	}

	/**
	 * sets the theme for the current user.
	 * 
	 * @param pageParameters
	 *            current page parameters
	 */
	private void configureTheme(PageParameters pageParameters) {
		StringValue theme = pageParameters.get("theme");

		if (!theme.isEmpty()) {
			IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
			settings.getActiveThemeProvider().setActiveTheme(theme.toString(""));
		}
	}

    protected String getClassName() {
        return Classes.simpleName(getClass());
    }
}
