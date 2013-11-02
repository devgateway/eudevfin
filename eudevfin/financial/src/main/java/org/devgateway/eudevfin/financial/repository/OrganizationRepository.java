/**
 * 
 */
package org.devgateway.eudevfin.financial.repository;

import org.devgateway.eudevfin.financial.Organization;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author alex
 *
 */
public interface OrganizationRepository extends
		PagingAndSortingRepository<Organization, Long> {

}
