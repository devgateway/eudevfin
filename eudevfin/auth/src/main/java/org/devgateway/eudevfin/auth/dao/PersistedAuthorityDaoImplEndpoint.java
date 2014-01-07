package org.devgateway.eudevfin.auth.dao;

import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.devgateway.eudevfin.auth.repository.PersistedAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@Lazy(value=false)
public class PersistedAuthorityDaoImplEndpoint {
	 
	@Autowired
	private PersistedAuthorityRepository repo;
	
	@ServiceActivator(inputChannel="findOnePersistedAuthorityChannel")
	public PersistedAuthority findOne(String id) {
		return repo.findOne(id);
	}
	
	
	@ServiceActivator(inputChannel="savePersistedAuthorityChannel")
	public PersistedAuthority saveAuthority(PersistedAuthority a) {
		repo.save(a);
		return a;
	}
	
	@ServiceActivator(inputChannel="findAllPersistedAuthorityChannel")
	public Iterable<PersistedAuthority> findAll(PersistedAuthority a) {
		Iterable<PersistedAuthority> findAll = repo.findAll();
		return findAll;
	}
	
	
	
 }