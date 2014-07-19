/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.exchange.liquibase;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.devgateway.eudevfin.exchange.dao.HistoricalExchangeRateDaoImplEndpoint;
import org.jadira.usertype.exchangerate.ExchangeRateConstants;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PopulateExchangeDbChangeLTL extends AbstractSpringCustomTaskChange {

	private static final MathContext mc=new MathContext(16,RoundingMode.CEILING);
	
	
	
	@Autowired
	private HistoricalExchangeRateDaoImplEndpoint exchangeRateDaoImplEndpoint;

	public BigDecimal invert(double d) {
		BigDecimal r=BigDecimal.ONE.divide(new BigDecimal(d,mc),8,RoundingMode.CEILING);
		return r;
	}
	
	@Override
	@Transactional
	public void execute(Database database) throws CustomChangeException {
		
		
	
		//LTL
		Currency LTL = Currency.getInstance("LTL");
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(4.0000), LocalDateTime.parse("2001-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(3.6733), LocalDateTime.parse("2002-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(3.0599), LocalDateTime.parse("2003-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(2.7808), LocalDateTime.parse("2004-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));

		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(2.7746), LocalDateTime.parse("2005-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(2.7513), LocalDateTime.parse("2006-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(2.5230), LocalDateTime.parse("2007-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(2.3569), LocalDateTime.parse("2008-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(2.4828), LocalDateTime.parse("2009-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(2.6067), LocalDateTime.parse("2010-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));		
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(2.4817), LocalDateTime.parse("2011-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(2.6867), LocalDateTime.parse("2012-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LTL), CurrencyUnit.USD,
				invert(2.6012), LocalDateTime.parse("2013-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		
	}

	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationErrors validate(Database database) {
		// TODO Auto-generated method stub
		return null;
	}

}
