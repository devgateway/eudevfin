package org.devgateway.eudevfin.auth.common.service;

import org.devgateway.eudevfin.auth.common.domain.User;
import org.springframework.integration.annotation.Gateway;
import org.springframework.stereotype.Component;


/**
 * 
 * @author mihai
 *
 */
public interface UserService {
	
	@Gateway(requestChannel="createUserChannel",replyChannel="replyCreateUserChannel")
	User createUser(User u);
	
	@Gateway(requestChannel="getUserChannel",replyChannel="replyGetUserChannel")
	User getUserByUsername(String username);

}