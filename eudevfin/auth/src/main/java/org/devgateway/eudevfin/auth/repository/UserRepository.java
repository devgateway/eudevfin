/**
 * 
 */
package org.devgateway.eudevfin.auth.repository;

import org.devgateway.eudevfin.auth.common.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 *
 */
@Component
public interface UserRepository extends
		PagingAndSortingRepository<User, String> {
}
