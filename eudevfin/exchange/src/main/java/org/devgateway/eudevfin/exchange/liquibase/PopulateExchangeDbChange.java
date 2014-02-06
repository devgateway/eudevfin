package org.devgateway.eudevfin.exchange.liquibase;

import java.math.BigDecimal;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.devgateway.eudevfin.exchange.dao.HistoricalExchangeRateDaoImplEndpoint;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PopulateExchangeDbChange extends AbstractSpringCustomTaskChange {

	@Autowired
	private HistoricalExchangeRateDaoImplEndpoint exchangeRateDaoImplEndpoint; 

	@Override
	@Transactional
	public void execute(Database database) throws CustomChangeException {		
		//add some hard coded exchange rates
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.EUR, CurrencyUnit.USD, BigDecimal.valueOf(1.3133), LocalDateTime.parse("2010-06-06")));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.EUR, CurrencyUnit.USD, BigDecimal.valueOf(1.2949), LocalDateTime.parse("2011-06-06")));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.EUR, CurrencyUnit.USD, BigDecimal.valueOf(1.3215), LocalDateTime.parse("2012-06-06")));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.EUR, CurrencyUnit.USD, BigDecimal.valueOf(1.3766), LocalDateTime.parse("2013-06-06")));
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
