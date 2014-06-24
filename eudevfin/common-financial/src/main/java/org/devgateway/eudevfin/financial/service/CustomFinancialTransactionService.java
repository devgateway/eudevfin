/**
 * 
 */
package org.devgateway.eudevfin.financial.service;

import java.util.Collection;
import java.util.List;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.Payload;

/**
 * @author Alex,mihai
 *
 */
public interface CustomFinancialTransactionService extends BaseEntityService<CustomFinancialTransaction> {
	public Page<CustomFinancialTransaction> findByDraftAndPersistedUserGroupPageable(Boolean draft,
			@Header("persistedUserGroup") PersistedUserGroup persistedUserGroup, @Header("pageable") Pageable pageable);
	
	public Page<CustomFinancialTransaction> findByApprovedAndPersistedUserGroupPageable(Boolean approved,
			@Header("persistedUserGroup") PersistedUserGroup persistedUserGroup, @Header("pageable") Pageable pageable);
	
	public Page<CustomFinancialTransaction> findByDraftPageable(final Boolean draft,
			@Header("pageable") final Pageable pageable);
	
	public Page<CustomFinancialTransaction> findByApprovedPageable(final Boolean draft,
			@Header("pageable") final Pageable pageable);
	
	public List<CustomFinancialTransaction> findByReportingYearAndDraftFalse(final Integer year);
	
	public List<CustomFinancialTransaction> findByReportingYearAndDraftFalseAndFormTypeNotIn(final Integer year,
			@Header("notFormType") Collection<String> notFormType);
	
	public List<CustomFinancialTransaction> findByReportingYearAndApprovedTrueAndFormTypeIn(final Integer year,
			@Header("notFormType") Collection<String> notFormType);
	
	@Payload("new java.util.Date()")
	public List<Integer> findDistinctReportingYears();

    @Payload("new java.util.Date()")
    public List<Integer> findDistinctStartingYears();

    @Payload("new java.util.Date()")
    public List<Integer> findDistinctCompletitionYears();

    @Payload("new java.lang.String()")
    public List<String> findDistinctReportingGeopraphy();

	
	public Page<FinancialTransaction> findBySearchFormPageable(
			@Header(value = "year", required = false) LocalDateTime year,
			@Header(value = "sector", required = false) Category sector,
			@Header(value = "recipient", required = false) Area recipient,
			@Header(value = "searchString", required = false) String searchString,
			@Header(value = "formType", required = false) String formType,			
			@Header(value = "extendingAgency", required = false) Organization extendingAgency,
		    @Header(value = "locale", required = false) String locale, 
		    Pageable pageable);
}
