/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public interface FinancialTransactionRepository extends
		PagingAndSortingRepository<FinancialTransaction, Long> {
	
//	List<FinancialTransaction> findByDescriptionOrderByIdAsc(String description);
	List<FinancialTransaction> findByReportingOrganizationId(Long orgId);
}
