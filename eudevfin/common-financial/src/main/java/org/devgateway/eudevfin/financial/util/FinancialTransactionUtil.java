package org.devgateway.eudevfin.financial.util;

import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.service.CurrencyMetadataService;
import org.joda.money.CurrencyUnit;

public final class FinancialTransactionUtil {

	
	/**
	 * Initialize a previously newly created transaction
	 * @param transaction the previously created trnasation 
	 * @param currencyMetadaService
	 * @param organization
	 */
	public static void initializeFinancialTransaction(FinancialTransaction transaction, CurrencyMetadataService currencyMetadaService, Organization organization) {
		CurrencyUnit defaultCurrency = getDefaultCurrency(currencyMetadaService);
		transaction.setCurrency(defaultCurrency);
		transaction.setExtendingAgency(organization);
	}
	
	/**
	 * @param currencyMetadataService
	 * @return
	 */
	public static CurrencyUnit getDefaultCurrency(CurrencyMetadataService currencyMetadataService) {
		NullableWrapper<CurrencyUnit> defaultCurrencyUnitNW	= 
				currencyMetadataService.findByCode(CurrencyConstants.DEFAULT_CURRENCY_CODE_REQ);
		if ( defaultCurrencyUnitNW.isNull() )
			return CurrencyUnit.of("EUR");
		else
			return defaultCurrencyUnitNW.getEntity();		
	}
}
