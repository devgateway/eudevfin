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
package org.devgateway.eudevfin.exchange.common.service;

import org.devgateway.eudevfin.common.service.BaseEntityService;
import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.joda.time.LocalDateTime;

/**
 * @author mihai
 * 
 */
public interface HistoricalExchangeRateService  extends BaseEntityService<HistoricalExchangeRate> {

		/**
	 * Finds the {@link HistoricalExchangeRate}S from the database for the
	 * given date
	 * 
	 * @param date
	 *            the date
	 * @return returns the {@link HistoricalExchangeRate}S
	 */
	Iterable<HistoricalExchangeRate> findRatesForDate(LocalDateTime date);

	/**
	 * Checks the database to see if {@link HistoricalExchangeRate} do exist for
	 * the given date. If they don't then it fetches them from the net and
	 * caches them in the database
	 * 
	 * @param date
	 *            the date
	 * @return the number of {@link HistoricalExchangeRate} persisted, or 0 if
	 *         the date already has exchange rates
	 */
	int fetchRatesForDate(LocalDateTime date);
}
