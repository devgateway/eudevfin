/**
 * 
 */
package org.devgateway.eudevfin.dim.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.auth.common.service.PersistedUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;

/**
 * @author mihai
 *
 */
@Component
public class PersistedUserGroupChoiceProvider extends
		TextChoiceProvider<PersistedUserGroup> {

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

	/* (non-Javadoc)
	 * @see com.vaynberg.wicket.select2.ChoiceProvider#query(java.lang.String, int, com.vaynberg.wicket.select2.Response)
	 */
	@Override
	public void query(String term, int page,
			Response<PersistedUserGroup> response) {
		Iterable<PersistedUserGroup> findAll = persistedUserGroupService
				.findAll();
		CollectionUtils.<PersistedUserGroup> addAll(response.getResults(),
				findAll);
	}

	/* (non-Javadoc)
	 * @see com.vaynberg.wicket.select2.ChoiceProvider#toChoices(java.util.Collection)
	 */
	@Override
	public Collection<PersistedUserGroup> toChoices(Collection<String> ids) {
		List<PersistedUserGroup> returnable = new ArrayList<PersistedUserGroup>();
		for (String string : ids)
			returnable.add(persistedUserGroupService.findById(Long.parseLong(string)).getEntity());
		return returnable;
	}

}
