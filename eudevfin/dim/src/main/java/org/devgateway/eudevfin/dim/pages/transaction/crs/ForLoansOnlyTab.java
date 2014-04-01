/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import java.math.BigDecimal;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DateInputField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.events.CurrencyUpdateBehavior;
import org.devgateway.eudevfin.ui.common.events.LoansField12UpdateBehavior;
import org.devgateway.eudevfin.ui.common.events.MarkersField13UpdateBehavior;
import org.devgateway.eudevfin.ui.common.models.BigMoneyModel;
import org.devgateway.eudevfin.ui.common.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;
import org.devgateway.eudevfin.ui.common.temporary.SB;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class ForLoansOnlyTab extends Panel implements PermissionAwareComponent {
    public static final String KEY = "tabs.loans";
	private PageParameters parameters;

    public ForLoansOnlyTab(String id,PageParameters parameters) {
        super(id);
        this.parameters=parameters;
        addComponents();
    }

    private void addComponents() {
        /**
         * BEWARE {@link ComponentPropertyModel} is a read-only model
         */
        ComponentPropertyModel<CurrencyUnit> readOnlyCurrencyModel = new ComponentPropertyModel<>("currency");

        DropDownField<Category> typeOfRepayment = new DropDownField<>("44typeOfRepayment",
                new RWComponentPropertyModel<Category>("typeOfRepayment"), SB.categoryProvider);
        typeOfRepayment.getField().add(new LoansField12UpdateBehavior()); 
        add(typeOfRepayment);

        DropDownField<Category> numberOfRepayments = new DropDownField<>("45numberOfRepayments",
                new RWComponentPropertyModel<Category>("numberOfRepaymentsAnnum"), SB.categoryProvider);
        numberOfRepayments.getField().add(new LoansField12UpdateBehavior());        
        add(numberOfRepayments);

        TextInputField<BigDecimal> interestRate = new TextInputField<>("46interestRate",
                new RWComponentPropertyModel<BigDecimal>("interestRate"));
        interestRate.getField().add(new LoansField12UpdateBehavior());        
        interestRate.typeBigDecimal();
        add(interestRate);

        TextInputField<BigDecimal> secondInterestRate = new TextInputField<>("47secondInterestRate",
                new RWComponentPropertyModel<BigDecimal>("secondInterestRate"));
        secondInterestRate.getField().add(new LoansField12UpdateBehavior());     
        secondInterestRate.typeBigDecimal();
        add(secondInterestRate);

        DateInputField firstRepaymentDate = new DateInputField("48firstRepaymentDate",
                new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("firstRepaymentDate")));
        firstRepaymentDate.getField().add(new LoansField12UpdateBehavior());   
        add(firstRepaymentDate);

        DateInputField finalRepaymentDate = new DateInputField("49finalRepaymentDate",
                new DateToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>("finalRepaymentDate")));
        finalRepaymentDate.getField().add(new LoansField12UpdateBehavior());   
        add(finalRepaymentDate);

        TextInputField<BigDecimal> interestReceived = new TextInputField<>("50interestReceived",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("interestReceived"), readOnlyCurrencyModel));
        interestReceived.getField().add(new LoansField12UpdateBehavior());   
        interestReceived.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(interestReceived);

        TextInputField<BigDecimal> principalDisbursed = new TextInputField<>("51principalDisbursed",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("principalDisbursedOutstanding"), readOnlyCurrencyModel));
        principalDisbursed.typeBigDecimal().add(new CurrencyUpdateBehavior());
        principalDisbursed.getField().add(new LoansField12UpdateBehavior());   
        add(principalDisbursed);

        TextInputField<BigDecimal> arrearsOfPrincipals = new TextInputField<>("52arrearsOfPrincipals",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("arrearsOfPrincipal"), readOnlyCurrencyModel));
        arrearsOfPrincipals.typeBigDecimal().add(new CurrencyUpdateBehavior());
        arrearsOfPrincipals.getField().add(new LoansField12UpdateBehavior());   
        add(arrearsOfPrincipals);

        TextInputField<BigDecimal> arrearsOfInterest = new TextInputField<>("53arrearsOfInterest",
                new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("arrearsOfInterest"), readOnlyCurrencyModel));
        arrearsOfInterest.typeBigDecimal().add(new CurrencyUpdateBehavior());
        arrearsOfInterest.getField().add(new LoansField12UpdateBehavior());   
        add(arrearsOfInterest);
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
