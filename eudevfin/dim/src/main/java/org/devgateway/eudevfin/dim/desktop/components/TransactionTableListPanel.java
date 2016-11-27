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

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.dim.desktop.components.util.ComponentsUtil;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.CustomTransactionPage;
import org.devgateway.eudevfin.dim.pages.transaction.custom.ViewCustomTransactionPage;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.ui.common.Constants;
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
	public TransactionTableListPanel(final String id,
			final ListGeneratorInterface<T> listGenerator) {
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
			public WebMarkupContainer getPanel(final String panelId) {
				return new TransactionTableListPanel<R>(panelId, listGenerator);
			}
		};
	}

	protected Link getTransactionEditLink(final FinancialTransaction tempTx) {
		return new Link("transaction-edit-link") {
			private static final long serialVersionUID = 9084184844700618410L;

			@Override
			public void onClick() {
				logger.info("Clicked edit on " + this.getModelObject());
				final PageParameters pageParameters = new PageParameters();
				pageParameters.add(TransactionPage.PARAM_TRANSACTION_ID, tempTx.getId());

				// maybe we need to subclass this page and create a custom table
				// for custom transactions
				// for the moment we play dumb and use instanceof

				if (tempTx instanceof CustomFinancialTransaction) {
					pageParameters.add(Constants.PARAM_TRANSACTION_TYPE,
							((CustomFinancialTransaction) tempTx).getFormType());
					this.setResponsePage(CustomTransactionPage.class, pageParameters);
				} else {
					this.setResponsePage(TransactionPage.class, pageParameters);
				}
			}
		};
	}

	@Override
	protected void populateHeader() {
		this.add( ComponentsUtil.generateLabel("txtable.tx.label", "transaction-name-label", this) );
		this.add( ComponentsUtil.generateLabel("txtable.tx.form-type", "transaction-form-type-label", this) );
		this.add( ComponentsUtil.generateLabel("txtable.tx.reporting-year", "transaction-reporting-year-label", this) );
		this.add( ComponentsUtil.generateLabel("txtable.tx.sector-name", "transaction-sector-name-label", this) );
		this.add( ComponentsUtil.generateLabel("txtable.tx.reporting-org-name", "transaction-organization-name-label", this) );
                this.add( ComponentsUtil.generateLabel("txtable.tx.extended-amount", "transaction-extended-amount-label", this) );
		this.add( ComponentsUtil.generateLabel("txtable.tx.actions", "transaction-actions-label", this) );

	}

	@Override
	protected void populateTable() {
		this.itemsListView		= new ListView<T>("transaction-list", this.items  ) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<T> ftListItem) {
				final FinancialTransaction tempTx			= ftListItem.getModelObject();
				final Label idLabel						= new Label("transaction-name", tempTx.getShortDescription());
				ftListItem.add(idLabel);

				String tempFt=null;
				if (tempTx instanceof CustomFinancialTransaction) {
					tempFt = new StringResourceModel(((CustomFinancialTransaction) tempTx).getFormType(),
							TransactionTableListPanel.this, null, null).getString();
				}
				final Label formType						= new Label("transaction-form-type", tempFt);
				ftListItem.add(formType);

				final Label reportingYear = new Label("transaction-reporting-year", tempTx.getReportingYear() == null ? "" : tempTx.getReportingYear().getYear());
				ftListItem.add(reportingYear);
				Label descriptionLabel	= null;
				if ( tempTx.getSector() != null ) {
					descriptionLabel				= new Label("transaction-sector-name", tempTx.getSector().getName() );
				} else {
					descriptionLabel				= new Label("transaction-sector-name", "none" );
				}
				ftListItem.add(descriptionLabel);
				final String extAgencyName 			= tempTx.getExtendingAgency() != null ? tempTx.getExtendingAgency().getName() : " - ";
				final Label orgLabel				= new Label("transaction-organization-name", extAgencyName );
				ftListItem.add(orgLabel);
                                
                                final String extendedAmount 			= tempTx.getAmountsExtended().getAmount().toString();
				final Label extendedAmountLabel			= new Label("transaction-extended-amount", extendedAmount );
				ftListItem.add(extendedAmountLabel);


				final Link editLink = TransactionTableListPanel.this.getTransactionEditLink(tempTx);
				editLink.add( ComponentsUtil.generateLabel( "txtable.tx.edit-action", "transaction-edit-link-label", this) );
				ftListItem.add(editLink);

				final Link viewLink	= new Link("transaction-view-link") {
					private static final long serialVersionUID = 9084184844700618410L;

					@Override
					public void onClick() {
						final PageParameters pageParameters = new PageParameters();
						pageParameters.add(TransactionPage.PARAM_TRANSACTION_ID, tempTx.getId());
						this.setResponsePage(ViewCustomTransactionPage.class, pageParameters);
					}

				};

				viewLink.add( ComponentsUtil.generateLabel( "txtable.tx.view-action", "transaction-view-link-label", this) );
				ftListItem.add(viewLink);

			}

		};

		this.add(this.itemsListView);
	}
}
