/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import org.apache.wicket.model.IDetachable;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.service.CategoryService;
import org.devgateway.eudevfin.ui.common.providers.SpringCategoryProviderProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

/**
 * @author aartimon
 * @since 14/01/14
 */
@Component
public class CategoryProviderFactory implements IDetachable {

    @Autowired
    protected CategoryService categoryService;

    private static final long serialVersionUID = 5075621973446951621L;

    private transient Hashtable<String, SpringCategoryProviderProxy<Category>> map = new Hashtable<>();

    public SpringCategoryProviderProxy<Category> get(String tag) {
        SpringCategoryProviderProxy<Category> provider = map.get(tag);
        if (provider != null)
            return provider;
        provider = new SpringCategoryProviderProxy<>(new TagCategoryProvider(tag));
        map.put(tag, provider);
        return provider;
    }


    @Override
    public void detach() {
    }

}
