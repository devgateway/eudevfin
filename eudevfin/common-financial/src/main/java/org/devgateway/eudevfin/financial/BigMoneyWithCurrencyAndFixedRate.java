/**
 * 
 */
package org.devgateway.eudevfin.financial;

import org.joda.money.BigMoney;
import org.joda.money.ExchangeRate;

/**
 * @author Alex
 *
 */
public final class BigMoneyWithCurrencyAndFixedRate {
//	@Columns(columns = {@Column(name = "money_currency"), @Column(name = "money_amount")})
//    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentBigMoneyAmountAndCurrency")
    private BigMoney money;
	
//	@Columns(columns={@Column(name="base_currency"),@Column(name="counter_currency"),@Column(name="rate")})	
//	@Type(type="org.jadira.usertype.exchangerate.joda.PersistentExchangeRate")
	private ExchangeRate rate;
	
	BigMoneyWithCurrencyAndFixedRate(BigMoney money, ExchangeRate rate) {
		super();
		this.money = money;
		this.rate = rate;
	}
	
	public static BigMoneyWithCurrencyAndFixedRate of (BigMoney money, ExchangeRate rate) {
		
		BigMoneyWithCurrencyAndFixedRate moneyWithFixedRate	= new BigMoneyWithCurrencyAndFixedRate(money, rate);
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
