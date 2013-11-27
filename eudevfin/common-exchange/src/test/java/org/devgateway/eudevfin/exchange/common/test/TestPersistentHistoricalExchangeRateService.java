package org.devgateway.eudevfin.exchange.common.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml" })
public class TestPersistentHistoricalExchangeRateService {

	/**
	 * Creates an exhange rate locally, no S-I
	 */
	@Test
	public void testCreateHistoricalExchangeRate() {
		
	}
}
