/**
 * 
 */
package org.devgateway.eudevfin.dim.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;

/**
 * @author mihai
 * 
 */
@Component
public class PersistedUserChoiceProvider extends
		TextChoiceProvider<PersistedUser> {

	@Value("#{commonProperties['sel.defaultSelectorPageSize']}")
	protected Integer pageSize;

	@Autowired
	protected PersistedUserService persistedUserService;

	private static final long serialVersionUID = -7413659137155284215L;

	public PersistedUserChoiceProvider() {
		// TODO Auto-generated constructor stub
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaynberg.wicket.select2.ChoiceProvider#query(java.lang.String,
	 * int, com.vaynberg.wicket.select2.Response)
	 */
	@Override
	public void query(String term, int page, Response<PersistedUser> response) {
		PagingHelper<PersistedUser> findByGeneralSearchPageable = persistedUserService
				.findByGeneralSearchPageable(term, page+1, pageSize);
		response.addAll(findByGeneralSearchPageable.getEntities());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaynberg.wicket.select2.ChoiceProvider#toChoices(java.util.Collection
	 * )
	 */
	@Override
	public Collection<PersistedUser> toChoices(Collection<String> ids) {
		List<PersistedUser> returnable = new ArrayList<PersistedUser>();
		for (String string : ids)
			returnable.add(persistedUserService.findOne(Long.parseLong(string)).getEntity());
		return returnable;
	}

}
