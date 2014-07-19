/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.models;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IWrapModel;
import org.joda.money.CurrencyUnit;
import org.joda.money.ExchangeRate;

import java.math.BigDecimal;

/**
 * @author aartimon
 * @since 15/01/14
 * @deprecated we're not using the {@link org.joda.money.ExchangeRate} since we can have an other currency without
 * the rate specified
 */
public class ExchangeRateModel extends WrappingModel<BigDecimal, ExchangeRate> {

    private final IComponentAssignedModel<CurrencyUnit> from;
    private final IComponentAssignedModel<CurrencyUnit> to;
    private IWrapModel<CurrencyUnit> wrappedFrom;
    private IWrapModel<CurrencyUnit> wrappedTo;


    public ExchangeRateModel(IWrapModel<ExchangeRate> originalModel, IComponentAssignedModel<CurrencyUnit> from, IComponentAssignedModel<CurrencyUnit> to) {
        super(originalModel);
        this.from = from;
        this.to = to;
    }

    @Override
    public IWrapModel<BigDecimal> wrapOnAssignment(Component component) {
        wrappedFrom = from.wrapOnAssignment(component);
        wrappedTo = to.wrapOnAssignment(component);
        return super.wrapOnAssignment(component);
    }

    @Override
    public void detach() {
        super.detach();
        wrappedTo.detach();
        wrappedFrom.detach();
        to.detach();
        from.detach();
    }

    @Override
    public BigDecimal getObject() {
        return (originalModel.getObject() == null ? null : originalModel.getObject().getRate());
    }

    @Override
    public void setObject(BigDecimal object) {
        //we must ensure that the rate's have been filled prior to this moment, no null checks allowed
        originalModel.setObject(ExchangeRate.of(wrappedFrom.getObject(), wrappedTo.getObject(), object));
    }
}
