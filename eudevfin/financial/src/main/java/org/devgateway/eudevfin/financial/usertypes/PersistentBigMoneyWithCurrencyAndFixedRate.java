/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.financial.usertypes;

import java.math.BigDecimal;

import org.devgateway.eudevfin.financial.BigMoneyWithCurrencyAndFixedRate;
import org.jadira.usertype.moneyandcurrency.joda.columnmapper.BigDecimalBigDecimalColumnMapper;
import org.jadira.usertype.moneyandcurrency.joda.columnmapper.StringColumnCurrencyUnitMapper;
import org.jadira.usertype.spi.shared.AbstractMultiColumnUserType;
import org.jadira.usertype.spi.shared.ColumnMapper;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.money.ExchangeRate;

/**
 * @author Alex
 *
 */
public class PersistentBigMoneyWithCurrencyAndFixedRate extends
		AbstractMultiColumnUserType<BigMoneyWithCurrencyAndFixedRate> {

	private static final long serialVersionUID = 6611621344417365421L;

	private static final ColumnMapper<?, ?>[] COLUMN_MAPPERS = new ColumnMapper<?, ?>[] {
			new StringColumnCurrencyUnitMapper(),
			new BigDecimalBigDecimalColumnMapper(),
			new StringColumnCurrencyUnitMapper(),
			new BigDecimalBigDecimalColumnMapper()};

	private static final String[] PROPERTY_NAMES = new String[] { "currencyUnit", "amount", "counter", "rate" };

	@Override
	protected ColumnMapper<?, ?>[] getColumnMappers() {
		return COLUMN_MAPPERS;
	}

	@Override
	protected BigMoneyWithCurrencyAndFixedRate fromConvertedColumns(Object[] convertedColumns) {

		CurrencyUnit basePart 		= (CurrencyUnit) convertedColumns[0];
		BigDecimal amountPart 		= (BigDecimal) convertedColumns[1];
		CurrencyUnit counterPart 	= (CurrencyUnit) convertedColumns[2];
		BigDecimal ratePart 		= (BigDecimal) convertedColumns[3];

		ExchangeRate rate 	= ExchangeRate.of(basePart, counterPart, ratePart);
		BigMoney money 		= BigMoney.of(basePart, amountPart);
		
		BigMoneyWithCurrencyAndFixedRate moneyWithFixedRate	= BigMoneyWithCurrencyAndFixedRate.of(money, rate);

		return moneyWithFixedRate;
	}

	@Override
	protected Object[] toConvertedColumns(BigMoneyWithCurrencyAndFixedRate value) {

		ExchangeRate rate	= value.getRate();
		BigMoney money		= value.getMoney();
		
		return new Object[] { rate.getBase(), money.getAmount(), rate.getCounter(),
				rate.getRate() };
	}

	@Override
	public String[] getPropertyNames() {
		return PROPERTY_NAMES;
	}

}
