package org.devgateway.eudevfin.auth.dao;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.UserService;
import org.devgateway.eudevfin.auth.repository.PersistedUserRepository;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.financial.dao.AbstractDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@Lazy(value=false)
public class UserDaoImplEndpoint extends AbstractDaoImpl<PersistedUser, PersistedUserRepository> {
	 
	@Autowired
	private PersistedUserRepository repo;
	
	/**
	 * @see UserService#findByUsername(String)
	 */
	@ServiceActivator(inputChannel="findByUsernameUserChannel")
	public PersistedUser findByUserName(String username) {
		return repo.findByUsername(username);
	}
	
	
	/**
	 * @see UserService#save(PersistedUser)
	 */
	@ServiceActivator(inputChannel="saveUserChannel")
	public PersistedUser saveUser(PersistedUser u) {
		repo.save(u);
		return u;
	}


	@Override
	protected PersistedUserRepository getRepo() {
		return repo;
	}
	
	/**
	 * @see UserService#findOne(Long)
	 */
	@Override
	@ServiceActivator(inputChannel = "findOneUserChannel")
	public PersistedUser findOne(Long id) {
		// TODO Auto-generated method stub
		return super.findOne(id);
	}
	
	/**
	 * 
	 * @see UserService#findByGeneralSearchPageable(String, int, int)
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