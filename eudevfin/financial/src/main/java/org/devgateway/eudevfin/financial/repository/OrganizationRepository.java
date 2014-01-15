/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public interface OrganizationRepository extends
		PagingAndSortingRepository<Organization, Long> {
	
	@Query(" select trn.parent from OrganizationTranslation trn where trn.locale=?1 AND lower(trn.name) like %?2% ")
	List<Organization> findByTranslationLocaleAndTranslationNameContaining(String locale, String searchString);

}
