/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import java.util.List;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * @author aartimon
 * @since 14/01/14
 */
public abstract class AbstractCategoryProvider extends AbstractTranslatableProvider<Category> {
    private CategoryService categoryService;

    protected AbstractCategoryProvider(CategoryService categoryService) {
        this.categoryService = categoryService;    
        this.sort=new Sort(Direction.ASC,"code");
    }

    @Override
    protected BaseEntityService<Category> getService() {
        return categoryService;
    }

    @Override
    protected String getDisplayText(Category choice) {
        return choice.getName();
    }

    @Override
    protected Page<Category> getItemsByTerm(String term,int page) {
    	//TODO: change this if you want pagination !
        List<Category> tagsCode = categoryService.findByTagsCode(getCategoryTag());
        PageImpl<Category> pageImpl = new PageImpl<Category>(tagsCode);
        return pageImpl;
    }

    protected abstract String getCategoryTag();
    
   

}
