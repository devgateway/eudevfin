/**
 * 
 */
package org.devgateway.eudevfin.exchange.common.service;

import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;

/**
 * @author mihai
 *
 */
public interface HistoricalExchangeRateService {
	HistoricalExchangeRate save(HistoricalExchangeRate t);

	HistoricalExchangeRate findById(String id);
}
