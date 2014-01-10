/** 
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components.util;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.UserService;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;

/**
 * @author mihai
 *
 */
public class PersistedUserListGenerator implements
		ListGeneratorInterface<PersistedUser> {

	private static final long serialVersionUID = -6936731401076768620L;
	private UserService userService;
	private String searchString;
	
	public PersistedUserListGenerator(UserService userService,String searchString) {
		this.userService=userService;
		this.searchString=searchString;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface#getResultsList(int, int)
	 */
	@Override
	public PagingHelper<PersistedUser> getResultsList(int pageNumber,
			int pageSize) {
		return userService.findByGeneralSearch(searchString, pageNumber, pageSize);
	}

}