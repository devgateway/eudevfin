/**
 * 
 */
package org.devgateway.eudevfin.auth.repository;

import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 *
 */
@Component
public interface PersistedAuthorityRepository extends
		PagingAndSortingRepository<PersistedAuthority, String> {	
}
