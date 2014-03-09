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
import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.devgateway.eudevfin.exchange.dao.HistoricalExchangeRateDaoImplEndpoint;
import org.jadira.usertype.exchangerate.ExchangeRateConstants;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PopulateExchangeDbChangeLVL extends AbstractSpringCustomTaskChange {

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
		//LVL
		Currency LVL = Currency.getInstance("LVL");
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LVL), CurrencyUnit.USD,
				invert(0.5469), LocalDateTime.parse("2012-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
		exchangeRateDaoImplEndpoint.save(HistoricalExchangeRate.of(CurrencyUnit.of(LVL), CurrencyUnit.USD,
				invert(0.5295), LocalDateTime.parse("2013-06-06"), ExchangeRateConstants.SOURCE_NATIONAL));
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
