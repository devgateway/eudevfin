/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.ui.common.filter;

import org.apache.wicket.application.ReloadingClassLoader;
import org.apache.wicket.protocol.http.ReloadingWicketFilter;

/**
 * @author mihai
 * 
 * @see ReloadingWicketFilter
 */
public class EUDevFinReloadingWicketFilter extends ReloadingWicketFilter {
	static
  	{
  		ReloadingClassLoader.includePattern("org.devgateway.eudevfin.*");
  	}

}
