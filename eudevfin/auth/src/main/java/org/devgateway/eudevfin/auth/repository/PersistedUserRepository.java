/**
 * 
 */
package org.devgateway.eudevfin.auth.repository;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 *
 */
@Component
public interface PersistedUserRepository extends
		PagingAndSortingRepository<PersistedUser, Long> {

	PersistedUser findByUsername(String username);
}
