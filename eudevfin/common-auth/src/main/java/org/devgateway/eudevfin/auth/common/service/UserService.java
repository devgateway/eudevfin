package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.User;
import org.springframework.integration.annotation.Gateway;


/**
 * 
 * @author mihai
 *
 */
public interface UserService {
	
	@Gateway(requestChannel="createUserChannel")
	User createUser(User u);
	
	@Gateway(requestChannel="getUserChannel")
	User getUserByUsername(String username);

}