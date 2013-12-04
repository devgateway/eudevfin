package org.devgateway.eudevfin.exchange.test;

/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    mpostelnicu
 ******************************************************************************/

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.exchange.job.HistoricalExchangeRateRetrieveJob;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.financial.service.OrganizationService;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml",
		"classpath:META-INF/exchangeContext.xml",
		"classpath:META-INF/commonExchangeContext.xml" })
public class TestHistoricalExchangeRateRetrieveJob {
	protected static Logger logger = Logger
			.getLogger(TestHistoricalExchangeRateRetrieveJob.class);

	@Autowired
	private HistoricalExchangeRateRetrieveJob exchangeRateRetrieveJob;

	@Autowired
	private FinancialTransactionService financialTransactionService;

	@Autowired
	private OrganizationService organizationService;

	@Test
	@Ignore("please enable only when doing real tests, this test eats up from the free number of queries we are allowed to make on openexchangerates")
	public void testfetchRatesForDate() {
		Organization o = new Organization();
		o.setName("Testing Org");
		organizationService.save(o);

		FinancialTransaction ft = new FinancialTransaction();
		ft.setAmount(new BigDecimal(123));
		ft.setDescription("Auditing test descr");
		ft.setReportingOrganization(o);
		ft.setCommitmentDate(LocalDateTime.now());

		financialTransactionService.save(ft);

		int totalFetched = exchangeRateRetrieveJob
				.retrieveHistoricalExchangeRates();
		Assert.assertNotEquals(totalFetched, 0);
	}

}
