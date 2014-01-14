/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import org.apache.wicket.model.IDetachable;
import org.devgateway.eudevfin.financial.service.CategoryService;
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
    private CategoryService categoryService;

    private Hashtable<String, AbstractCategoryProvider> map = new Hashtable<>();

    public AbstractCategoryProvider get(String tag) {
        AbstractCategoryProvider provider = map.get(tag);
        if (provider != null)
            return provider;
        provider = new TagCategoryProvider(categoryService, tag);
        map.put(tag, provider);
        return provider;
    }

    @Override
    public void detach() {
    }
}
