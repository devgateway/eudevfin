package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.provider.RemoteAuthenticationProvider;
import org.springframework.integration.annotation.Gateway;
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
	 * @return the {@link Authentication} token reply (full authentication object)
	 */
	@Gateway(requestChannel="authenticationChannel")
	public Authentication authenticate(Authentication authentication);
	

}