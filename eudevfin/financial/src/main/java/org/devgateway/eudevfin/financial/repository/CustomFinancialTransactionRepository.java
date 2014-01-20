/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public interface CustomFinancialTransactionRepository extends
		PagingAndSortingRepository<CustomFinancialTransaction, Long> {
	
	Page<CustomFinancialTransaction> findByDraft(Boolean draft, Pageable pageable );

}
