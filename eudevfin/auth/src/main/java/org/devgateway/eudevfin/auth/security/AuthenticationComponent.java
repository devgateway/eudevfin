package org.devgateway.eudevfin.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Spring Component that is activated by a spring integration service activator,
 * when an {@link Authentication} token is received coming from a remote
 * {@link AuthenticationManager}. A local {@link AuthenticationManager} is
 * invoked ({@link #localAuthenticationManager}) and the response is forwarded
 * back to the remote end. This basically represents the server-side
 * authentication endpoint, where the {@link #localAuthenticationManager} has
 * access to a real {@link UserDetailsService}
 * 
 * @author mihai
 * 
 */
@Component
public class AuthenticationComponent {

	/**
	 * A local {@link AuthenticationManager} with access to a real {@link UserDetailsService}
	 */
	@Autowired
	private AuthenticationManager localAuthenticationManager;

	/**
	 * This method forwards the {@link Authentication} token to the {@link #localAuthenticationManager}.
	 * The method is activated remotely through a spring integration channel
	 * @param authentication the {@link Authentication} token
	 * @return the {@link Authentication} response from {@link #localAuthenticationManager}
	 * @see org.devgateway.eudevfin.auth.common.service.AuthenticationService
	 */
	@ServiceActivator(inputChannel = "authenticationChannel")
	public Authentication authenticate(Authentication authentication) {
		Authentication response = localAuthenticationManager
				.authenticate(authentication);
		return response;
	}

}