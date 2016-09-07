/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.metadata.common.service;

import java.util.List;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public interface OrganizationService extends BaseEntityService<Organization> {
    public Page<Organization> findUsedOrganizationPaginated(
            @Header("locale") String locale, String searchString,
            @Header("pageable") Pageable page);

	public Page<Organization> findByDacFalse(Pageable pageable);

	/**
	 * @deprecated this doesn't always return a unique result. Use findByCodeAndDonorCode instead
	 *
	 */
	@Deprecated
	public NullableWrapper<Organization> findByCode(String code);

	public NullableWrapper<Organization> findByCodeAndDonorCode(String code,  @Header("donorCode") String donorCode);

	public NullableWrapper<Organization> findByName(String name);
        
        public List<Organization> findUsedOrgByGeographicFocus(String geographicFocus);

}
