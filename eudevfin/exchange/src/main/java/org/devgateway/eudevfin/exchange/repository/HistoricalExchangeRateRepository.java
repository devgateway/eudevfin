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
package org.devgateway.eudevfin.exchange.repository;

import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.joda.time.LocalDateTime;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 *
 */
@Component
public interface HistoricalExchangeRateRepository extends
		JpaRepository<HistoricalExchangeRate, Long> {
	
	/**
	 * Finds all {@link HistoricalExchangeRate}S with the given date parameter
	 * @param date a {@link LocalDateTime}
	 * @return the list with all {@link HistoricalExchangeRate}S that have the same date as the parameter
	 */
	Iterable<HistoricalExchangeRate> findByDate(LocalDateTime date);
	
	Page<HistoricalExchangeRate> findBySourceNot(String source, Pageable pageable);
	
}

