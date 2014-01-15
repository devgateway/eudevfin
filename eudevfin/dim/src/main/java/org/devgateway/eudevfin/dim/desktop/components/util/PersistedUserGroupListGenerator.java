/** 
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components.util;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.auth.common.service.PersistedUserGroupService;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;

/**
 * @author mihai
 *
 */
public class PersistedUserGroupListGenerator implements
		ListGeneratorInterface<PersistedUserGroup> {

	private static final long serialVersionUID = -6936731401076768620L;
	private PersistedUserGroupService userGroupService;
	private String searchString;
	
	public PersistedUserGroupListGenerator(PersistedUserGroupService userGroupService,String searchString) {
		this.userGroupService=userGroupService;
		this.searchString=searchString;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface#getResultsList(int, int)
	 */
	@Override
	public PagingHelper<PersistedUserGroup> getResultsList(int pageNumber,
			int pageSize) {
		return userGroupService.findByGeneralSearchPageable(searchString,null, pageNumber, pageSize,null);
	}

}
