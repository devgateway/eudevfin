package org.devgateway.eudevfin.dim.spring;

 import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.devgateway.eudevfin.dim.ItemsPage;
import org.devgateway.eudevfin.dim.LoginPage;
import org.devgateway.eudevfin.dim.dao.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;

public class WicketSpringApplication extends AuthenticatedWebApplication {

	@Autowired
	private ItemDao itemDao;

	@Override
	protected void init() {
		super.init();

		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this));
		itemDao.createItems();
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
