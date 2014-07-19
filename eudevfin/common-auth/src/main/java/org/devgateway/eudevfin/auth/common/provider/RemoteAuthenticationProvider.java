/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.auth.common.provider;

import org.devgateway.eudevfin.auth.common.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * {@link AuthenticationProvider} that delegates
 * {@link #authenticate(Authentication)} method to the the
 * {@link AuthenticationService}. The service connects to a remote
 * authentication through a spring integration channel.
 * 
 * This provider is used to configure the {@link AuthenticationManager} for the
 * client module. Basically any request going to this manager will be forwarded
 * through spring integration to a remote {@link AuthenticationManager} where
 * direct access to a real authentication mechanism (like say read the user and
 * password from a local database) is possible.
 * 
 * @see AuthenticationService
 * @author mihai
 * 
 */
@Component
public class RemoteAuthenticationProvider implements AuthenticationProvider {

	/**
	 * The spring integration service that forwards the {@link Authentication} token
	 * to a remote {@link AuthenticationManager}
	 */
	@Autowired
	private AuthenticationService authService;

	/**
	 * This method is forwarded to {@link AuthenticationManager#authenticate(Authentication)}
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		return authService.authenticate(authentication);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}