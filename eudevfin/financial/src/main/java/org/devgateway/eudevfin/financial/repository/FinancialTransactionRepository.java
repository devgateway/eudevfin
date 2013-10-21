/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public interface FinancialTransactionRepository extends
		PagingAndSortingRepository<FinancialTransaction, Long> {

}
