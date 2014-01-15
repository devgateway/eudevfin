/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;

/**
 * @author aartimon
 * @since 14/01/14
 */
public abstract class AbstractTextChoiceProvider<T> extends TextChoiceProvider<T> {

	private static final long serialVersionUID = -5058430903550172780L;

	private static final Logger logger = Logger.getLogger(AbstractTextChoiceProvider.class);
	
	protected Sort sort;

	@Value("#{commonProperties['sel.defaultSelectorPageSize']}")
	protected Integer pageSize;

	protected abstract BaseEntityService<T> getService();


	@Override
	public void query(String term, int page, Response<T> response) {		
		List<T> list = getItemsByTerm(term,page); 		
		//response.setHasMore(pagingHelper.hasMorePages());
		response.addAll(list);
	}
	
	 protected List<T> getItemsByTerm(String term,int page) {
		 return getService().findByGeneralSearchPageable(term,null, page+1, pageSize,sort).getEntities();		 
	 }

	@Override
	public Collection<T> toChoices(Collection<String> ids) {
		ArrayList<T> ret = new ArrayList<>();
		for (String strId : ids) {
			Long id = Long.parseLong(strId);
			T item = getService().findOne(id).getEntity();
			if (item == null)
				logger.error("Can't find object with id: " + id);
			else
				ret.add(item);
		}
		return ret;
	}

	/**
	 * @return the sort
	 */
	public Sort getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public AbstractTextChoiceProvider<T> sort(Sort sort) {
		this.sort = sort;
		return this;
	}

}
