/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.util.CategoryConstants;
import org.joda.time.LocalDateTime;
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
	List<FinancialTransaction> findByExtendingAgencyId(Long orgId);
	
	Page<FinancialTransaction> findBySectorCode(String sectorCode, Pageable pageable );
	
	//@Query(" select trn.parent from FinancialTransactionTranslation trn where lower(trn.description) like %?1%")
	@Query ("select tx from FinancialTransaction tx join tx.translations trn where lower(trn.description) like %?1% or lower(trn.shortDescription) like %?1% ")
	Page<FinancialTransaction> findByTranslationsDescriptionContaining(String searchString, Pageable pageable);
	
	//@Query ("select tx from FinancialTransaction tx where tx.reportingYear=?1 and tx.typeOfFlow.code='TYPE_OF_FLOW##40'"+CategoryConstants.TypeOfFlow.NON_FLOW)
	List<FinancialTransaction> findByReportingYearAndTypeOfFlowCode(LocalDateTime reportingYear,String typeOfFlowCode);

}
