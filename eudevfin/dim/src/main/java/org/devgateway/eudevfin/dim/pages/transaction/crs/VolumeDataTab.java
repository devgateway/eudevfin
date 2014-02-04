/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.dim.core.models.BigMoneyModel;
import org.devgateway.eudevfin.dim.providers.CurrencyUnitProvider;
import org.devgateway.eudevfin.dim.providers.CurrencyUnitProviderFactory;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.events.CurrencyChangedEvent;
import org.devgateway.eudevfin.ui.common.events.CurrencyUpdateBehavior;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwareComponent;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 * @author aartimon@developmentgateway.org
 * @since 01 November 2013
 */
public class VolumeDataTab extends Panel implements PermissionAwareComponent {
    private static final Logger logger = Logger.getLogger(VolumeDataTab.class);
    public static final String KEY = "tabs.volume";
	private PageParameters parameters;
	
	private DropDownField<CurrencyUnit> currency;
	
	@SpringBean
	private CurrencyUnitProviderFactory currencyUnitProviderFactory;

    public VolumeDataTab(String id,PageParameters parameters) {
        super(id);
        this.parameters=parameters;
        addComponents();
        addExtensionPanel1();
        addExtensionPanel2();
        addExtensionPanel3();
    }

    protected void addExtensionPanel1() {
        add(new WebMarkupContainer("extensionPanel1").setVisibilityAllowed(false));
    }

    protected void addExtensionPanel2() {
        add(new WebMarkupContainer("extensionPanel2").setVisibilityAllowed(false));
    }

    protected void addExtensionPanel3() {
        add(new WebMarkupContainer("extensionPanel3").setVisibilityAllowed(false));
    }

    private void addComponents() {

        ComponentPropertyModel<CurrencyUnit> readOnlyCurrencyModel = new ComponentPropertyModel<>("currency");
        CurrencyUnitProvider nationalCurrenciesProvider	= 
        		this.currencyUnitProviderFactory.getCurrencyUnitProviderInstance(CurrencyUnitProviderFactory.NATIONAL_UNSORTED_CURRENCIES_PROVIDER);
        currency = new DropDownField<CurrencyUnit>("32currency", new RWComponentPropertyModel<CurrencyUnit>("currency"),
                nationalCurrenciesProvider) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                send(getPage(), Broadcast.DEPTH, new CurrencyChangedEvent(target));
            }
        };
        currency.required();
        add(currency);
        TextInputField<BigDecimal> commitments = new TextInputField<>("33commitments", new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("commitments"), readOnlyCurrencyModel));
        commitments.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(commitments);

        TextInputField<BigDecimal> amountsExtended = new TextInputField<>("34amountsExtended", new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("amountsExtended"), readOnlyCurrencyModel));
        amountsExtended.typeBigDecimal().required().add(new CurrencyUpdateBehavior());
        add(amountsExtended);

        TextInputField<BigDecimal> amountsReceived = new TextInputField<>("35amountsReceived", new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("amountsReceived"), readOnlyCurrencyModel));
        amountsReceived.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(amountsReceived);

        TextInputField<BigDecimal> amountUntied = new TextInputField<>("36amountUntied", new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("amountsUntied"), readOnlyCurrencyModel));
        amountUntied.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(amountUntied);

        TextInputField<BigDecimal> amountPartiallyUntied = new TextInputField<>("37amountPartiallyUntied", new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("amountsPartiallyUntied"), readOnlyCurrencyModel));
        amountPartiallyUntied.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(amountPartiallyUntied);

        TextInputField<BigDecimal> amountTied = new TextInputField<>("38amountTied", new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("amountsTied"), readOnlyCurrencyModel));
        amountTied.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(amountTied);

        TextInputField<BigDecimal> amountOfIRTC = new TextInputField<>("39amountOfIRTC", new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("amountOfIRTC"), readOnlyCurrencyModel));
        amountOfIRTC.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(amountOfIRTC);

        TextInputField<BigDecimal> amountOfExpertsCommitments = new TextInputField<>("40amountOfExpertsCommitments", new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("projectAmountExpertCommitments"), readOnlyCurrencyModel));
        amountOfExpertsCommitments.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(amountOfExpertsCommitments);

        TextInputField<BigDecimal> amountOfExpertsExtended = new TextInputField<>("41amountOfExpertsExtended", new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("projectAmountExpertExtended"), readOnlyCurrencyModel));
        amountOfExpertsExtended.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(amountOfExpertsExtended);

        TextInputField<BigDecimal> amountOfExportCredit = new TextInputField<>("42amountOfExportCredit", new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("amountOfExportCreditInAFPackage"), readOnlyCurrencyModel));
        amountOfExportCredit.typeBigDecimal().add(new CurrencyUpdateBehavior());
        add(amountOfExportCredit);
    }

    @Override
    public String getPermissionKey() {
        return KEY;
    }

    @Override
    public void enableRequired() {
    }

	public DropDownField<CurrencyUnit> getCurrency() {
		return currency;
	}

	public void setCurrency(DropDownField<CurrencyUnit> currency) {
		this.currency = currency;
	}
    
}
