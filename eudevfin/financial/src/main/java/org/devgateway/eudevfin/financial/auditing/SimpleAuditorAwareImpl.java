package org.devgateway.eudevfin.financial.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SimpleAuditorAwareImpl implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//	    if (authentication == null || !authentication.isAuthenticated()) {
//	      return null;
//	    }
//
//	    return ((User) authentication.getPrincipal()).getUser();
		
		return "Test user";
	}

}
