/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/** 
 * 
 */
package org.devgateway.eudevfin.mcm.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.devgateway.eudevfin.auth.common.service.PersistedAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;

/**
 * @author mihai
 * 
 */
@Component
public class PersistedAuthorityChoiceProvider extends
		TextChoiceProvider<PersistedAuthority> {

	private static final long serialVersionUID = 6601111337536432388L;
	
	@Autowired
	private PersistedAuthorityService persistedAuthorityService;

	

	/**
	 * @param persistedAuthorityService 
	 * 
	 */
	public PersistedAuthorityChoiceProvider() {
		super();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaynberg.wicket.select2.ChoiceProvider#query(java.lang.String,
	 * int, com.vaynberg.wicket.select2.Response)
	 */
	@Override
	public void query(String term, int page,
			Response<PersistedAuthority> response) {
		Iterable<PersistedAuthority> findAll = persistedAuthorityService.findAll();
		CollectionUtils.<PersistedAuthority> addAll(response.getResults(),findAll
				);
	}

	@Override
	public String getDisplayText(PersistedAuthority choice) {
		return choice.getAuthority();
	}

	@Override
	public Object getId(PersistedAuthority choice) {
		return choice.getAuthority();
	}

	@Override
	public Collection<PersistedAuthority> toChoices(Collection<String> ids) {
		List<PersistedAuthority> returnable = new ArrayList<PersistedAuthority>();
		for (String string : ids)
			returnable.add(persistedAuthorityService.findOne(string).getEntity());
		return returnable;
	}

}
