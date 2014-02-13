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
package org.devgateway.eudevfin.mcm.components;

import java.text.SimpleDateFormat;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.devgateway.eudevfin.mcm.pages.EditHistoricalExchangeRatePage;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;

/**
 * @author mihai
 * 
 */
public class HistoricalExchangeRateTableListPanel extends TableListPanel<HistoricalExchangeRate> {

	private static final long serialVersionUID = 7403189032489301520L;

	public HistoricalExchangeRateTableListPanel(String id, ListGeneratorInterface<HistoricalExchangeRate> listGenerator) {
		super(id, listGenerator);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void populateTable() {
		this.itemsListView = new ListView<HistoricalExchangeRate>("list", items) {

			private static final long serialVersionUID = -8758662617501215830L;

			@Override
			protected void populateItem(ListItem<HistoricalExchangeRate> listItem) {
				final HistoricalExchangeRate rate = listItem.getModelObject();

				Link linkToEditRate = new Link("linkToEdit") {
					@Override
					public void onClick() {
						PageParameters pageParameters = new PageParameters();
						pageParameters.add(EditHistoricalExchangeRatePage.PARAM_ID, rate.getId());
						setResponsePage(EditHistoricalExchangeRatePage.class, pageParameters);
					}
				};
				linkToEditRate.setBody( new StringResourceModel("link.edit", this, null, null));

				
				
				Label baseCurrencyLabel = new Label("baseCurrency", rate.getRate().getBase().getCode());
				Label counterCurrencyLabel = new Label("counterCurrency", rate.getRate().getCounter().getCode());
				Label source = new Label("source", rate.getSource());
				Label dateLabel = new Label("date", rate.getDate().toString("dd-MM-yyyy"));
				Label rateLabel = new Label("rate", rate.getRate().getRate());

				listItem.add(baseCurrencyLabel);
				listItem.add(counterCurrencyLabel);
				listItem.add(source);
				listItem.add(dateLabel);
				listItem.add(linkToEditRate);				
				listItem.add(rateLabel);
			}

		};

		this.add(itemsListView);

	}
}
