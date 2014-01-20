package org.devgateway.eudevfin.auth.dao;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
import org.devgateway.eudevfin.auth.repository.PersistedUserRepository;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * 
 * @author mihai
 * 
 */
@Component
@Lazy(value=false)
public class PersistedUserDaoImplEndpoint extends AbstractDaoImpl<PersistedUser, Long, PersistedUserRepository> {
	 
	@Autowired
	private PersistedUserRepository repo;
	
	/**
	 * @see PersistedUserService#findByUsername(String)
	 */
	@ServiceActivator(inputChannel="findByUsernameUserChannel")
	public NullableWrapper<PersistedUser> findByUserName(String username) {
		return newWrapper(repo.findByUsername(username));
	}
	
	
	/**
	 * @see PersistedUserService#save(PersistedUser)
	 */
	@ServiceActivator(inputChannel="saveUserChannel")
	public  NullableWrapper<PersistedUser>  saveUser(PersistedUser u) {
		return newWrapper(repo.save(u));		
	}


	@Override
	protected PersistedUserRepository getRepo() {
		return repo;
	}
	
	/**
	 * @see PersistedUserService#findOne(Long)
	 */
	@Override
	@ServiceActivator(inputChannel = "findOneUserChannel")
	public NullableWrapper<PersistedUser> findOne(Long id) {
		return super.findOne(id);
	}
	


	/**
	 * @see PersistedUserService#findByGeneralSearchPageable(String, int, int)
	 * @param searchString
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@Override
	@ServiceActivator(inputChannel = "findByGeneralSearchPageableUserChannel")
	public Page<PersistedUser> findByGeneralSearchPageable(String searchString,
			@Header(value="locale",required=false) String locale, @Header("pageable") Pageable pageable) {
		// TODO Auto-generated method stub
		return super.findByGeneralSearchPageable(searchString, locale, pageable);
	}
	
 }