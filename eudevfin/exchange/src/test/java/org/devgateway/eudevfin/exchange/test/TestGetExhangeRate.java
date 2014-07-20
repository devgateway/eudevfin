package org.devgateway.eudevfin.exchange.test;

/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.exchange.common.service.HistoricalExchangeRateService;
import org.devgateway.eudevfin.exchange.service.ExternalExchangeQueryService;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This should test fetching external exchange rates from openexchangerates.org
 * for a given date It tests the functionality of
 * {@link HistoricalExchangeRateService#fetchRatesForDate(LocalDateTime)}
 * 
 * @author mpostelnicu
 * @since 23 October 2013
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml",
		"classpath:META-INF/exchangeContext.xml",
		"classpath:META-INF/commonExchangeContext.xml" })
public class TestGetExhangeRate {
	protected static Logger logger = Logger.getLogger(TestGetExhangeRate.class);

	@Autowired
	ExternalExchangeQueryService exchangeQueryService;

	@Autowired
	HistoricalExchangeRateService service;

	@Test
	@Ignore("testing this will eat up our 1000 requests per month limit on openexchangerates.org. Keep it off for now!")
	public void testfetchRatesForDate() {
		String someDateString = "2010-10-10";
		LocalDateTime localDateTime = LocalDateTime.parse(someDateString,
				ISODateTimeFormat.date());

		Assert.assertEquals(someDateString,
				localDateTime.toString(ISODateTimeFormat.date()));

		int fetchRatesForDate = service.fetchRatesForDate(localDateTime);
		Assert.assertNotEquals(fetchRatesForDate, 0);

	}

}
