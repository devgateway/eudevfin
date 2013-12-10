package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.springframework.integration.annotation.Gateway;


/**
 * 
 * @author mihai
 *
 */
public interface UserService {
	
	@Gateway(requestChannel="createUserChannel")
	PersistedUser createUser(PersistedUser u);
	
	@Gateway(requestChannel="getUserChannel")
	PersistedUser getUserByUsername(String username);

}