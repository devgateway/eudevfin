/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.financial.util;

import java.util.Locale;

import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FileWrapper;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.CurrencyMetadataService;
import org.devgateway.eudevfin.financial.translate.FinancialTransactionTranslation;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.joda.money.CurrencyUnit;

public final class FinancialTransactionUtil {

	/**
	 * Gets the {@link CurrencyUnit} based on the {@link Locale} using
	 * {@link Locale#getISO3Country()}
	 * 
	 * @param locale
	 * @return
	 */
	public static CurrencyUnit getCurrencyForCountryIso(String countryIso) {
		CurrencyUnit currencyUnit = null;
		if (countryIso != null)
			currencyUnit = CurrencyUnit.ofCountry(countryIso);
		return currencyUnit;
	}

	public static CurrencyUnit getCurrencyForCountryName(String countryName) {
		String iso2 = AuthUtils.countryNameIso2Map.get(countryName);
		if(iso2==null) return null;
		return getCurrencyForCountryIso(iso2);
	}

	/**
	 * @param locale
	 * @param currencyMetadataService
	 * @return a not null
	 *         {@link FinancialTransactionUtil#getCurrencyForLocale(Locale)} or
	 *         else the
	 *         {@link FinancialTransactionUtil#getDefaultCurrency(CurrencyMetadataService)}
	 */
	public static CurrencyUnit getCurrencyForCountryIsoOrDefault(String countryIso,
			CurrencyMetadataService currencyMetadataService) {
		CurrencyUnit currencyUnit = getCurrencyForCountryIso(countryIso);
		if (currencyUnit != null)
			return currencyUnit;
		else
			return getDefaultCurrency(currencyMetadataService);
	}
	
	/**
	 * Prepares to clone a financial transaction
	 * @param source the source entity
	 * @return the same entity, detached (null IDs)
	 */
	public static CustomFinancialTransaction prepareClonedTransaction(CustomFinancialTransaction source) {
		source.setId(null);
		source.setReportingYear(null);
		source.setDraft(true);
		source.setApproved(false);
		
		for (FinancialTransactionTranslation financialTransactionTranslation : source.getTranslations().values()) 
			financialTransactionTranslation.setId(null);
		
		for (FileWrapper fileWrapper : source.getUploadDocumentation()) {
			fileWrapper.setId(null);
			if(fileWrapper.getContent()!=null) fileWrapper.getContent().setId(null);
		}
		
		return source;
	}
	

	/**
	 * Initialize a previously newly created transaction
	 * 
	 * @param transaction
	 *            the previously created transaction
	 * @param currencyMetadataService
	 * @param organization
	 */
	public static void initializeFinancialTransaction(FinancialTransaction transaction,
			CurrencyMetadataService currencyMetadataService, Organization organization, String countryIso) {

		transaction.setCurrency(getCurrencyForCountryIsoOrDefault(countryIso, currencyMetadataService));
		transaction.setExtendingAgency(organization);
	}

	/**
	 * @param currencyMetadataService
	 * @return
	 */
	public static CurrencyUnit getDefaultCurrency(CurrencyMetadataService currencyMetadataService) {
		NullableWrapper<CurrencyUnit> defaultCurrencyUnitNW = currencyMetadataService
				.findByCode(CurrencyConstants.DEFAULT_CURRENCY_CODE_REQ);
		if (defaultCurrencyUnitNW.isNull())
			return CurrencyUnit.of("EUR");
		else
			return defaultCurrencyUnitNW.getEntity();
	}
}
