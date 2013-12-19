package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;


/**
 * 
 * @author mihai
 *
 */
public interface UserService {
	
	PersistedUser save(PersistedUser u);
	
	PersistedUser findByUsername(String username);

	PersistedUser findOne(Long userId);
	
	Iterable<PersistedUser> findAll();

}