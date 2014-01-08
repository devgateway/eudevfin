/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.models;

import org.apache.wicket.model.IWrapModel;
import org.devgateway.eudevfin.ui.common.models.WrappingModel;
import org.joda.time.LocalDateTime;

/**
 * Wrapper model of {@link LocalDateTime} that only sets the year leaving the rest of the date
 * set to 1st January at 00:00
 *
 * @author aartimon
 * @since 29/11/13
 */
public class YearToLocalDateTimeModel extends WrappingModel<Integer, LocalDateTime> {
    public YearToLocalDateTimeModel(IWrapModel<LocalDateTime> originalModel) {
        super(originalModel);
    }

    @Override
    public Integer getObject() {
        LocalDateTime date = originalModel.getObject();
        return (date == null ? null : date.getYear());
    }

    @Override
    public void setObject(Integer object) {
        originalModel.setObject(new LocalDateTime(object, 1, 1, 0, 0)); //1st January 00:00 at the current year
    }
}
