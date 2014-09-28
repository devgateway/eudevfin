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
package org.devgateway.eudevfin.financial.dao;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.repository.CustomFinancialTransactionRepository;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author Alex,mihai
 *
 */
@Component
@Lazy(value=false)
public class CustomFinancialTransactionDao
		extends
		AbstractDaoImpl<CustomFinancialTransaction, Long, CustomFinancialTransactionRepository> {
	
	@Autowired
	private CustomFinancialTransactionRepository repo;


	/**
	 * @see FinancialTransactionService#findBySearchFormPageable(Integer, Category, Area, String, String, Organization, String, Pageable)
	 * @param year
	 * @param sector
	 * @param recipient
	 * @param searchString
	 * @param formType
	 * @param extendingAgency
	 * @param locale
	 * @param pageable
	 * @return
	 */
	@ServiceActivator(inputChannel = "findTransactionBySearchFormPageableChannel")
	public Page<CustomFinancialTransaction> findBySearchFormPageable(
			@Header(value = "year", required = false) final LocalDateTime year,
			@Header(value = "sector", required = false) final Category sector,
			@Header(value = "recipient", required = false) final Area recipient,
			@Header(value = "searchString", required = false) final String searchString,
			@Header(value = "formType", required = false) final String formType,
			@Header(value = "extendingAgency", required = false) final Organization extendingAgency,
			@Header(value = "locale", required = false) final String locale, final Pageable pageable) {
		return this.getRepo().performSearch(year, sector, recipient, searchString, formType, extendingAgency, pageable);
	}
	
/**
 * @see CustomFinancialTransactionService#findByDonorIdCrsIdActive(String, String, Boolean, String, Pageable)
 * @param crsIdSearch
 * @param donorIdSearch
 * @param locale
 * @param pageable
 * @return
 */
	@ServiceActivator(inputChannel = "findTransactionByDonorIdCrsIdActiveChannel")
	public Page<CustomFinancialTransaction> findByDonorIdCrsIdActive(
			@Header(value = "donorIdSearch", required = false) String donorIdSearch,
			@Header(value = "crsIdSearch", required = false) String crsIdSearch,
			@Header(value = "active", required = false) Boolean active, 
		    @Header(value = "locale", required = false) String locale, 
		    Pageable pageable) {
		return this.getRepo().performSearchByDonorIdCrsIdActive(donorIdSearch,crsIdSearch,active,locale,pageable);
	}
	
	
	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.common.dao.AbstractDaoImpl#getRepo()
	 */
	@Override
	protected CustomFinancialTransactionRepository getRepo() {
		return this.repo;
	}
	
	/**
	 * @see CustomFinancialTransactionService#findByDraftAndPersistedUserGroupPageable(Boolean, PersistedUserGroup, Pageable)
	 * @param draft
	 * @param persistedUserGroup
	 * @param pageable
	 * @return
	 */
	@ServiceActivator(inputChannel="findCustomTransactionByDraftAndPersistedUserGroupPageableChannel")
	public Page<CustomFinancialTransaction> findByDraftAndPersistedUserGroupPageable(final Boolean draft,
			@Header("persistedUserGroup") final PersistedUserGroup persistedUserGroup,
			@Header("pageable") final Pageable pageable) {
		return this.getRepo().findByDraftAndPersistedUserGroupAndApprovedFalse(draft, persistedUserGroup, pageable);
	}
	
	/**
	 * @see CustomFinancialTransactionService#findByDraftPageable(Boolean, Pageable)
	 * @param draft
	 * @param pageable
	 * @return
	 */
	@ServiceActivator(inputChannel="findCustomTransactionByDraftPageableChannel")
	public Page<CustomFinancialTransaction> findByDraftPageable(final Boolean draft,
			@Header("pageable") final Pageable pageable) {
		return this.getRepo().findByDraftAndApprovedFalse(draft, pageable);
	}
	
	/**
	 * @see CustomFinancialTransactionService#findByApprovedPageable(Boolean, Pageable)
	 * @param draft
	 * @param pageable
	 * @return
	 */
	@ServiceActivator(inputChannel="findCustomTransactionByApprovedPageableChannel")
	public Page<CustomFinancialTransaction> findByApprovedPageable(final Boolean draft,
			@Header("pageable") final Pageable pageable) {
		return this.getRepo().findByApproved(draft, pageable);
	}
	
	/**
	 * @see CustomFinancialTransactionService#findByApprovedAndPersistedUserGroupPageable(Boolean, PersistedUserGroup, Pageable)
	 * @param approved
	 * @param persistedUserGroup
	 * @param pageable
	 * @return
	 */
	@ServiceActivator(inputChannel="findCustomTransactionByApprovedAndPersistedUserGroupPageableChannel")
	public Page<CustomFinancialTransaction> findByApprovedAndPersistedUserGroupPageable(final Boolean approved,
			@Header("persistedUserGroup") final PersistedUserGroup persistedUserGroup,
			@Header("pageable") final Pageable pageable) {
		return this.getRepo().findByApprovedAndPersistedUserGroup(approved, persistedUserGroup, pageable);
	}
	
	/**
	 * @see CustomFinancialTransactionService#findByReportingYearAndDraftFalse(Integer)
	 * @param year
	 * @return
	 */
	@ServiceActivator(inputChannel="findCustomTransactionByReportingYearAndDraftFalseChannel")
	public List<CustomFinancialTransaction> findByReportingYearAndDraftFalse(final Integer year) {
		final LocalDateTime start	= new LocalDateTime(year, 1, 1, 0, 0);
        final LocalDateTime end = new LocalDateTime(year, 12, 31, 23, 59);
		
		return this.getRepo().findByReportingYearBetweenAndDraftFalse(start, end);
	}
	
	/**
	 * @see CustomFinancialTransactionService#findByReportingYearAndDraftFalseAndFormTypeNotIn(Integer)
	 * @param year
	 * @return
	 */
	@ServiceActivator(inputChannel = "findCustomTransactionByReportingYearAndDraftFalseAndFormTypeNotInChannel")
	public List<CustomFinancialTransaction> findByReportingYearAndDraftFalseAndFormTypeNotIn(final Integer year,
			@Header("notFormType") final Collection<String> notFormType) {
		final LocalDateTime start = new LocalDateTime(year, 1, 1, 0, 0);
        final LocalDateTime end = new LocalDateTime(year, 12, 31, 23, 59);

		return this.getRepo().findByReportingYearBetweenAndDraftFalseAndFormTypeNotIn(start, end, notFormType);
	}
	
	/**
	 * @see CustomFinancialTransactionService#findByReportingYearAndDraftFalseAndFormTypeIn(Integer)
	 * @param year
	 * @return
	 */
	@ServiceActivator(inputChannel = "findCustomTransactionByReportingYearAndApprovedTrueAndFormTypeInChannel")
	public List<CustomFinancialTransaction> findByReportingYearAndApprovedTrueAndFormTypeIn(final Integer year,
			@Header("notFormType") final Collection<String> notFormType) {
		final LocalDateTime start = new LocalDateTime(year, 1, 1, 0, 0);
		final LocalDateTime end = new LocalDateTime(year, 12, 31, 23, 59);

		return this.getRepo().findByReportingYearBetweenAndApprovedTrueAndFormTypeIn(start, end, notFormType);
	}
	
	/**
	 * @see CustomFinancialTransactionService#findByReportingYearAndDraftFalseAndFormTypeIn(Integer)
	 * @param year
	 * @return
	 */
	@ServiceActivator(inputChannel = "findCustomTransactionByApprovedTrueAndFormTypeInlOrderByCrsIdentificationNumberAscCreatedDateAscChannel")
	public List<CustomFinancialTransaction> findByApprovedTrueAndFormTypeIn(final Collection<String> notFormType) {
		return this.getRepo().findByApprovedTrueAndFormTypeInOrderByCrsIdentificationNumberAscCreatedDateAsc(notFormType);
	}
	
	@ServiceActivator(inputChannel="findDistinctReportingYearsInTransactionChannel")
	public List<Integer> findDistinctReportingYears(){
		final List<Integer> ret	= this.getRepo().findDistinctReportingYears();
		return ret;
	}

    @ServiceActivator(inputChannel="findDistinctStartingYearsInTransactionChannel")
    public List<Integer> findDistinctStartingYears(){
        final List<Integer> ret	= this.getRepo().findDistinctStartingYears();
        return ret;
    }

    @ServiceActivator(inputChannel="findDistinctCompletitionYearsInTransactionChannel")
    public List<Integer> findDistinctCompletitionYears(){
        final List<Integer> ret	= this.getRepo().findDistinctCompletitionYears();
        return ret;
    }

    @ServiceActivator(inputChannel="findDistinctReportingGeopraphyInTransactionChannel")
    public List<String> findDistinctReportingGeopraphy(){
        final List<String> ret	= this.getRepo().findDistinctReportingGeopraphy();
        return ret;
    }

    @ServiceActivator(inputChannel="findDistinctDonorProjectNumberChannel")
    public List<String> findDistinctDonorProjectNumber(){
        final List<String> ret	= this.getRepo().findDistinctDonorProjectNumber();
        return ret;
    }

    @ServiceActivator(inputChannel="findDistinctCRSIdChannel")
    public List<String> findDistinctCRSId(){
        final List<String> ret	= this.getRepo().findDistinctCRSId();
        return ret;
    }
}
