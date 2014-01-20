/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.dim.desktop.components.util.ComponentsUtil;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.tabs.AbstractTabWithKey;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;

/**
 * @author mihai
 *
 */
public class TransactionTableListPanel<T extends FinancialTransaction> extends TableListPanel<T> {

	private static final long serialVersionUID = -3321918474505239409L;
	
	private static final Logger logger	= Logger.getLogger(TransactionTableListPanel.class);

	/**
	 * @param id
	 * @param listGenerator
	 */
	public TransactionTableListPanel(String id,
			ListGeneratorInterface<T> listGenerator) {
		super(id, listGenerator);
		// TODO Auto-generated constructor stub
	}

	public static <R extends FinancialTransaction> ITabWithKey newTab(
			final Component askingComponent,
			final String key,
			final ListGeneratorInterface<R> listGenerator) {
		
		return new AbstractTabWithKey(new StringResourceModel(key,
				askingComponent, null), key) {

			private static final long serialVersionUID = -3378395518127823461L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new TransactionTableListPanel<R>(panelId, listGenerator);
			}
		};
	}
	
	@Override
	protected void populateHeader() {
		this.add( ComponentsUtil.generateLabel("txtable.tx.label", "transaction-name-label", this) );
		this.add( ComponentsUtil.generateLabel("txtable.tx.commitment-value", "transaction-commitment-value-label", this) );
		this.add( ComponentsUtil.generateLabel("txtable.tx.sector-name", "transaction-sector-name-label", this) );
		this.add( ComponentsUtil.generateLabel("txtable.tx.reporting-org-name", "transaction-organization-name-label", this) );
		this.add( ComponentsUtil.generateLabel("txtable.tx.actions", "transaction-actions-label", this) );
		
	}

	@Override	
	protected void populateTable() {
		this.itemsListView		= new ListView<T>("transaction-list", items  ) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<T> ftListItem) {
				FinancialTransaction tempTx			= ftListItem.getModelObject();
				Label idLabel						= new Label("transaction-name", tempTx.getDescription() );
				ftListItem.add(idLabel);
                Label amountLabel = new Label("transaction-commitment-value", tempTx.getCommitments() == null ? "" : tempTx.getCommitments());
                ftListItem.add(amountLabel);
				Label descriptionLabel	= null;
				if ( tempTx.getSector() != null ) 
					descriptionLabel				= new Label("transaction-sector-name", tempTx.getSector().getName() );
				else
					descriptionLabel				= new Label("transaction-sector-name", "none" );
				ftListItem.add(descriptionLabel);
				Label orgLabel						= new Label("transaction-organization-name", tempTx.getReportingOrganization().getName() );
				ftListItem.add(orgLabel);
				
				
				AjaxLink<Long> editLink	= new AjaxLink<Long>("transaction-edit-link", Model.of(tempTx.getId()) ) {

					@Override
					public void onClick(AjaxRequestTarget target) {
						logger.info("Clicked edit on " + this.getModelObject());
					}
					
				};
				editLink.add( ComponentsUtil.generateLabel( "txtable.tx.edit-action", "transaction-edit-link-label", this) );
				ftListItem.add(editLink);
			}

		};
		
		this.add(itemsListView);
	}
}
