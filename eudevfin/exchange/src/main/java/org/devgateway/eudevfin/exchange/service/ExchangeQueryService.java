package org.devgateway.eudevfin.exchange.service;

import java.util.LinkedHashMap;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.Payload;

public interface ExchangeQueryService {

	/**
	 * Retrieves all the currencies from the openexchangerates.org
	 * and returns a {@link LinkedHashMap} with [currency,name]
	 * @return
	 */
	@Gateway(requestChannel="getCurrenciesJson")
	@Payload("new java.util.Date()")
	LinkedHashMap<String,String> getCurrenciesAndNames();

}
