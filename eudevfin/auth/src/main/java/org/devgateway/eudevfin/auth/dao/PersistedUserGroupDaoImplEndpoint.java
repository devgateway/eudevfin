/**
 * 
 */
package org.devgateway.eudevfin.auth.dao;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.auth.common.service.PersistedUserGroupService;
import org.devgateway.eudevfin.auth.repository.PersistedUserGroupRepository;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.financial.dao.AbstractDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 *
 */
@Component
@Lazy(value=false)
public class PersistedUserGroupDaoImplEndpoint extends
		AbstractDaoImpl<PersistedUserGroup, PersistedUserGroupRepository> {

	@Autowired
	protected PersistedUserGroupRepository persistedUserGroupRepository;
	
	
	/**
	 * 
	 */
	public PersistedUserGroupDaoImplEndpoint() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.financial.dao.AbstractDaoImpl#getRepo()
	 */
	@Override
	protected PersistedUserGroupRepository getRepo() {
		return persistedUserGroupRepository;
	}
	
	@ServiceActivator(inputChannel = "findAllUserGroupChannel")
	@Override
	public Iterable<PersistedUserGroup> findAll() {
		// TODO Auto-generated method stub
		return super.findAll();
	}
	
	
	@ServiceActivator(inputChannel = "findByIdUserGroupChannel")
	@Override
	public PersistedUserGroup findOne(Long id) {
		// TODO Auto-generated method stub
		return super.findOne(id);
	}
	
	/**
	 * @see PersistedUserGroupService#findByGeneralSearchPageable(String, int, int)
	 */
	@ServiceActivator(inputChannel = "findByGeneralSearchUserGroupChannel")
	public PagingHelper<PersistedUserGroup> findByGeneralSearch(String searchString,
			@Header("pageNumber") int pageNumber,
			@Header("pageSize") int pageSize) {
		int realPageNumber = pageNumber - 1;
		PageRequest pageRequest = new PageRequest(realPageNumber, pageSize,
				null);
		return this.createPagingHelperFromPage(this.getRepo().findAll(
				pageRequest));
	}


}
