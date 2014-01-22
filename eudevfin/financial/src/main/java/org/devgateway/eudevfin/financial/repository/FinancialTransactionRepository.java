/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public interface FinancialTransactionRepository extends
		PagingAndSortingRepository<FinancialTransaction, Long> {
	
//	List<FinancialTransaction> findByDescriptionOrderByIdAsc(String description);
	List<FinancialTransaction> findByReportingOrganizationId(Long orgId);
	
	Page<FinancialTransaction> findBySectorCode(String sectorCode, Pageable pageable );
	
	//@Query(" select trn.parent from FinancialTransactionTranslation trn where lower(trn.description) like %?1%")
	@Query ("select tx from FinancialTransaction tx join tx.translations trn where lower(trn.description) like %?1% or lower(trn.shortDescription) like %?1% ")
	Page<FinancialTransaction> findByTranslationsDescriptionContaining(String searchString, Pageable pageable);
}
