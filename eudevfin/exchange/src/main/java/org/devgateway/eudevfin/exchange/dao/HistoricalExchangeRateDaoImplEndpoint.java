/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    mpostelnicu
 */

/**
 * 
 */
package org.devgateway.eudevfin.exchange.dao;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.devgateway.eudevfin.exchange.common.service.HistoricalExchangeRateService;
import org.devgateway.eudevfin.exchange.repository.HistoricalExchangeRateRepository;
import org.devgateway.eudevfin.exchange.service.ExternalExchangeQueryService;
import org.joda.money.CurrencyUnit;
import org.joda.money.ExchangeRate;
import org.joda.money.IllegalCurrencyException;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * @author mihai Endpoint for the spring integration service to access exchange
 *         rates Do NOT use this on the client side!
 * @see HistoricalExchangeRateService
 */
@Component
public class HistoricalExchangeRateDaoImplEndpoint {

	@Autowired
	private HistoricalExchangeRateRepository repo;

	@Autowired
	ExternalExchangeQueryService exchangeQueryService;

	@Autowired
	HistoricalExchangeRateService service;

	protected static Logger logger = Logger
			.getLogger(HistoricalExchangeRateDaoImplEndpoint.class);

	/**
	 * @see HistoricalExchangeRateService#findById(Long)
	 * @param id
	 * @return
	 */
	@ServiceActivator(inputChannel = "getHistoricalExchangeRateByIdChannel")
	public HistoricalExchangeRate findById(Long id) {
		return repo.findOne(id);
	}

	/**
	 * @see HistoricalExchangeRateService#save(HistoricalExchangeRate)
	 * @param u
	 * @return
	 */
	@ServiceActivator(inputChannel = "createHistoricalExchangeRateChannel")
	public HistoricalExchangeRate saveHistoricalExchangeRate(
			HistoricalExchangeRate u) {
		repo.save(u);
		return u;
	}

	/**
	 * @see HistoricalExchangeRateService#deleteRatesForDate(LocalDateTime)
	 * @param date
	 * @return
	 */
	@ServiceActivator(inputChannel = "findRatesForDateChannel")
	public Iterable<HistoricalExchangeRate> findRatesForDate(LocalDateTime date) {
		Iterable<HistoricalExchangeRate> findByDate = repo.findByDate(date);
		return findByDate;
	}

	/**
	 * @see HistoricalExchangeRateService#fetchRatesForDate(LocalDateTime)
	 * @param date
	 * @return
	 */
	@ServiceActivator(inputChannel = "fetchRatesForDateChannel")
	public int fetchRatesForDate(LocalDateTime date) {

		int savedRates = 0;

		// rates exist already for the given date, currently we skip any more
		// rates fetching.
		// we can parameterize this l8r
		if (service.findRatesForDate(date).iterator().hasNext())
			return savedRates;

		
		LinkedHashMap<String, Object> mapFromJson = exchangeQueryService
				.getExchangeRatesForDate(date);

		CurrencyUnit baseUnit = CurrencyUnit.of((String) mapFromJson.get("base"));
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Number> rates = (LinkedHashMap<String, Number>) mapFromJson
				.get("rates");

		Iterator<String> iterator = rates.keySet().iterator();
		while (iterator.hasNext()) {
			String currency = (String) iterator.next();
			CurrencyUnit counterUnit = null;
			try {
				counterUnit = CurrencyUnit.of(currency);
			} catch (IllegalCurrencyException e) {
				logger.warn("Unkown currency " + currency
						+ ". Will not import the exchange rate!");
				continue;
			}
			HistoricalExchangeRate her = new HistoricalExchangeRate();
			her.setDate(date);

			her.setRate(ExchangeRate.of(baseUnit, counterUnit, new BigDecimal(
					rates.get(currency).toString())));
			service.save(her);
			savedRates++;
		}
		return savedRates;
	}

}
