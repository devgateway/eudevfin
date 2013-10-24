package org.devgateway.eudevfin.financial.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
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
