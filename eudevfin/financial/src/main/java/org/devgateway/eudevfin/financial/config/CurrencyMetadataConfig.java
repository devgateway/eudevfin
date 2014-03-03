/**
 * 
 */
package org.devgateway.eudevfin.financial.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.devgateway.eudevfin.financial.exception.InitializingCurrenciesException;
import org.joda.money.CurrencyUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
public class CurrencyMetadataConfig {
	@Value("#{currencyProperties['currencies.national']}")
	private String nationalCurrencies;
	
	@Value("#{currencyProperties['currencies.other']}")
	private String otherCurrencies;
	
	@Value("#{currencyProperties['currencies.default']}")
	private String defaultCurrency;
	
	private List<String> nationalCurrenciesList;
	private List<String> otherCurrenciesList;
	private List<String> allCurrenciesList;
	
	private List<CurrencyUnit> nationalCurrencyUnitList;
	private List<CurrencyUnit> otherCurrencyUnitList;
	private List<CurrencyUnit> allCurrencyUnitList;
	private CurrencyUnit defaultCurrencyUnit;

	@PostConstruct
	private void init() {
		
		this.defaultCurrencyUnit		= 
				CurrencyUnit.of(this.defaultCurrency);
		
		this.nationalCurrenciesList		= 
				this.convertStringToUnmodifiableList(this.nationalCurrencies);
		
		this.otherCurrenciesList		= 
				this.convertStringToUnmodifiableList(this.otherCurrencies);
		
		List<String> allCurrencies		= new ArrayList<String>(
				this.nationalCurrenciesList.size() + this.otherCurrenciesList.size());
		allCurrencies.addAll(this.nationalCurrenciesList);
		allCurrencies.addAll(this.otherCurrenciesList);
		this.allCurrenciesList			= Collections.unmodifiableList(allCurrencies);
		
		this.nationalCurrencyUnitList	= 
				this.convertStringToUnmodifiableList(nationalCurrenciesList);
		this.otherCurrencyUnitList		= 
				this.convertStringToUnmodifiableList(otherCurrenciesList);
		this.allCurrencyUnitList = 
				this.convertStringToUnmodifiableList(allCurrenciesList);
		
		
	}
	
	private List<String> convertStringToUnmodifiableList(String source) {
		String [] currArray		= source.split(",");
		if ( currArray != null && currArray.length > 0 ) {
			List<String> tempList	= new ArrayList<String>();
			for (String currString : currArray) {
				if ( currString != null && currString.length() > 0 ) {
					tempList.add(currString.trim());
				}
				else
					throw new InitializingCurrenciesException("A currency code is empty");
			}
			return Collections.unmodifiableList(tempList);
		}
		throw new InitializingCurrenciesException("Currency list from properties file is empty");
	}
	
	private List<CurrencyUnit> convertStringToUnmodifiableList(List<String> currenciesList) {
		List<CurrencyUnit> tempList	= new ArrayList<CurrencyUnit>();
		for (String string : currenciesList) {
			tempList.add(CurrencyUnit.of(string));
		}
		return Collections.unmodifiableList(tempList);
	}

	public List<String> getNationalCurrenciesList() {
		return nationalCurrenciesList;
	}

	public List<String> getOtherCurrenciesList() {
		return otherCurrenciesList;
	}

	public List<CurrencyUnit> getNationalCurrencyUnitList() {
		return nationalCurrencyUnitList;
	}

	public List<CurrencyUnit> getOtherCurrencyUnitList() {
		return otherCurrencyUnitList;
	}

	public CurrencyUnit getDefaultCurrencyUnit() {
		return defaultCurrencyUnit;
	}

	public List<String> getAllCurrenciesList() {
		return allCurrenciesList;
	}

	public List<CurrencyUnit> getAllCurrencyUnitList() {
		return allCurrencyUnitList;
	}
	
}
