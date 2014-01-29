/**
 * 
 */
package org.devgateway.eudevfin.mcm.providers;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.auth.common.service.PersistedUserGroupService;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.ui.common.providers.AbstractTextChoiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 *
 */
@Component
public class PersistedUserGroupChoiceProvider extends AbstractTextChoiceProvider<PersistedUserGroup> {

	private static final long serialVersionUID = 7528820465678340874L;
	@Autowired
	protected PersistedUserGroupService persistedUserGroupService;
	
	/**
	 * 
	 */
	public PersistedUserGroupChoiceProvider() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.vaynberg.wicket.select2.TextChoiceProvider#getDisplayText(java.lang.Object)
	 */
	@Override
	public String getDisplayText(PersistedUserGroup choice) {
		return choice.getName();
	}

	/* (non-Javadoc)
	 * @see com.vaynberg.wicket.select2.TextChoiceProvider#getId(java.lang.Object)
	 */
	@Override
	public Object getId(PersistedUserGroup choice) {
		return choice.getId();
	}

	@Override
	protected BaseEntityService<PersistedUserGroup> getService() {
		return persistedUserGroupService;
	}


}
