/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.List;

import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Alex
 *
 */
@Component
@Lazy(value=false)
public class OrganizationDaoImpl extends AbstractDaoImpl<Organization, Long, OrganizationRepository> {

	@Autowired
	private OrganizationRepository repo;
	
	@Override
	protected
	OrganizationRepository getRepo() {
		return repo;
	}

	@Override
	@ServiceActivator(inputChannel="saveOrganizationChannel")
	public NullableWrapper<Organization> save(Organization o) {
		return super.save(o);
	}

    @Override
    @ServiceActivator(inputChannel = "findAllAsListOrganizationChannel")
    public List<Organization> findAllAsList() {
        return super.findAllAsList();
    }
    @ServiceActivator(inputChannel="findOrganizationByGeneralSearchChannel")
	public List<Organization> findByGeneralSearchPageable(@Header("locale")String locale, String searchString) {
		return this.getRepo().findByTranslationLocaleAndOrganizationNameContaining(locale, searchString);
	}

	@Override
	@ServiceActivator(inputChannel="findOrganizationByIdChannel")
	public NullableWrapper<Organization> findOne(Long id) {
		return super.findOne(id);
	}
}
