package org.devgateway.eudevfin.auth.dao;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
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
	
	@ServiceActivator(inputChannel="findByUsernameUserChannel")
	public PersistedUser findByUserName(String username) {
		return repo.findByUsername(username);
	}
	
	
	@ServiceActivator(inputChannel="saveUserChannel")
	public PersistedUser saveUser(PersistedUser u) {
		repo.save(u);
		return u;
	}


	@Override
	protected PersistedUserRepository getRepo() {
		return repo;
	}
	
	
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