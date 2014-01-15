/**
 * 
 */
package org.devgateway.eudevfin.dim.providers;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 * 
 */
@Component
public class PersistedUserChoiceProvider extends
		AbstractTextChoiceProvider<PersistedUser> {

	@Autowired
	protected PersistedUserService persistedUserService;

	private static final long serialVersionUID = -7413659137155284215L;

	public PersistedUserChoiceProvider() {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaynberg.wicket.select2.TextChoiceProvider#getDisplayText(java.lang
	 * .Object)
	 */
	@Override
	public String getDisplayText(PersistedUser choice) {
		return choice.getUsername();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaynberg.wicket.select2.TextChoiceProvider#getId(java.lang.Object)
	 */
	@Override
	public Object getId(PersistedUser choice) {
		return choice.getId();
	}


	@Override
	protected BaseEntityService<PersistedUser> getService() {
		return persistedUserService;
	}

}
