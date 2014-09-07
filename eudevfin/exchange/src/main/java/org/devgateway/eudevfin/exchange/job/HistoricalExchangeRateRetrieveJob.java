/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.exchange.job;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.exchange.common.domain.ExchangeRateConfiguration;
import org.devgateway.eudevfin.exchange.common.domain.ExchangeRateConfigurationConstants;
import org.devgateway.eudevfin.exchange.common.service.ExchangeRateConfigurationService;
import org.devgateway.eudevfin.exchange.common.service.HistoricalExchangeRateService;
import org.devgateway.eudevfin.financial.service.HistoricalExchangeRateRetrievable;
import org.joda.time.LocalDateTime;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

@Service
public class HistoricalExchangeRateRetrieveJob implements ApplicationContextAware {
    private static final Logger logger = Logger.getLogger(HistoricalExchangeRateRetrieveJob.class);

	private static ApplicationContext applicationContext;

    private ExchangeRateConfiguration exchangeJobActivey;

	@Autowired
	private HistoricalExchangeRateService exchangeRateService;

    @Autowired
    private ExchangeRateConfigurationService exchangeRateConfigurationService;

    // for some reason Spring Annotation @Scheduled is not working
    // moved the configuration in 'task:scheduled-tasks' tag in the XML configuration file
	public void jobRetrieveHistoricalExchangeRates() {
        logger.debug("Run Retrieve Historical Exchange Rates Job!");

        // fetch the open exchange properties
        exchangeJobActivey = exchangeRateConfigurationService.
                findByEntityKey(ExchangeRateConfigurationConstants.EXCHANGE_JOB_ACTIVE).getEntity();

        // check if the key exists and if job is active
        if (exchangeJobActivey != null && exchangeJobActivey.getEntitValue().
                equals(ExchangeRateConfigurationConstants.EXCHANGE_JOB_ACTIVE_TRUE)) {
            retrieveHistoricalExchangeRates();
        }
	}

	public int retrieveHistoricalExchangeRates() {
		Reflections reflections = new Reflections(
				ClasspathHelper.forPackage("org.devgateway.eudevfin"), 
				new SubTypesScanner());

		// get all implemented subtypes of HistoricalExchangeRateRetrievable
		Set<Class<? extends HistoricalExchangeRateRetrievable>> allSubTypesofHistExchRateRetrievable = reflections
				.getSubTypesOf(HistoricalExchangeRateRetrievable.class);

		TreeSet<LocalDateTime> datesSet = new TreeSet<>();

		for (Class<? extends HistoricalExchangeRateRetrievable> retrievable : allSubTypesofHistExchRateRetrievable) {
			// get the bean from app context
			HistoricalExchangeRateRetrievable service = applicationContext
					.getBean(retrievable);

			// get all the rates from db
			Collection<LocalDateTime> historicalExchangeRates = service
					.getHistoricalExchangeRates();
			
			// add them to a treeset
			datesSet.addAll(historicalExchangeRates);
		}

		int totalFetched = 0;
		for (LocalDateTime localDateTime : datesSet) {
			// fetch the rates for new dates that don't have rates, from the internet
			totalFetched += exchangeRateService
					.fetchRatesForDate(localDateTime);
		}

        logger.debug("totalFetched: " + totalFetched);

		return totalFetched;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
