/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import java.util.Hashtable;

import org.apache.wicket.model.IDetachable;
import org.devgateway.eudevfin.common.spring.ContextHelper;
import org.devgateway.eudevfin.financial.service.CategoryService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author aartimon
 * @since 14/01/14
 */
@Component
public class CategoryProviderFactory implements IDetachable {

	@Autowired
	protected CategoryService categoryService;
	
	private static final long serialVersionUID = 5075621973446951621L;

    private transient Hashtable<String, AbstractCategoryProvider> map = new Hashtable<>();

    public AbstractCategoryProvider get(String tag) {
        AbstractCategoryProvider provider = map.get(tag);
        if (provider != null)
            return provider;
    	AutowireCapableBeanFactory factory = ContextHelper.newInstance()
    			.getAutowireCapableBeanFactory();
		provider = new TagCategoryProvider(tag);
		factory.autowireBean(provider);
        map.put(tag, provider);
        return provider;
    }
    

	@Override
	public void detach() {
		
	}


}
