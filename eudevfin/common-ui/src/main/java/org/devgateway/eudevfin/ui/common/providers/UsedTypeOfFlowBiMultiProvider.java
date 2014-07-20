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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author idobre
 * @since 4/7/14
 */
public class UsedTypeOfFlowBiMultiProvider extends AbstractCategoryProvider {

    protected UsedTypeOfFlowBiMultiProvider() {

    }

    @Override
    protected Page<Category> getItemsByTerm(String term, int page) {
        Page<Category> categories = categoryService.findUsedTypeOfFlowBiMultiPaginated(Session.get().getLocale()
                .getLanguage(), term, new PageRequest(page, pageSize, sort), false);
        return categories;
    }
}