package org.devgateway.eudevfin.dim.spring;

 import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.devgateway.eudevfin.dim.ItemsPage;
import org.devgateway.eudevfin.dim.LoginPage;

public class WicketSpringApplication extends AuthenticatedWebApplication {


	@Override
	protected void init() {
		super.init();

		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this));
	}

	@Override
	public Class getHomePage() {
		return ItemsPage.class;
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
