/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.core.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.devgateway.eudevfin.ui.common.models.WrappingModel;
import org.joda.money.CurrencyUnit;
import org.joda.money.ExchangeRate;

import java.math.BigDecimal;

/**
 * @author aartimon
 * @since 15/01/14
 */
public class ExchangeRateModel extends WrappingModel<BigDecimal, ExchangeRate> {

    private final IModel<CurrencyUnit> from;
    private final IModel<CurrencyUnit> to;

    public ExchangeRateModel(IWrapModel<ExchangeRate> originalModel, IModel<CurrencyUnit> from, IModel<CurrencyUnit> to) {
        super(originalModel);
        this.from = from;
        this.to = to;
    }

    @Override
    public BigDecimal getObject() {
        return (originalModel.getObject() == null ? null : originalModel.getObject().getRate());
    }

    @Override
    public void setObject(BigDecimal object) {
        //we must ensure that the rate's have been filled prior to this moment, no null checks allowed
        originalModel.setObject(ExchangeRate.of(from.getObject(), to.getObject(), object));

    }
}
