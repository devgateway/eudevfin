/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    mihai
 ******************************************************************************/

package org.devgateway.eudevfin.dim.pages.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.service.UserService;
import org.devgateway.eudevfin.dim.core.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.devgateway.eudevfin.dim.desktop.components.config.ListGeneratorInterface;
import org.devgateway.eudevfin.dim.pages.HomePage;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.util.PagingHelper;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mihai
 * @since 19.12.2013
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/users")
public class ListUsersPage extends HeaderFooter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8637428983397263676L;



	@SpringBean
	private UserService userService;
	


	private static final Logger logger = Logger.getLogger(ListUsersPage.class);


	@SuppressWarnings("unchecked")
	public ListUsersPage(final PageParameters parameters) {
	
	}

}
