package org.devgateway.eudevfin.auth.dao;

import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.devgateway.eudevfin.auth.repository.PersistedAuthorityRepository;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@Lazy(value=false)
public class PersistedAuthorityDaoImplEndpoint extends AbstractDaoImpl<PersistedAuthority, String, PersistedAuthorityRepository> {
	 
	@Autowired
	private PersistedAuthorityRepository repo;
	
	@ServiceActivator(inputChannel="findOnePersistedAuthorityChannel")
	public NullableWrapper<PersistedAuthority> findOne(String id) {
		return newWrapper(repo.findOne(id));
	}
	
	
	@ServiceActivator(inputChannel="savePersistedAuthorityChannel")
	public  NullableWrapper<PersistedAuthority> saveAuthority(PersistedAuthority a) {
		return newWrapper(repo.save(a));		
	}
	
	@ServiceActivator(inputChannel="findAllPersistedAuthorityChannel")
	public Iterable<PersistedAuthority> findAll() {
		Iterable<PersistedAuthority> findAll = repo.findAll();
		return findAll;
	}


	@Override
	protected PersistedAuthorityRepository getRepo() {
		return repo;
	}
	
	
	
 }