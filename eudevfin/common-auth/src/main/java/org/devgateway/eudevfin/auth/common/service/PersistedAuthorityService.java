package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.springframework.integration.annotation.Payload;

public interface PersistedAuthorityService {
	PersistedAuthority save(PersistedAuthority a);
	
	@Payload("new java.util.Date()")
	Iterable<PersistedAuthority> findAll();

	PersistedAuthority findOne(String string);
}
