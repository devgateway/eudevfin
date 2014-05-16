/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.Collection;
import java.util.List;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Alex
 *
 */
public interface CustomFinancialTransactionRepository extends
		JpaRepository<CustomFinancialTransaction, Long> {
	
	Page<CustomFinancialTransaction> findByDraft(Boolean draft, Pageable pageable );
	
	Page<CustomFinancialTransaction> findByApproved(Boolean approved, Pageable pageable );
	
	Page<CustomFinancialTransaction> findByDraftAndPersistedUserGroup(Boolean draft,PersistedUserGroup persistedUserGroup, Pageable pageable );
	
	Page<CustomFinancialTransaction> findByApprovedAndPersistedUserGroup(Boolean approved,PersistedUserGroup persistedUserGroup, Pageable pageable );
	
	List<CustomFinancialTransaction> findByReportingYearBetweenAndDraftFalse(LocalDateTime start, LocalDateTime end);
	
	List<CustomFinancialTransaction> findByReportingYearBetweenAndDraftFalseAndFormTypeNotIn(LocalDateTime start,
			LocalDateTime end, Collection<String> notFormType);

	@Query ("select distinct year(ctx.reportingYear) from CustomFinancialTransaction ctx where ctx.reportingYear IS NOT NULL and ctx.draft = false ")
	List<Integer> findDistinctReportingYears();

    @Query ("select distinct year(ctx.expectedStartDate) from CustomFinancialTransaction ctx where ctx.expectedStartDate IS NOT NULL and ctx.draft = false ")
    List<Integer> findDistinctStartingYears();

    @Query ("select distinct year(ctx.expectedCompletionDate) from CustomFinancialTransaction ctx where ctx.expectedCompletionDate IS NOT NULL and ctx.draft = false ")
    List<Integer> findDistinctCompletitionYears();

    @Query ("select distinct ct.name from Category c, CategoryTranslation ct where c.code like 'GEOGRAPHY##%' and ct.parent  = c.id ")
    List<String> findDistinctReportingGeopraphy();
}
