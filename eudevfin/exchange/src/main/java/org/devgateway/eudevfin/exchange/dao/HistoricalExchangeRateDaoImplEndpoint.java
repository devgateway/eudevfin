/**
 * 
 */
package org.devgateway.eudevfin.exchange.dao;

import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.devgateway.eudevfin.exchange.repository.HistoricalExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 *
 */
@Component
public class HistoricalExchangeRateDaoImplEndpoint {
	
	@Autowired
	private HistoricalExchangeRateRepository repo;
	
	@ServiceActivator(inputChannel="getHistoricalExchangeRateByIdChannel")
	public HistoricalExchangeRate findById(String id) {
		return repo.findOne(id);
	}
	
	
	@ServiceActivator(inputChannel="createHistoricalExchangeRateChannel")
	public HistoricalExchangeRate saveHistoricalExchangeRate(HistoricalExchangeRate u) {
		repo.save(u);
		return u;
	}
}
