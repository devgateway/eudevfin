/** 
 * 
 */
package org.devgateway.eudevfin.mcm.util;

import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.devgateway.eudevfin.exchange.common.service.HistoricalExchangeRateService;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
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
		return PagingHelper.createPagingHelperFromPage(historicalExchangeRateService.findByGeneralSearchPageable(
				searchString, null, new PageRequest(pageNumber - 1, pageSize)));
	}

}
