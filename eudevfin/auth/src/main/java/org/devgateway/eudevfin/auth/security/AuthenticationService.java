package org.devgateway.eudevfin.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {
	 
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@ServiceActivator(inputChannel="authenticationChannel")
	public Authentication authenticate(Authentication authentication) {
		Authentication a = authenticationManager.authenticate(authentication);
		//authenticated = authentication.isAuthenticated();
		return null;
	}
	
	
 }