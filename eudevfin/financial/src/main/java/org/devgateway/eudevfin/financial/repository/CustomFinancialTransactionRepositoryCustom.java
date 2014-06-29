/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author mihai
 * 
 * We go with dynamic JPQL instead of criteria specification query, because we need to define Metamodels for translations
 * and it becomes a pain...
 */
public interface CustomFinancialTransactionRepositoryCustom  {

	Page<CustomFinancialTransaction> performSearch(LocalDateTime year, Category sector, Area recipient,
			String searchString, String formType, Organization extendingAgency, Pageable pageable);
	
	Page<CustomFinancialTransaction> performSearchByDonorIdCrsIdActive(String crsIdSearch, String donorIdSearch,
			Boolean active, String locale, Pageable pageable);
}
