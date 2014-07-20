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
package org.devgateway.eudevfin.mcm.util;

import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.devgateway.eudevfin.exchange.common.service.HistoricalExchangeRateService;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.jadira.usertype.exchangerate.ExchangeRateConstants;
import org.springframework.data.domain.PageRequest;

/**
 * @author mihai
 * 
 */
public class HistoricalExchangeRateListGenerator implements ListGeneratorInterface<HistoricalExchangeRate> {

	private static final long serialVersionUID = -6936731401076768620L;
	private HistoricalExchangeRateService historicalExchangeRateService;
	private String searchString;

	public HistoricalExchangeRateListGenerator(HistoricalExchangeRateService historicalExchangeRateService,
			String searchString) {
		this.historicalExchangeRateService = historicalExchangeRateService;
		this.searchString = searchString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface
	 * #getResultsList(int, int)
	 */
	@Override
	public PagingHelper<HistoricalExchangeRate> getResultsList(int pageNumber, int pageSize) {
		return PagingHelper.createPagingHelperFromPage(historicalExchangeRateService.findByInvertedSource(ExchangeRateConstants.SOURCE_INTERNET,
				new PageRequest(pageNumber - 1, pageSize)));
	}

}
