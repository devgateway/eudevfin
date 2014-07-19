/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.dim.pages.ReuseTransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPage;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;

/**
 * @author mihai
 * 
 */
public class GroupingTransactionTableListPanel<T extends FinancialTransaction> extends TransactionTableListPanel<T> {

	private static final long serialVersionUID = 3450888580447024637L;

	/**
	 * @param id
	 * @param listGenerator
	 */
	public GroupingTransactionTableListPanel(String id, ListGeneratorInterface<T> listGenerator) {
		super(id, listGenerator);
	}

	
	@Override
	protected Link getTransactionEditLink(final FinancialTransaction tempTx) {
		return new Link<FinancialTransaction>("transaction-edit-link") {
			private static final long serialVersionUID = 9084184844700618410L;

			@Override
			public void onClick() {
				PageParameters pageParameters = new PageParameters();
				pageParameters.add(TransactionPage.PARAM_TRANSACTION_ID, tempTx.getId());
				pageParameters.add(Constants.PARAM_TRANSACTION_TYPE,
						((CustomFinancialTransaction) tempTx).getFormType());
				setResponsePage(ReuseTransactionPage.class, pageParameters);
			}
		};
	}
}
