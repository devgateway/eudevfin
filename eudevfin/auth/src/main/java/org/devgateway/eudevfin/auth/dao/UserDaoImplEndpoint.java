package org.devgateway.eudevfin.auth.dao;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.repository.PersistedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@Lazy(value=false)
public class UserDaoImplEndpoint {
	 
	@Autowired
	private PersistedUserRepository repo;
	
	@ServiceActivator(inputChannel="getUserChannel")
	public PersistedUser findByUserName(String username) {
		return repo.findByUsername(username);
	}
	
	
	@ServiceActivator(inputChannel="createUserChannel")
	public PersistedUser saveUser(PersistedUser u) {
		repo.save(u);
		return u;
	}
 }