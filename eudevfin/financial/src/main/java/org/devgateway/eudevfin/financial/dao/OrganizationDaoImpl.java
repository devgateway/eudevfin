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
import org.devgateway.eudevfin.financial.repository.OrganizationRepository;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    
//    /**
//     * @see OrganizationService#findByGeneralSearch(String, String)
//     * @param locale
//     * @param searchString
//     * @return
//     */
//    @ServiceActivator(inputChannel="findOrganizationByGeneralSearchChannel")
//	public List<Organization> findByGeneralSearch(@Header("locale")String locale, String searchString) {
//		return this.getRepo().findByTranslationLocaleAndTranslationNameContaining(locale, searchString.toLowerCase());
//	}

	@Override
	@ServiceActivator(inputChannel="findOrganizationByIdChannel")
	public NullableWrapper<Organization> findOne(Long id) {
		return super.findOne(id);
	}
	
	@ServiceActivator(inputChannel="findOrganizationByCodeChannel")
	public NullableWrapper<Organization> findByCode(String code) {
		return super.findOne(code);
	}
	
	
	
	/**
	 * @see OrganizationService#findByDacFalse(Pageable)
	 * @param pageable
	 * @return
	 */
	@ServiceActivator(inputChannel="findOrganizationByDacFalse")
	public Page<Organization> findByDacFalse(Pageable pageable) {
		return repo.findByDacFalse(pageable);
	}
	
	
	/**
	 * @see OrganizationService#findByGeneralSearchPageable(String, String, Pageable)
	 */
	@Override
	@ServiceActivator(inputChannel="findOrganizationByGeneralSearchPageableChannel")
	public Page<Organization> findByGeneralSearch(String searchString,
			@Header(value="locale",required=false) String locale, @Header("pageable") Pageable pageable) {
		if(searchString.isEmpty()) return repo.findAll(pageable);
		return repo.findByTranslationNameContaining(searchString.toLowerCase(), pageable);		 
	}

    /**
     * @see org.devgateway.eudevfin.metadata.common.service.OrganizationService#findUsedAreaPaginated(String, String, Pageable)
     *
     * @param locale
     * @param searchString
     * @param page
     *
     * @return Page<Organization>
     */
    @ServiceActivator(inputChannel = "findUsedOrganizationPaginatedOrganizationChannel")
    @Transactional
    public Page<Organization> findUsedOrganizationPaginated(
            @Header("locale") String locale, String searchString,
            @Header("pageable") Pageable page) {
        Page<Organization> result = null;

        if (searchString.isEmpty()) {
            result = this.getRepo().findUsedOrganization(page);
        }
        else {
            result = this.getRepo().findUsedOrganizationByTranslationsNameIgnoreCase(locale, searchString.toLowerCase(), page);
        }

        return result;
    }
	
    public Organization findByCodeAndDonorCode(String code, String donorCode) {
        return this.repo.findByCodeAndDonorCode(code, donorCode);
    }
}
