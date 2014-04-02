/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.providers;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;
import org.devgateway.eudevfin.metadata.common.service.ChannelCategoryService;
import org.devgateway.eudevfin.ui.common.providers.AbstractTranslatableProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author aartimon
 * @since 14/01/14
 * @deprecated use {@link org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory} instead
 */
@Deprecated
@Component
public class ChannelCategoryChoiceProvider extends AbstractTranslatableProvider<ChannelCategory> {

    public ChannelCategoryChoiceProvider() {
        //this.sort=new Sort(Direction.ASC,"code");
    }

    @Autowired
    private ChannelCategoryService channelCategoryService;

    @Override
    protected BaseEntityService<ChannelCategory> getService() {
        return channelCategoryService;
    }

    @Override
    public String getDisplayText(ChannelCategory choice) {
        return choice.getDisplayableCode() + " - " + choice.getName();
    }

    @Override
    public void detach() {
        //Spring component no need to detach if added into wicket components with @SpringBean
    }
}
