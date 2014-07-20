/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Alex
 *
 */
public interface OrganizationRepository extends
		JpaRepository<Organization, Long> {
	
	
	@Query(" select distinct(trn.parent) from OrganizationTranslation trn where trn.locale=?1 AND lower(trn.name) like %?2% ")
	Page<Organization> findByTranslationLocaleAndTranslationNameContaining(String locale, String searchString,Pageable pageable);

	@Query(" select distinct(trn.parent) from OrganizationTranslation trn "
			+ "where lower(trn.name) like %?1% or lower(trn.donorName) like %?1% or lower(trn.parent.acronym) like %?1%")
	Page<Organization> findByTranslationNameContaining(String searchString,Pageable pageable);
	
	Organization findByCodeAndDonorCode(String code, String donorCode);
	
	Page<Organization> findByDacFalse(Pageable pageable);
	
	Organization findByCode(String code);
	
    @Query(" select distinct org from CustomFinancialTransaction ctx join ctx.extendingAgency org " +
            "where ctx.approved = true")
    Page<Organization> findUsedOrganization(Pageable page);

    @Query(" select distinct org from OrganizationTranslation trn, CustomFinancialTransaction ctx join ctx.extendingAgency org " +
            "where ctx.approved = true and trn.parent = org.id AND trn.locale=?1 AND lower(trn.name) like %?2% ")
    Page<Organization> findUsedOrganizationByTranslationsNameIgnoreCase(String locale, String term, Pageable page);
}
