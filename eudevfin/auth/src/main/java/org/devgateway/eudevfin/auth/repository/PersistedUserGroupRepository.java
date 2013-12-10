/**
 * 
 */
package org.devgateway.eudevfin.auth.repository;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 *
 */
@Component
public interface PersistedUserGroupRepository extends
		PagingAndSortingRepository<PersistedUserGroup, Long> {

}
