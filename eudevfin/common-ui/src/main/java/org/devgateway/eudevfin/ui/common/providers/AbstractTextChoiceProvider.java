/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.providers;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.common.spring.SpringPropertyExpressions;
import org.json.JSONException;
import org.json.JSONWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;

/**
 * @author aartimon
 * @since 14/01/14
 */
public abstract class AbstractTextChoiceProvider<T> extends ChoiceProvider<T> {

	private static final long serialVersionUID = -5058430903550172780L;

	private static final Logger logger = Logger.getLogger(AbstractTextChoiceProvider.class);
	
	protected Sort sort;

	@Value(SpringPropertyExpressions.SELECT2_PAGE_SIZE)
	protected Integer pageSize;

	protected abstract BaseEntityService<T> getService();


	@Override
	public void query(String term, int page, Response<T> response) {		
		Page<T> itemsByTerm = getItemsByTerm(term,page);	
		response.setHasMore(itemsByTerm.hasNextPage());
		response.addAll(itemsByTerm.getContent());
	}
	
	 protected Page<T> getItemsByTerm(String term,int page) {
		 return getService().findByGeneralSearchPageable(term,null, new PageRequest(page, pageSize,sort));		 
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
	
	protected abstract String getDisplayText(T choice);

    protected abstract Object getId(T choice);

    @Override
    public void toJson(T choice, JSONWriter writer) throws JSONException {
    	writer.key("id").value(getId(choice)).key("text").value(getDisplayText(choice));
    };

}
