/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.models;

import org.apache.wicket.model.IModel;
import org.joda.time.LocalDateTime;

import java.util.Date;

/**
 * Wrapper model of {@link LocalDateTime} that gets the input from a {@link Date} field
 *
 * @author aartimon
 * @since 29/11/13
 */
public class DateToLocalDateTimeModel extends WrapperModel<Date, LocalDateTime> {

    public DateToLocalDateTimeModel(IModel<LocalDateTime> originalModel) {
        super(originalModel);
    }

    @Override
    public Date getObject() {
        LocalDateTime date = originalModel.getObject();
        return (date == null ? null : date.toDate());
    }

    @Override
    public void setObject(Date object) {
        originalModel.setObject(LocalDateTime.fromDateFields(object));
    }
}
