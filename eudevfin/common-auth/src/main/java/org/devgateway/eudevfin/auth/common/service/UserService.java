package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.common.service.BaseEntityService;


/**
 * 
 * @author mihai
 *
 */
public interface UserService  extends BaseEntityService<PersistedUser> {
	
	PersistedUser save(PersistedUser u);
	
	PersistedUser findByUsername(String username);

	PersistedUser findOne(Long userId);

	
}