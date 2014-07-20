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

public class PopulateExchangeDbChange extends AbstractSpringCustomTaskChange {

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
		
		
		// fake rates
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.EUR, CurrencyUnit.USD,
				new BigDecimal(1.3133,mc), LocalDateTime.parse("2010-06-06"),
				ExchangeRateConstants.SOURCE_NATIONAL));

		// OECD real rates. Will be move to a nice XLS file at some point
 
		//EURO
		
		HistoricalExchangeRate historicalExchangeRate = HistoricalExchangeRate.of(CurrencyUnit.EUR, CurrencyUnit.USD,
				invert(0.7192), LocalDateTime.parse("2011-06-06"),ExchangeRateConstants.SOURCE_OECD);
		NullableWrapper<HistoricalExchangeRate> save = exchangeRateDaoImplEndpoint.save(historicalExchangeRate);
		
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.EUR, CurrencyUnit.USD,
				invert(0.778), LocalDateTime.parse("2012-06-06"),ExchangeRateConstants.SOURCE_OECD));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.EUR, CurrencyUnit.USD,
				invert(0.7532), LocalDateTime.parse("2013-06-06"),ExchangeRateConstants.SOURCE_OECD));

		
		//PLN
		Currency PLN=Currency.getInstance("PLN");		
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(PLN), CurrencyUnit.USD,
				invert(2.9621), LocalDateTime.parse("2011-06-06"),ExchangeRateConstants.SOURCE_OECD));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(PLN), CurrencyUnit.USD,
				invert(3.2518), LocalDateTime.parse("2012-06-06"),ExchangeRateConstants.SOURCE_OECD));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(PLN), CurrencyUnit.USD,
				invert(3.1596), LocalDateTime.parse("2013-06-06"),ExchangeRateConstants.SOURCE_OECD));

		//HUF
		Currency HUF=Currency.getInstance("HUF");		
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(HUF), CurrencyUnit.USD,
				invert(200.9068), LocalDateTime.parse("2011-06-06"),ExchangeRateConstants.SOURCE_OECD));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(HUF), CurrencyUnit.USD,
				invert(224.823), LocalDateTime.parse("2012-06-06"),ExchangeRateConstants.SOURCE_OECD));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(HUF), CurrencyUnit.USD,
				invert(223.5404), LocalDateTime.parse("2013-06-06"),ExchangeRateConstants.SOURCE_OECD));


		//CZK
		Currency CZK=Currency.getInstance("CZK");		
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(CZK), CurrencyUnit.USD,
				invert(17.6722), LocalDateTime.parse("2011-06-06"),ExchangeRateConstants.SOURCE_OECD));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(CZK), CurrencyUnit.USD,
				invert(19.5383), LocalDateTime.parse("2012-06-06"),ExchangeRateConstants.SOURCE_OECD));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(CZK), CurrencyUnit.USD,
				invert(19.5585), LocalDateTime.parse("2013-06-06"),ExchangeRateConstants.SOURCE_OECD));

		
		
		//BGN
		Currency BGN=Currency.getInstance("BGN");		
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(BGN), CurrencyUnit.USD,
				invert(1.4066), LocalDateTime.parse("2011-06-06"),ExchangeRateConstants.SOURCE_OECD));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(BGN), CurrencyUnit.USD,
				invert(1.523), LocalDateTime.parse("2012-06-06"),ExchangeRateConstants.SOURCE_OECD));
//		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(BGN), CurrencyUnit.USD,
//				new BigDecimal(1/17.6722,mc), LocalDateTime.parse("2013-06-06"),ExchangeRateConstants.EXCHANGE_RATE_OECD));
		
		
		//RON
		Currency RON = Currency.getInstance("RON");
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(RON), CurrencyUnit.USD,
				invert(3.3279), LocalDateTime.parse("2013-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(RON), CurrencyUnit.USD,
				invert(3.4682), LocalDateTime.parse("2012-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(RON), CurrencyUnit.USD,
				invert(3.0486), LocalDateTime.parse("2011-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(RON), CurrencyUnit.USD,
				invert(3.1779), LocalDateTime.parse("2010-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
	
		
		
		//HRK
		Currency HRK = Currency.getInstance("HRK");
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(HRK), CurrencyUnit.USD,
				invert(5.500015), LocalDateTime.parse("2010-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(HRK), CurrencyUnit.USD,
				invert(5.343508), LocalDateTime.parse("2011-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(HRK), CurrencyUnit.USD,
				invert(5.850861), LocalDateTime.parse("2012-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(HRK), CurrencyUnit.USD,
				invert(5.705883), LocalDateTime.parse("2013-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));

		
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
