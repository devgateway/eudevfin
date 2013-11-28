/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 */

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
	public HistoricalExchangeRate findById(Long id) {
		return repo.findOne(id);
	}
	
	
	@ServiceActivator(inputChannel="createHistoricalExchangeRateChannel")
	public HistoricalExchangeRate saveHistoricalExchangeRate(HistoricalExchangeRate u) {
		repo.save(u);
		return u;
	}
}
