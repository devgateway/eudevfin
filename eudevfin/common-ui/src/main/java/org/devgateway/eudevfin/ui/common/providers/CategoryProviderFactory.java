/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.providers;

import org.apache.wicket.model.IDetachable;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.service.CategoryService;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

/**
 * @author aartimon
 * @since 14/01/14
 */
@Component
public class CategoryProviderFactory implements IDetachable {
    private final String USED_GEOGRAPHY_PROVIDER = "USEDGEOGRAPHYPROVIDER";
    private final String USED_TYPE_OF_FLOW_BIMULTI_PROVIDER = "USEDTYPEOFFLOWBIMULTIPROVIDER";
    private final String USED_TYPE_OF_AID_PROVIDER = "USEDTYPEOFAIDPROVIDER";
    private final String USED_SECTOR_PROVIDER = "USEDSECTORPROVIDER";
    private final String USED_CHANNEL_PROVIDER = "USEDCHANNELPROVIDER";

    @Autowired
    protected CategoryService categoryService;

    private static final long serialVersionUID = 5075621973446951621L;

    private transient Hashtable<String, SpringCategoryProviderProxy<Category>> map = new Hashtable<>();

    public SpringCategoryProviderProxy<Category> get(String tag) {
        SpringCategoryProviderProxy<Category> provider = map.get(tag);
        if (provider != null) {
            return provider;
        }

        if (tag.equals(CategoryConstants.CHANNEL_TAG))
            provider = new SpringCategoryProviderProxy<>(new ChannelCategoryProvider(tag));
        else
            provider = new SpringCategoryProviderProxy<>(new TagCategoryProvider(tag));
        map.put(tag, provider);
        return provider;
    }

    public SpringCategoryProviderProxy<Category> getUsedGeographyProvider () {
        SpringCategoryProviderProxy<Category> provider = map.get(USED_GEOGRAPHY_PROVIDER);
        if (provider != null) {
            return provider;
        }

        provider = new SpringCategoryProviderProxy<>(new UsedGeographyProvider());
        map.put(USED_GEOGRAPHY_PROVIDER, provider);

        return provider;
    }

    public SpringCategoryProviderProxy<Category> getUsedTypeOfFlowBiMultiProvider () {
        SpringCategoryProviderProxy<Category> provider = map.get(USED_TYPE_OF_FLOW_BIMULTI_PROVIDER);
        if (provider != null) {
            return provider;
        }

        provider = new SpringCategoryProviderProxy<>(new UsedTypeOfFlowBiMultiProvider());
        map.put(USED_TYPE_OF_FLOW_BIMULTI_PROVIDER, provider);

        return provider;
    }

    public SpringCategoryProviderProxy<Category> getUsedTypeOfAidProvider () {
        SpringCategoryProviderProxy<Category> provider = map.get(USED_TYPE_OF_AID_PROVIDER);
        if (provider != null) {
            return provider;
        }

        provider = new SpringCategoryProviderProxy<>(new UsedTypeOfAidProvider());
        map.put(USED_TYPE_OF_AID_PROVIDER, provider);

        return provider;
    }

    public SpringCategoryProviderProxy<Category> getUsedSectorProvider () {
        SpringCategoryProviderProxy<Category> provider = map.get(USED_SECTOR_PROVIDER);
        if (provider != null) {
            return provider;
        }

        provider = new SpringCategoryProviderProxy<>(new UsedSectorProvider());
        map.put(USED_SECTOR_PROVIDER, provider);

        return provider;
    }

    public SpringCategoryProviderProxy<Category> getUsedChannelProvider () {
        SpringCategoryProviderProxy<Category> provider = map.get(USED_CHANNEL_PROVIDER);
        if (provider != null) {
            return provider;
        }

        provider = new SpringCategoryProviderProxy<>(new UsedChannelProvider());
        map.put(USED_CHANNEL_PROVIDER, provider);

        return provider;
    }

    @Override
    public void detach() {
    }
}
