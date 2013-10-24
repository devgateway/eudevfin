package org.devgateway.eudevfin.auth.common.provider;

import org.devgateway.eudevfin.auth.common.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class RemoteAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private AuthenticationService authService;
 
    @Override    
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	return authService.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
    	return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}