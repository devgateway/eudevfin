package org.devgateway.eudevfin.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationComponent {
	 
	@Autowired
	private AuthenticationManager localAuthenticationManager;
	
	@ServiceActivator(inputChannel="authenticationChannel")
	public Authentication authenticate(Authentication authentication) {
		Authentication response = localAuthenticationManager.authenticate(authentication);		
		return response;
	}
	
	
 }