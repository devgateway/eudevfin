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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
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
import org.devgateway.eudevfin.ui.common.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.joda.time.LocalDateTime;
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

	@SuppressWarnings("unchecked")
	public EditNonFlowItemsPage(final PageParameters parameters) {
		super(parameters);

		Form form = new Form("form");
		add(form);
		
		WebMarkupContainer reportingYearContainer = new WebMarkupContainer("reportingYearContainer");
		form.add(reportingYearContainer);
		
		final FinancialTransaction financialTransaction = initializeNonFlowTransaction(new FinancialTransaction(),parameters);
		
		CompoundPropertyModel<FinancialTransaction> model = new CompoundPropertyModel<FinancialTransaction>(
				financialTransaction);
		
		reportingYearContainer.setDefaultModel(model);
	
		TextInputField<Integer> reportingYear = new TextInputField<>("reportingYear", new YearToLocalDateTimeModel(
				 new RWComponentPropertyModel<LocalDateTime>("reportingYear")));
		reportingYear.typeInteger().required().range(1900, 2099).decorateMask("9999");
		reportingYearContainer.add(reportingYear);
		
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
				financialTransaction.toString();
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