/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import org.apache.wicket.model.ComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.RWComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.components.TextInputField;
import org.devgateway.eudevfin.dim.core.models.BigMoneyModel;
import org.devgateway.eudevfin.dim.pages.transaction.crs.ForLoansOnlyTab;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

import java.math.BigDecimal;

/**
 * For Loans Only Tab extension for the EU-DEVFIN Form
 *
 * @author aartimon
 * @since 11/12/13
 */
class CustomForLoansOnlyTab extends ForLoansOnlyTab {
    public CustomForLoansOnlyTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        /**
         * BEWARE {@link org.apache.wicket.model.ComponentPropertyModel} is a read-only model
         */
        ComponentPropertyModel<CurrencyUnit> readOnlyCurrencyModel = new ComponentPropertyModel<>("currency");


        TextInputField<BigDecimal> futureDebtPrincipal = new TextInputField<>("54futureDebtPrincipal",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("futureDebtPrincipal"), readOnlyCurrencyModel));
        futureDebtPrincipal.typeInteger();
        add(futureDebtPrincipal);

        TextInputField<BigDecimal> futureDebtInterest = new TextInputField<>("54futureDebtInterest",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("futureDebtInterest"), readOnlyCurrencyModel));
        futureDebtInterest.typeInteger();
        add(futureDebtInterest);
    }
}
