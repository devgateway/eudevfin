package org.devgateway.eudevfin.exchange.common.job;

import java.util.Collection;

import org.joda.time.LocalDateTime;
import org.springframework.integration.annotation.Payload;

public interface HistoricalExchangeRateRetrievable {


	
	@Payload("new java.util.Date()")
	public Collection<LocalDateTime> getHistoricalExchangeRates();
	
}
