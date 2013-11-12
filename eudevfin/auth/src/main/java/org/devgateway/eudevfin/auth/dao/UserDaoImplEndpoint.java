package org.devgateway.eudevfin.auth.dao;

import org.devgateway.eudevfin.auth.common.domain.User;
import org.devgateway.eudevfin.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImplEndpoint {
	 
	@Autowired
	private UserRepository repo;
	
	@ServiceActivator(inputChannel="getUserChannel")
	public User findByUserName(String username) {
		return repo.findOne(username);
	}
	
	
	@ServiceActivator(inputChannel="createUserChannel")
	public User saveUser(User u) {
		repo.save(u);
		return u;
	}
 }