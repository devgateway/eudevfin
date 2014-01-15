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

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
import org.devgateway.eudevfin.dim.desktop.components.PersistedUserTableListPanel;
import org.devgateway.eudevfin.dim.desktop.components.util.PersistedUserListGenerator;
import org.devgateway.eudevfin.ui.common.pages.ListPage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mihai
 * @since 19.12.2013
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/users")
public class ListPersistedUsersPage extends ListPage {

	private static final long serialVersionUID = -8637428983397263676L;

	@SpringBean
	private PersistedUserService userService;

	private static final Logger logger = Logger.getLogger(ListPersistedUsersPage.class);

	@SuppressWarnings("unchecked")
	public ListPersistedUsersPage(final PageParameters parameters) {

		PersistedUserTableListPanel persistedUserTableListPanel = new PersistedUserTableListPanel(
				WICKETID_LIST_PANEL, new PersistedUserListGenerator(userService, ""));
		add(persistedUserTableListPanel);

	}

}