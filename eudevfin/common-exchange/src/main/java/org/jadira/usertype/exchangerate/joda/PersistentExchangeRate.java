package org.jadira.usertype.exchangerate.joda;

import org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency;
import org.jadira.usertype.moneyandcurrency.joda.columnmapper.BigDecimalBigDecimalColumnMapper;
import org.jadira.usertype.moneyandcurrency.joda.columnmapper.StringColumnCurrencyUnitMapper;
import org.jadira.usertype.spi.shared.AbstractMultiColumnUserType;
import org.jadira.usertype.spi.shared.ColumnMapper;
import org.joda.money.CurrencyUnit;
import org.joda.money.ExchangeRate;

import java.math.BigDecimal;

/**
 * Persists the decimal amount and currency from an {@link ExchangeRate}
 * instance. Uses the same pattern as {@link PersistentBigMoneyAmountAndCurrency}
 * @author mihai
 * @since 20.nov.2013
 */
public class PersistentExchangeRate extends
		AbstractMultiColumnUserType<ExchangeRate> {

	private static final long serialVersionUID = -3224638442105007299L;

	private static final ColumnMapper<?, ?>[] COLUMN_MAPPERS = new ColumnMapper<?, ?>[] {
			new StringColumnCurrencyUnitMapper(),
			new StringColumnCurrencyUnitMapper(),
			new BigDecimalBigDecimalColumnMapper() };

	private static final String[] PROPERTY_NAMES = new String[] { "base",
			"counter", "rate" };

	@Override
	protected ColumnMapper<?, ?>[] getColumnMappers() {
		return COLUMN_MAPPERS;
	}

	@Override
	protected ExchangeRate fromConvertedColumns(Object[] convertedColumns) {

		CurrencyUnit basePart = (CurrencyUnit) convertedColumns[0];
		CurrencyUnit counterPart = (CurrencyUnit) convertedColumns[1];
		BigDecimal ratePart = (BigDecimal) convertedColumns[2];

		ExchangeRate rate = ExchangeRate.of(basePart, counterPart, ratePart);

		return rate;
	}

	@Override
	protected Object[] toConvertedColumns(ExchangeRate value) {

		return new Object[] { value.getBase(), value.getCounter(),
				value.getRate() };
	}

	@Override
	public String[] getPropertyNames() {
		return PROPERTY_NAMES;
	}
}
