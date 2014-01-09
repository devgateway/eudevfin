package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.devgateway.eudevfin.common.service.BaseEntityService;

public interface PersistedAuthorityService  extends BaseEntityService<PersistedAuthority>  {
	
	PersistedAuthority findOne(String string);
}
