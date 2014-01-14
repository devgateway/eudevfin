/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import org.devgateway.eudevfin.financial.service.CategoryService;

/**
 * @author aartimon
 * @since 14/01/14
 */
public class TagCategoryProvider extends AbstractCategoryProvider {

    private final String tag;

    protected TagCategoryProvider(CategoryService categoryService, String tag) {
        super(categoryService);
        this.tag = tag;
    }

    @Override
    protected String getCategoryTag() {
        return tag;
    }
}
