package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;


/**
 * 
 * @author mihai
 *
 */
public interface PersistedUserService  extends BaseEntityService<PersistedUser> {
	
	NullableWrapper<PersistedUser> save(PersistedUser u);
	
	NullableWrapper<PersistedUser> findByUsername(String username);		
}