/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.dim.core.RWComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.components.DateInputField;
import org.devgateway.eudevfin.dim.core.components.DropDownField;
import org.devgateway.eudevfin.dim.core.components.TextInputField;
import org.devgateway.eudevfin.dim.core.components.tabs.AbstractTabWithKey;
import org.devgateway.eudevfin.dim.core.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.dim.core.events.CurrencyUpdateBehavior;
import org.devgateway.eudevfin.dim.core.models.BigMoneyModel;
import org.devgateway.eudevfin.dim.core.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.dim.core.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.dim.core.temporary.SB;
import org.devgateway.eudevfin.financial.Category;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;

import java.math.BigDecimal;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class ForLoansOnlyTab extends Panel implements PermissionAwareComponent {

    public static final String KEY = "tabs.loans";

    protected ForLoansOnlyTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        /**
         * BEWARE {@link ComponentPropertyModel} is a read-only model
         */
        ComponentPropertyModel<CurrencyUnit> readOnlyCurrencyModel = new ComponentPropertyModel<>("currency");

        DropDownField<Category> typeOfRepayment = new DropDownField<>("44typeOfRepayment",
                new RWComponentPropertyModel<Category>("typeOfRepayment"), SB.categoryProvider);
        add(typeOfRepayment);

        DropDownField<Category> numberOfRepayments = new DropDownField<>("45numberOfRepayments",
                new RWComponentPropertyModel<Category>("numberOfRepaymentsAnnum"), SB.categoryProvider);
        add(numberOfRepayments);

        TextInputField<BigDecimal> interestRate = new TextInputField<>("46interestRate",
                new RWComponentPropertyModel<BigDecimal>("interestRate"));
        interestRate.typeBigDecimal();
        add(interestRate);

        TextInputField<BigDecimal> secondInterestRate = new TextInputField<>("47secondInterestRate",
                new RWComponentPropertyModel<BigDecimal>("secondInterestRate"));
        secondInterestRate.typeBigDecimal();
        add(secondInterestRate);

        DateInputField firstRepaymentDate = new DateInputField("48firstRepaymentDate",
                new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("firstRepaymentDate")));
        add(firstRepaymentDate);

        DateInputField finalRepaymentDate = new DateInputField("49finalRepaymentDate",
                new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("finalRepaymentDate")));
        add(finalRepaymentDate);

        TextInputField<BigDecimal> interestReceived = new TextInputField<>("50interestReceived",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("interestReceived"), readOnlyCurrencyModel));
        interestReceived.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(interestReceived);

        TextInputField<BigDecimal> principalDisbursed = new TextInputField<>("51principalDisbursed",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("principalDisbursedOutstanding"), readOnlyCurrencyModel));
        principalDisbursed.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(principalDisbursed);

        TextInputField<BigDecimal> arrearsOfPrincipals = new TextInputField<>("52arrearsOfPrincipals",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("arrearsOfPrincipal"), readOnlyCurrencyModel));
        arrearsOfPrincipals.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(arrearsOfPrincipals);

        TextInputField<BigDecimal> arrearsOfInterest = new TextInputField<>("53arrearsOfInterest",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("arrearsOfInterest"), readOnlyCurrencyModel));
        arrearsOfInterest.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(arrearsOfInterest);
/*

        TextInputField<BigDecimal> futureDebtPrincipal = new TextInputField<>("54futureDebtPrincipal", new RWComponentPropertyModel<Integer>("futureDebtPrincipal"));
        futureDebtPrincipal.typeInteger();
        add(futureDebtPrincipal);

        TextInputField<BigDecimal> futureDebtInterest = new TextInputField<>("54futureDebtInterest", new RWComponentPropertyModel<Integer>("futureDebtInterest"));
            futureDebtInterest.typeInteger();
        add(futureDebtInterest);
*/

    }

    public static ITabWithKey newTab(Component askingComponent) {
        return new AbstractTabWithKey(new StringResourceModel(KEY, askingComponent, null), KEY) {
            private static final long serialVersionUID = -724508987522388955L;

            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new ForLoansOnlyTab(panelId);
            }
        };
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
        //do nothing
    }
}
