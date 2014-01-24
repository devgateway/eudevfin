/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import org.apache.wicket.Session;
import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * @author aartimon
 * @since 14/01/14
 */
public abstract class AbstractCategoryProvider extends AbstractTranslatableProvider<Category> {
	
	@Autowired
	protected transient CategoryService categoryService;

    protected AbstractCategoryProvider() {
      //  this.sort=new Sort(Direction.ASC,"code");
    }

    @Override
    protected BaseEntityService<Category> getService() {
        return categoryService;
    }

    @Override
    protected String getDisplayText(Category choice) {
        return choice.getDisplayableCode()+ " - "+ choice.getName();
    }

	@Override
	protected Page<Category> getItemsByTerm(String term, int page) {
		Page<Category> categories = categoryService.findByGeneralSearchAndTagsCodePaginated(Session.get().getLocale()
				.getLanguage(), term, getCategoryTag(), new PageRequest(page, pageSize, sort));
		return categories;
	}

    protected abstract String getCategoryTag();
    
   

}
