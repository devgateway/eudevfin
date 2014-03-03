/**
 * 
 */
package org.devgateway.eudevfin.exchange.common.service;

import java.math.BigDecimal;

import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 * 
 *         This should be just a wrapper over whatever the actual logic for translating BigMoney to another currency
 */
@Component
public class ExchangeRateUtil {

	@Autowired
	private HistoricalExchangeRateService historicalExchangeRateService;

	public BigMoney exchange(final BigMoney srcMoney, final CurrencyUnit toCU, final BigDecimal fixedRate,
			final LocalDateTime date) {
		BigMoney finalMoney = srcMoney;

		if (toCU != null && !srcMoney.getCurrencyUnit().equals(toCU)) {
			if (fixedRate != null) {
				finalMoney = srcMoney.convertedTo(toCU, fixedRate);
			} else {
				if (date != null) {
					final Iterable<HistoricalExchangeRate> iterRates = this.historicalExchangeRateService.findRatesForDate(date);
					if (iterRates != null) {
						for (final HistoricalExchangeRate historicalExchangeRate : iterRates) {
							final CurrencyUnit baseCU = historicalExchangeRate.getRate().getBase();
							final CurrencyUnit counterCU = historicalExchangeRate.getRate().getCounter();
							if (baseCU.equals(srcMoney.getCurrencyUnit()) && counterCU.equals(toCU)) {
								finalMoney = historicalExchangeRate.getRate().operations().exchange(srcMoney);
								break;
							}
						}
					}
				}
			}
		}
		return finalMoney;
	}

}
