package org.devgateway.eudevfin.reports;

import javax.annotation.PostConstruct;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.reports.ui.pages.ReportsPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;

@Configuration

public class DashboardsConfiguration {

	@Autowired
	private Navbar navbar;
	
	@PostConstruct
	@DependsOn(value="wicketSpringApplication")
	private void addReportsNavbar() {
	    NavbarButton<ReportsPage> reportsPageNavbarButton = 
	    		new NavbarButton<ReportsPage>(ReportsPage.class, new StringResourceModel("navbar.reports", null, null)).setIconType(IconType.thlist);
        MetaDataRoleAuthorizationStrategy.authorize(reportsPageNavbarButton, Component.RENDER, AuthConstants.Roles.ROLE_USER);
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,reportsPageNavbarButton));
	}
	
}

