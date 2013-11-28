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
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * @author mihai
 *
 */
@Component
public interface HistoricalExchangeRateRepository extends
		PagingAndSortingRepository<HistoricalExchangeRate, Long> {
}

