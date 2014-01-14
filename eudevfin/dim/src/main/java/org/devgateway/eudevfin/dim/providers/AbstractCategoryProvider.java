/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.service.CategoryService;

import java.util.List;

/**
 * @author aartimon
 * @since 14/01/14
 */
public abstract class AbstractCategoryProvider extends AbstractTranslatableProvider<Category> {
    private CategoryService categoryService;

    protected AbstractCategoryProvider(CategoryService categoryService) {
        this.categoryService = categoryService;
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
    protected List<Category> getItemsByTerm(String term) {
        return categoryService.findByTagsCode(getCategoryTag());
    }

    protected abstract String getCategoryTag();

}
