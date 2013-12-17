package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;


/**
 * 
 * @author mihai
 *
 */
public interface UserService {
	
	PersistedUser createUser(PersistedUser u);
	
	PersistedUser getUserByUsername(String username);

}