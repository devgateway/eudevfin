/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.devgateway.eudevfin.dim.core.RWComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.components.DateInputField;
import org.devgateway.eudevfin.dim.core.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.dim.pages.transaction.crs.IdentificationDataTab;
import org.joda.time.LocalDateTime;

/**
 * @author aartimon
 * @since 12/12/13
 */
public class CustomIdentificationDataTab extends IdentificationDataTab {
    public CustomIdentificationDataTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        DateInputField firstRepaymentDate = new DateInputField("1aDataAsPerDate",
                new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("dataAsPerDate")));
        add(firstRepaymentDate);

    }
}
