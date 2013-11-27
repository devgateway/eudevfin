package org.devgateway.eudevfin.exchange.test;

import java.math.BigDecimal;

import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.devgateway.eudevfin.exchange.repository.HistoricalExchangeRateRepository;
import org.joda.money.CurrencyUnit;
import org.joda.money.ExchangeRate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml",
		"classpath:META-INF/exchangeContext.xml"})
public class TestPersistentHistoricalExchangeRateRepository {

	
	@Autowired
	private HistoricalExchangeRateRepository repo;
	
	/**
	 * Creates an exhange rate locally, no S-I
	 */
	@Test
	public void testCreateHistoricalExchangeRate() {
		
		HistoricalExchangeRate her=new HistoricalExchangeRate();
		her.setDate(LocalDateTime.now());
		her.setRate(ExchangeRate.of(CurrencyUnit.USD, CurrencyUnit.EUR, BigDecimal.valueOf(1.33d)));
		repo.save(her);
	}
}
