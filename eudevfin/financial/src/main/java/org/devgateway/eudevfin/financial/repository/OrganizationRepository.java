/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import org.devgateway.eudevfin.financial.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 *
 */
public interface OrganizationRepository extends
		PagingAndSortingRepository<Organization, Long> {
	
	
	@Query(" select distinct(trn.parent) from OrganizationTranslation trn where trn.locale=?1 AND lower(trn.name) like %?2% ")
	Page<Organization> findByTranslationLocaleAndTranslationNameContaining(String locale, String searchString,Pageable pageable);

	@Query(" select distinct(trn.parent) from OrganizationTranslation trn where lower(trn.name) like %?1% ")
	Page<Organization> findByTranslationNameContaining(String searchString,Pageable pageable);

}
