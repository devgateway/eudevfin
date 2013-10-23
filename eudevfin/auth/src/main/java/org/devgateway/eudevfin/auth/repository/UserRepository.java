/**
 * 
 */
package org.devgateway.eudevfin.auth.repository;

import org.devgateway.eudevfin.auth.common.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * @author mihai
 *
 */
public interface UserRepository extends
		PagingAndSortingRepository<User, Long> {
	
	
	@Override
	@ServiceActivator(inputChannel="getUserChannel")
	User findOne(Long username);
	
	@Override
	@ServiceActivator(inputChannel="createUserChannel")
	public <S extends User> S save(S someuser);

}
