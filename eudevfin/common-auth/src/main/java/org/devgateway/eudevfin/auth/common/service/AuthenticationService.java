/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.provider.RemoteAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;


/**
 * Service that provides remote authentication to a local {@link AuthenticationManager}.
 * @see RemoteAuthenticationProvider
 * @author mihai
 *
 */
public interface AuthenticationService {
	
	/**
	 * Method to send the {@link Authentication} token to the remote {@link AuthenticationManager}
	 * @param authentication the {@link Authentication} token stub (username+password)
	 * @return the {@link Authentication} tokRen reply (full authentication object)
	 */
	public Authentication authenticate(Authentication authentication);
	

}