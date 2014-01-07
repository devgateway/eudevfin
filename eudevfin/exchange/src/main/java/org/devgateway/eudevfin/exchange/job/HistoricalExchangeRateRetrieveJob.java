package org.devgateway.eudevfin.exchange.job;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.devgateway.eudevfin.exchange.common.job.HistoricalExchangeRateRetrievable;
import org.devgateway.eudevfin.exchange.common.service.HistoricalExchangeRateService;
import org.devgateway.eudevfin.exchange.dao.HistoricalExchangeRateDaoImplEndpoint;
import org.joda.time.LocalDateTime;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HistoricalExchangeRateRetrieveJob implements
		ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Autowired
	private HistoricalExchangeRateService exchangeRateService;

	@Scheduled(cron = "0 0 6 * * MON-FRI")
	public void jobRetrieveHistoricalExchangeRates() {
		retrieveHistoricalExchangeRates();
	}

	public int retrieveHistoricalExchangeRates() {

		Reflections reflections = new Reflections(
				ClasspathHelper.forPackage("org.devgateway.eudevfin"), 
				new SubTypesScanner());

		// get all implemented subtypes of HistoricalExchangeRateRetrievable
		Set<Class<? extends HistoricalExchangeRateRetrievable>> allSubTypesofHistExchRateRetrievable = reflections
				.getSubTypesOf(HistoricalExchangeRateRetrievable.class);

		TreeSet<LocalDateTime> datesSet = new TreeSet<LocalDateTime>();

		for (Class<? extends HistoricalExchangeRateRetrievable> retrievable : allSubTypesofHistExchRateRetrievable) {

			// get the bean from app context
			HistoricalExchangeRateRetrievable service = applicationContext
					.getBean(retrievable);

			// get all the rates from db
			Collection<LocalDateTime> historicalExchangeRates = service
					.getHistoricalExchangeRates();
			
			//add them to a treeset
			datesSet.addAll(historicalExchangeRates);
		}

		int totalFetched = 0;
		for (LocalDateTime localDateTime : datesSet) {
			//fetch the rates for new dates that don't have rates, from the internet			//
			totalFetched += exchangeRateService
					.fetchRatesForDate(localDateTime);
		}

		return totalFetched;

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}
}
