/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.ui.common.providers;

import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.ChannelCategory;

/**
 * @author Alexandru Artimon
 * @since 27/05/14
 */
public class ChannelCategoryProvider extends TagCategoryProvider {
    protected ChannelCategoryProvider(String tag) {
        super(tag);
    }

    @Override
    protected String getDisplayText(Category choice) {
        String extra = "";
        if (((ChannelCategory) choice).getAcronym() != null)
            extra = " - " + ((ChannelCategory) choice).getAcronym();
        return choice.getDisplayableCode() + extra + " - " + choice.getName();
    }
}
