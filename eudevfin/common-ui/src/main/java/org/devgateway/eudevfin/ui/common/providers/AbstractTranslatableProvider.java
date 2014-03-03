/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.providers;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.devgateway.eudevfin.financial.AbstractTranslateable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author aartimon
 * @since 14/01/14
 */
public abstract class AbstractTranslatableProvider<T extends AbstractTranslateable> extends AbstractTextChoiceProvider<T> {

	private static final long serialVersionUID = -5058430903550172780L;

	private static final Logger logger = Logger.getLogger(AbstractTranslatableProvider.class);


	@Override
	public Object getId(T choice) {
		return choice.getId();
	}

	 @Override
	 protected Page<T> getItemsByTerm(String term,int page) {
		 Page<T> helper = getService().findByGeneralSearchPageable(term,
					Session.get().getLocale().getLanguage(), new PageRequest(page, pageSize,sort));
		 return helper;
	 }
}
