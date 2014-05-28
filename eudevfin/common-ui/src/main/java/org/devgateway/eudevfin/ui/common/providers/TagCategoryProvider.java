/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.providers;


import org.apache.wicket.Session;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author aartimon
 * @since 14/01/14
 */
public class TagCategoryProvider extends AbstractCategoryProvider {

	private static final long serialVersionUID = 5293225229898330470L;
	private final String tag;

    protected TagCategoryProvider(String tag) {
        super();
        this.tag = tag;
    }

    @Override
    protected String getDisplayText(Category choice) {
        if (choice instanceof ChannelCategory) {
            String extra = "";
            if (((ChannelCategory) choice).getAcronym() != null)
                extra = " - " + ((ChannelCategory) choice).getAcronym();
            return choice.getDisplayableCode() + extra + " - " + choice.getName();
        }
        return super.getDisplayText(choice);
    }

    @Override
    protected Page<Category> getItemsByTerm(String term, int page) {
        Page<Category> categories = categoryService.findByGeneralSearchAndTagsCodePaginated(Session.get().getLocale()
                .getLanguage(), term, getCategoryTag(), new PageRequest(page, pageSize, sort), false);
        return categories;
    }

    protected String getCategoryTag() {
        return tag;
    }
}
