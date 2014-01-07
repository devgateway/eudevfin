package org.devgateway.eudevfin.reports;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.cdf.pages.reports.ReportsPage;
import org.devgateway.eudevfin.ui.common.WicketNavbarComponentInitializer;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;

public final class NavbarInitializer {

	@WicketNavbarComponentInitializer(position = Navbar.ComponentPosition.LEFT)
	public static Component newReportsPageNavbarButton(Page page) {
		NavbarButton<ReportsPage> reportsPageNavbarButton = new NavbarButton<ReportsPage>(ReportsPage.class, new StringResourceModel("navbar.reports", page, null, null)).setIconType(IconType.thlist);
        MetaDataRoleAuthorizationStrategy.authorize(reportsPageNavbarButton, Component.RENDER, AuthConstants.Roles.ROLE_USER);
        return reportsPageNavbarButton;
	}
	

}
