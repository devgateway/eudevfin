/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.mcm.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.exchange.common.service.HistoricalExchangeRateService;
import org.devgateway.eudevfin.mcm.components.HistoricalExchangeRateTableListPanel;
import org.devgateway.eudevfin.mcm.util.HistoricalExchangeRateListGenerator;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.pages.ListPage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mihai
 * @since 12.02.14
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/rates")
public class ListHistoricalExchangeRatePage extends ListPage {

	private static final long serialVersionUID = -8637428983397263676L;

	@SpringBean
	private HistoricalExchangeRateService historicalExchangeRateService;

	private static final Logger logger = Logger.getLogger(ListHistoricalExchangeRatePage.class);

	@SuppressWarnings("unchecked")
	public ListHistoricalExchangeRatePage(final PageParameters parameters) {

		HistoricalExchangeRateTableListPanel historicalExchangeRateTableListPanel = new HistoricalExchangeRateTableListPanel(
				WICKETID_LIST_PANEL, new HistoricalExchangeRateListGenerator(historicalExchangeRateService, ""));
		add(historicalExchangeRateTableListPanel);
		
		Form form = new Form("form");
		BootstrapSubmitButton submitButton = new BootstrapSubmitButton("addNewRate", new StringResourceModel("button.addNewRate", this, null, null)) {			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
		
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				setResponsePage(EditHistoricalExchangeRatePage.class);
			}
			
		};
		submitButton.setDefaultFormProcessing(false);
		form.add(submitButton);
		add(form);
	}

}
