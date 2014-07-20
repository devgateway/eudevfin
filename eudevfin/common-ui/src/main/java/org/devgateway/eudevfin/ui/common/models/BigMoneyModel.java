/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.models;

import java.math.BigDecimal;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IWrapModel;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 * @author aartimon
 * @since 03/12/13
 */
public class BigMoneyModel extends WrappingModel<BigDecimal, BigMoney> {

    private final IComponentAssignedModel<CurrencyUnit> readOnlyCurrencyModel;
    private IWrapModel<CurrencyUnit> wrappedReadOnlyModel;

    public BigMoneyModel(IWrapModel<BigMoney> originalModel, IComponentAssignedModel<CurrencyUnit> readOnlyCurrencyModel) {
        super(originalModel);
        this.readOnlyCurrencyModel = readOnlyCurrencyModel;
    }

    @Override
    public BigDecimal getObject() {
        return (originalModel.getObject() == null ? null : originalModel.getObject().getAmount());
    }

    @Override
    public IWrapModel<BigDecimal> wrapOnAssignment(Component component) {
        wrappedReadOnlyModel = readOnlyCurrencyModel.wrapOnAssignment(component);
        return super.wrapOnAssignment(component);
    }

    @Override
    public void detach() {
        super.detach();
        wrappedReadOnlyModel.detach();
        readOnlyCurrencyModel.detach();
    }

    @Override
    public void setObject(BigDecimal object) {
        if (object == null)
            originalModel.setObject(null);
        else
            originalModel.setObject(BigMoney.of(wrappedReadOnlyModel.getObject(), object));
    }
}
