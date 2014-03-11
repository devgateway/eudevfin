/**
 * 
 */
package org.devgateway.eudevfin.financial;

import java.math.BigDecimal;

import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.money.ExchangeRate;

/**
 * @author Alex
 *
 */
public final class BigMoneyWithCurrencyAndFixedRate {
    private BigMoney money;
	
	private ExchangeRate rate;
	
	BigMoneyWithCurrencyAndFixedRate(BigMoney money, ExchangeRate rate) {
		super();
		this.money = money;
		this.rate = rate;
	}
	
	public static BigMoneyWithCurrencyAndFixedRate of (BigMoney money, ExchangeRate exchangeRate) {
		
		BigMoneyWithCurrencyAndFixedRate moneyWithFixedRate	= new BigMoneyWithCurrencyAndFixedRate(money, exchangeRate);
		return moneyWithFixedRate;
	}
	
	/**
	 * 
	 * @param currency the currency of the money, it's also the base currency for the exchange rate
	 * @param amount the amount of the money
	 * @param counterCurrency it's relatively to this that the currency is compared . <em> This can be null if there's no fixed exchange rate</em>
	 * @param rate the exchange rate: 1 currency = rate counterCurrency <em> This can be null if there's no fixed exchange rate</em>
	 * @return a new instance of {@link BigMoneyWithCurrencyAndFixedRate}
	 */
	public static BigMoneyWithCurrencyAndFixedRate of (String currency, BigDecimal amount, 
			String counterCurrency, BigDecimal rate) {
		
		if ( currency == null || amount == null )
			throw new NullPointerException("Currency and amount are not supposed to be null.");
		
		CurrencyUnit currencyUnit		= CurrencyUnit.getInstance(currency);
		BigMoney money					= BigMoney.of(currencyUnit, amount);
		
		ExchangeRate exchangeRate	= null;
		if ( counterCurrency != null && rate != null ) {
			CurrencyUnit counterCurrencyUnit	= CurrencyUnit.getInstance(counterCurrency);
			exchangeRate			= ExchangeRate.of(currencyUnit, counterCurrencyUnit, rate);
		}
		
		
		BigMoneyWithCurrencyAndFixedRate moneyWithFixedRate	= new BigMoneyWithCurrencyAndFixedRate(money, exchangeRate);
		return moneyWithFixedRate;
	}
	
	/**
	 * It's a wrapper over {@link #of(String,BigDecimal,String,BigDecimal) of} with the last 2 parameters null. 
	 * @param currency
	 * @param amount
	 * @return BigMoneyWithCurrencyAndFixedRate instance with no (null) fixed exchange rate
	 */
	public static BigMoneyWithCurrencyAndFixedRate of (String currency, BigDecimal amount ) {
		return BigMoneyWithCurrencyAndFixedRate.of(currency, amount, null, null);
	}
	
	/**
	 * Simply uses the parse methods from {@link BigMoney} and {@link ExchangeRate} to 
	 * instantiate a {@link BigMoneyWithCurrencyAndFixedRate} object
	 * @param parsableBigMoney string representation of a currency and amount
	 * @param parsableExchangeRate string representation of an exchange rate between 2 currencies. 
	 * <em> This can be null if there's no fixed exchange rate</em>
	 * @return a new instance of {@link BigMoneyWithCurrencyAndFixedRate}
	 */
	public static BigMoneyWithCurrencyAndFixedRate parse(String parsableBigMoney, String parsableExchangeRate) {
		BigMoney money				= BigMoney.parse(parsableBigMoney);
		ExchangeRate exchangeRate	= null;
		if (parsableExchangeRate != null) {
			exchangeRate	= ExchangeRate.parse(parsableExchangeRate);
		}
		
		BigMoneyWithCurrencyAndFixedRate moneyWithFixedRate	= new BigMoneyWithCurrencyAndFixedRate(money, exchangeRate);
		return moneyWithFixedRate;
	}
	

	public BigMoney getMoney() {
		return money;
	}

	public void setMoney(BigMoney money) {
		this.money = money;
	}

	public ExchangeRate getRate() {
		return rate;
	}

	public void setRate(ExchangeRate rate) {
		this.rate = rate;
	}


}
