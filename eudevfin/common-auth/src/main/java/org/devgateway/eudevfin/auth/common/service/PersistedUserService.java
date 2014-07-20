/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;

import java.util.List;


/**
 * 
 * @author mihai
 *
 */
public interface PersistedUserService  extends BaseEntityService<PersistedUser> {
	
	NullableWrapper<PersistedUser> save(PersistedUser u);
	
	NullableWrapper<PersistedUser> findByUsername(String username);

    public List<PersistedUser> findUsersWithPersistedAuthority(String persistedAuthority);

}