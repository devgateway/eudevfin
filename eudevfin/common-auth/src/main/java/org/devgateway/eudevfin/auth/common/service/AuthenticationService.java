package org.devgateway.eudevfin.auth.common.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.security.core.Authentication;


/**
 * 
 * @author mihai
 *
 */
public interface AuthenticationService {
	
	@Gateway(requestChannel="authenticationChannel")
	public Authentication authenticate(Authentication authentication);
	

}