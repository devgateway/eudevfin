/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import java.math.BigDecimal;

import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.dim.pages.transaction.crs.ForLoansOnlyTab;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.events.CurrencyUpdateBehavior;
import org.devgateway.eudevfin.ui.common.events.LoansField12UpdateBehavior;
import org.devgateway.eudevfin.ui.common.models.BigMoneyModel;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 * For Loans Only Tab extension for the EU-DEVFIN Form
 *
 * @author aartimon
 * @since 11/12/13
 */
public class CustomForLoansOnlyTab extends ForLoansOnlyTab {
    public CustomForLoansOnlyTab(String id,PageParameters parameters) {
        super(id, parameters);
        addComponents();
    }

    private void addComponents() {
        /**
         * BEWARE {@link org.apache.wicket.model.ComponentPropertyModel} is a read-only model
         */
        ComponentPropertyModel<CurrencyUnit> readOnlyCurrencyModel = new ComponentPropertyModel<>("currency");


        TextInputField<BigDecimal> futureDebtPrincipal = new TextInputField<>("54futureDebtPrincipal",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("futureDebtPrincipal"), readOnlyCurrencyModel));
        futureDebtPrincipal.typeBigDecimal().add(new CurrencyUpdateBehavior());
        futureDebtPrincipal.getField().add(new LoansField12UpdateBehavior());  
        add(futureDebtPrincipal);

        TextInputField<BigDecimal> futureDebtInterest = new TextInputField<>("54futureDebtInterest",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("futureDebtInterest"), readOnlyCurrencyModel));
        futureDebtInterest.typeBigDecimal().add(new CurrencyUpdateBehavior());
        futureDebtInterest.getField().add(new LoansField12UpdateBehavior());  
        add(futureDebtInterest);
    }
}
