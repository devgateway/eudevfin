package org.devgateway.eudevfin.exchange.service;

import java.util.LinkedHashMap;

import org.joda.time.LocalDateTime;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.Payload;

/**
 * Exchange service gateway, it controls the outbound gateways that connect to
 * the external json service that provides exchange rates
 * 
 * @author mihai
 * 
 */
public interface ExternalExchangeQueryService {

	/**
	 * Retrieves all the currencies from the openexchangerates.org and returns a
	 * {@link LinkedHashMap} with [currency,name]
	 * 
	 * @return
	 */
	@Gateway(requestChannel = "getCurrenciesJson")
	@Payload("new java.util.Date()")
	LinkedHashMap<String, String> getCurrenciesAndNames();

	/**
	 * Retrieves all exchange rates for the given date
	 * 
	 * @param date
	 *            the date
	 * @return a full hashmap with all currencies and rates, with USD as base
	 */
	@Gateway(requestChannel = "getExchangeRateChannel")
	LinkedHashMap<String, Object> getExchangeRatesForDate(LocalDateTime date);

}
