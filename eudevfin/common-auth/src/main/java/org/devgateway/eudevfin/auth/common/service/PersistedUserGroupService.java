package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;

public interface PersistedUserGroupService extends BaseEntityService<PersistedUserGroup>{
	
	NullableWrapper<PersistedUserGroup> findByName(String groupName);
	
}
