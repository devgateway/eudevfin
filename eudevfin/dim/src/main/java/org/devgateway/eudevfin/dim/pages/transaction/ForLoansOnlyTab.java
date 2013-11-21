/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 ******************************************************************************/

package org.devgateway.eudevfin.dim.pages.transaction;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.dim.core.RWComponentPropertyModel;
import org.devgateway.eudevfin.dim.core.StaticBinds;
import org.devgateway.eudevfin.dim.core.components.DateInputField;
import org.devgateway.eudevfin.dim.core.components.DropDownField;
import org.devgateway.eudevfin.dim.core.components.TextInputField;

import java.util.Date;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class ForLoansOnlyTab extends Panel {

    public ForLoansOnlyTab(String id) {
        super(id);
        addComponents();
    }

    private void addComponents() {
        DateInputField commitmentDate = new DateInputField("43commitmentDate", new RWComponentPropertyModel<Date>("commitmentDate"), "43commitmentDate");
        add(commitmentDate);

        DropDownField<String> typeOfRepayment = new DropDownField<>("44typeOfRepayment", new RWComponentPropertyModel<String>("typeOfRepayment"), "44typeOfRepayment",
                StaticBinds.countryProvider);
        add(typeOfRepayment);

        DropDownField<String> numberOfRepayments = new DropDownField<>("45numberOfRepayments", new RWComponentPropertyModel<String>("numberOfRepayments"), "45numberOfRepayments",
                StaticBinds.countryProvider);
        add(numberOfRepayments);

        TextInputField interestRate = new TextInputField<>("46interestRate", new RWComponentPropertyModel<Integer>("interestRate"), "46interestRate");
        interestRate.typeInteger().decorateMask("99999");
        add(interestRate);

        TextInputField secondInterestRate = new TextInputField<>("47secondInterestRate", new RWComponentPropertyModel<Integer>("secondInterestRate"), "47secondInterestRate");
        secondInterestRate.typeInteger().decorateMask("99999");
        add(secondInterestRate);

        DateInputField firstRepaymentDate = new DateInputField("48firstRepaymentDate", new RWComponentPropertyModel<Date>("firstRepaymentDate"), "48firstRepaymentDate");
        add(firstRepaymentDate);

        DateInputField finalRepaymentDate = new DateInputField("49finalRepaymentDate", new RWComponentPropertyModel<Date>("finalRepaymentDate"), "49finalRepaymentDate");
        add(finalRepaymentDate);

        TextInputField interestReceived = new TextInputField<>("50interestReceived", new RWComponentPropertyModel<Integer>("interestReceived"), "50interestReceived");
        interestReceived.typeInteger();
        add(interestReceived);

        TextInputField principalDisbursed = new TextInputField<>("51principalDisbursed", new RWComponentPropertyModel<Integer>("principalDisbursed"), "51principalDisbursed");
        principalDisbursed.typeInteger();
        add(principalDisbursed);

        TextInputField arrearsOfPrincipals = new TextInputField<>("52arrearsOfPrincipals", new RWComponentPropertyModel<Integer>("arrearsOfPrincipals"), "52arrearsOfPrincipals");
        arrearsOfPrincipals.typeInteger();
        add(arrearsOfPrincipals);

        TextInputField arrearsOfInterest = new TextInputField<>("53arrearsOfInterest", new RWComponentPropertyModel<Integer>("arrearsOfInterest"), "53arrearsOfInterest");
        arrearsOfInterest.typeInteger();
        add(arrearsOfInterest);

        TextInputField futureDebtPrincipal = new TextInputField<>("54futureDebtPrincipal", new RWComponentPropertyModel<Integer>("futureDebtPrincipal"), "54futureDebtPrincipal");
        futureDebtPrincipal.typeInteger();
        add(futureDebtPrincipal);

        TextInputField futureDebtInterest = new TextInputField<>("54futureDebtInterest", new RWComponentPropertyModel<Integer>("futureDebtInterest"), "54futureDebtInterest");
        futureDebtInterest.typeInteger();
        add(futureDebtInterest);

    }

    public static ITab newTab(Component askingComponent){
        return new AbstractTab(new StringResourceModel("tabs.loans", askingComponent, null)){
            private static final long serialVersionUID = -724508987522388955L;

            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new ForLoansOnlyTab(panelId);
            }
        };
    }
}
