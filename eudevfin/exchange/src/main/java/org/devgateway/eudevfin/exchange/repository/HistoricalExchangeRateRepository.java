/**
 * 
 */
package org.devgateway.eudevfin.exchange.repository;

import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 *
 */
@Component
public interface HistoricalExchangeRateRepository extends
		PagingAndSortingRepository<HistoricalExchangeRate, String> {
}

