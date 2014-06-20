/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Alex,mihai
 *
 */
public interface FinancialTransactionRepository extends
		JpaRepository<FinancialTransaction, Long> {
	
//	List<FinancialTransaction> findByDescriptionOrderByIdAsc(String description);
	List<FinancialTransaction> findByExtendingAgencyId(Long orgId);
	
	Page<FinancialTransaction> findBySectorCode(String sectorCode, Pageable pageable );
	
	//@Query(" select trn.parent from FinancialTransactionTranslation trn where lower(trn.description) like %?1%")
	@Query ("select tx from FinancialTransaction tx join tx.translations trn where lower(trn.description) like %?1% or lower(trn.shortDescription) like %?1% ")
	Page<FinancialTransaction> findByTranslationsDescriptionContaining(String searchString, Pageable pageable);
	
	//@Query ("select tx from FinancialTransaction tx where tx.reportingYear=?1 and tx.typeOfFlow.code='TYPE_OF_FLOW##40'"+CategoryConstants.TypeOfFlow.NON_FLOW)
	List<FinancialTransaction> findByReportingYearAndTypeOfFlowCode(LocalDateTime reportingYear,String typeOfFlowCode);

	@Query("select tx from FinancialTransaction tx join tx.translations trn where (lower(trn.description) like %:searchString% or lower(trn.shortDescription) like %:searchString%) or "
			+ "tx.reportingYear=:year or tx.sector=:sector or tx.recipient=:recipient or tx.formType=:formType or tx.extendingAgency=:extendingAgency")
	Page<FinancialTransaction> findBySearchFormPageable(@Param("year") LocalDateTime year,
			@Param("sector") Category sector, @Param("recipient") Area recipient,
			@Param("searchString") String searchString, @Param("formType") String formType,
			@Param("extendingAgency") Organization extendingAgency, Pageable pageable);
}
