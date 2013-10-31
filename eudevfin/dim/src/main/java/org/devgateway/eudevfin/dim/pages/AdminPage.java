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

package org.devgateway.eudevfin.dim.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author aartimon@developmentgateway.org
 * @since 18 October 2013
 */
@AuthorizeInstantiation(Constants.ROLE_SUPERVISOR)
@MountPath(value = "/admin")
public class AdminPage extends HeaderFooter {
}