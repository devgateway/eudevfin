package org.devgateway.eudevfin.auth.dao;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
import org.devgateway.eudevfin.auth.repository.PersistedUserRepository;
import org.devgateway.eudevfin.common.dao.AbstractDaoImpl;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

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
	 * 
	 * @see PersistedUserService#findByGeneralSearch(String, int, int)
	 */
	@ServiceActivator(inputChannel = "findByGeneralSearchUserChannel")
	public PagingHelper<PersistedUser> findByGeneralSearch(String searchString,
			@Header("pageNumber") int pageNumber,
			@Header("pageSize") int pageSize) {
		int realPageNumber = pageNumber - 1;
		PageRequest pageRequest = new PageRequest(realPageNumber, pageSize,
				null);
		return this.createPagingHelperFromPage(this.getRepo().findAll(
				pageRequest));
	}

 }