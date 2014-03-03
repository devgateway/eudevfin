package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;

public interface PersistedAuthorityService  extends BaseEntityService<PersistedAuthority>  {
	
	NullableWrapper<PersistedAuthority> findOne(String string);
}
