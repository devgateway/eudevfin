/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.providers;

import org.apache.wicket.Session;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.service.ChannelCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandru Artimon
 * @since 27/05/14
 */
public class ChannelCategoryProvider extends TagCategoryProvider {
    protected ChannelCategoryProvider(String tag) {
        super(tag);
    }

    @Autowired
    protected transient ChannelCategoryService channelCategoryService;

    @Override
    protected String getDisplayText(Category choice) {
        String extra = "";
        if (((ChannelCategory) choice).getAcronym() != null)
            extra = " - " + ((ChannelCategory) choice).getAcronym();
        return choice.getDisplayableCode() + extra + " - " + choice.getName();
    }

    @Override
    protected Page<Category> getItemsByTerm(String term, int page) {
        Page<ChannelCategory> categories = channelCategoryService.findByGeneralSearchAndTagsCodePaginated(Session.get().getLocale()
                .getLanguage(), term, getCategoryTag(), new PageRequest(page, pageSize, sort), false);

        List<Category> elements = new ArrayList<Category>(categories.getContent());
        Page<Category> ret = new PageImpl<>(elements, categories.nextPageable(), categories.getTotalElements());
        return ret;
    }
}
