/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    mihai
 ******************************************************************************/

package org.devgateway.eudevfin.mcm.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.CurrencyMetadataService;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.models.BigMoneyModel;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mihai
 * @since 30.01.2014
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/nonflow")
public class EditNonFlowItemsPage extends HeaderFooter {

	@SpringBean
	private FinancialTransactionService financialTransactionService;

	@SpringBean
	private CurrencyMetadataService currencyMetadataService;

	private static final Logger logger = Logger.getLogger(EditNonFlowItemsPage.class);

	public WebMarkupContainer initializeFakeFinancialContainer(String id,String fieldId,PageParameters parameters) {
		//initialize a wrapping container
		WebMarkupContainer container = new WebMarkupContainer(id);
		
		//initialize a non flow transaction
		final FinancialTransaction financialTransaction = initializeNonFlowTransaction(new FinancialTransaction(),parameters);
		
		//create a compoundmodel and assign it to he container
		container.setDefaultModel(new CompoundPropertyModel<FinancialTransaction>(financialTransaction));
		
		//create a read only model to read the currency
		ComponentPropertyModel<CurrencyUnit> readOnlyCurrencyModel = new ComponentPropertyModel<>("currency");
		
		//create textinputfield to edit the amount
		container.add(new TextInputField<>(fieldId, new BigMoneyModel(new RWComponentPropertyModel<BigMoney>("amountsExtended"),
    			readOnlyCurrencyModel)).typeBigDecimal());
		
        return container;
	}
	
	@SuppressWarnings("unchecked")
	public EditNonFlowItemsPage(final PageParameters parameters) {
		super(parameters);

		Form form = new Form("form");
		add(form);
		
		//dummy integer for the year
		final Integer selectedYear=2000;
	
		//initialize containers
		WebMarkupContainer populationContainer = initializeFakeFinancialContainer("populationContainer","population", parameters);
		WebMarkupContainer gniContainer = initializeFakeFinancialContainer("gniContainer","gni", parameters);
		WebMarkupContainer totalFlowsContainer = initializeFakeFinancialContainer("totalFlowsContainer","totalFlows", parameters);
		WebMarkupContainer odaOfGniContainer = initializeFakeFinancialContainer("odaOfGniContainer","odaOfGni", parameters);

		//initialize textinput for reportingYear
		TextInputField<Integer> reportingYear = new TextInputField<Integer>("reportingYear",new Model<Integer>(selectedYear)) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					super.onUpdate(target);
					//change internal model for containers, fetch from repo and reassign
					
				}
		};
		reportingYear.typeInteger().required().range(2000, 2099).decorateMask("9999");
	
		form.add(reportingYear);

		form.add(populationContainer);
		form.add(gniContainer);
		form.add(totalFlowsContainer);
		form.add(odaOfGniContainer);
		
		form.add(new BootstrapSubmitButton("submit",new StringResourceModel("button.submit", this, null, null)) {
			private static final long serialVersionUID = 1064657874335641769L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				super.onError(target, form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Submitted ok!");
				logger.info("Object:" + getModel().getObject());				
			}
			
		});
	
		
	}
	
	/**
	 * Initialized a previously freshly constructed {@link FinancialTransaction}
	 * @param transaction
	 * @param parameters the {@link PageParameters}
	 * @return 
	 */
	public FinancialTransaction initializeNonFlowTransaction(FinancialTransaction transaction,PageParameters parameters) {
		PersistedUser user=(PersistedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		FinancialTransactionUtil.initializeFinancialTransaction(transaction, this.currencyMetadataService, user.getGroup().getOrganization());	
		return transaction;
	}	
}