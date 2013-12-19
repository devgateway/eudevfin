package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;

public interface AuthorityService {
	PersistedAuthority save(PersistedAuthority a);
	
	Iterable<PersistedAuthority> findAll();

	PersistedAuthority findOne(String string);
}
