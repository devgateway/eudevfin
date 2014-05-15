/**
 * 
 */
package org.devgateway.eudevfin.financial.dao;

import java.util.Collection;
import java.util.List;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.repository.CustomFinancialTransactionRepository;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

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
			@Header("persistedUserGroup") PersistedUserGroup persistedUserGroup,
			@Header("pageable") final Pageable pageable) {
		return this.getRepo().findByDraftAndPersistedUserGroup(draft, persistedUserGroup, pageable);
	}
	
	@ServiceActivator(inputChannel="findCustomTransactionByDraftPageableChannel")
	public Page<CustomFinancialTransaction> findByDraftPageable(final Boolean draft,
			@Header("pageable") final Pageable pageable) {
		return this.getRepo().findByDraft(draft, pageable);
	}
	
	
	/**
	 * @see CustomFinancialTransactionService#findByReportingYearAndDraftFalse(Integer)
	 * @param year
	 * @return
	 */
	@ServiceActivator(inputChannel="findCustomTransactionByReportingYearAndDraftFalseChannel")
	public List<CustomFinancialTransaction> findByReportingYearAndDraftFalse(final Integer year) {
		final LocalDateTime start	= new LocalDateTime(year, 1, 1, 0, 0);
		final LocalDateTime end		= new LocalDateTime(year+1, 1, 1, 0, 0);
		
		return this.getRepo().findByReportingYearBetweenAndDraftFalse(start, end);
	}
	
	/**
	 * @see CustomFinancialTransactionService#findByReportingYearAndDraftFalseAndFormTypeNotIn(Integer)
	 * @param year
	 * @return
	 */
	@ServiceActivator(inputChannel = "findCustomTransactionByReportingYearAndDraftFalseAndFormTypeNotInChannel")
	public List<CustomFinancialTransaction> findByReportingYearAndDraftFalseAndFormTypeNotIn(final Integer year,
			@Header("notFormType") Collection<String> notFormType) {
		final LocalDateTime start = new LocalDateTime(year, 1, 1, 0, 0);
		final LocalDateTime end = new LocalDateTime(year + 1, 1, 1, 0, 0);

		return this.getRepo().findByReportingYearBetweenAndDraftFalseAndFormTypeNotIn(start, end, notFormType);
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

}
