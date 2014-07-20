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
package org.devgateway.eudevfin.financial.dao;

import java.util.ArrayList;
import java.util.List;

import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.financial.config.CurrencyMetadataConfig;
import org.devgateway.eudevfin.financial.exception.InitializingCurrenciesException;
import org.devgateway.eudevfin.financial.util.CurrencyConstants;
import org.joda.money.CurrencyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
@Lazy(value=false)
public class CurrenciesMetadataDao {
	
	
	@Autowired
	private CurrencyMetadataConfig currencyMetadataConfig;
	
	@ServiceActivator(inputChannel="findByCodeCurrencyChannel")
	public NullableWrapper<CurrencyUnit> findByCode(String code) {
		CurrencyUnit currencyUnit	= null;
		if ( CurrencyConstants.DEFAULT_CURRENCY_CODE_REQ.equals(code) ){
			currencyUnit	= this.currencyMetadataConfig.getDefaultCurrencyUnit();
		}
		else {
			currencyUnit		= this.searchByCodeInList(code, 
						currencyMetadataConfig.getNationalCurrencyUnitList());
			if ( currencyUnit == null ) {
				currencyUnit	= this.searchByCodeInList(code, 
						currencyMetadataConfig.getOtherCurrencyUnitList());
			}
		}
		NullableWrapper<CurrencyUnit> result	= new NullableWrapper<CurrencyUnit>(currencyUnit);
		return result;
	}
	
	
	private CurrencyUnit searchByCodeInList(String code, List<CurrencyUnit> list) {
		
		CurrencyUnit currencyUnit	= null;
		if ( code != null && !code.isEmpty() && list != null) {
			for (CurrencyUnit temp : list) {
				if (code.equalsIgnoreCase(temp.getCode())) {
					currencyUnit	= temp;
					break;
				}
			}
		}
		return currencyUnit;
	}

	@ServiceActivator(inputChannel="findBySearchPageableCurrencyChannel")
	public Page<CurrencyUnit> findBySearch(String term, @Header("pageable")Pageable pageable, @Header("type") String type) {
		List<CurrencyUnit> resultList	= new ArrayList<CurrencyUnit>();
		int numOfResults				= 0;
		List<CurrencyUnit> sourceList	= this.decideOnSrcList(type);
		
		numOfResults = findCurrenciesInCollection(term, pageable, resultList, 
				sourceList, numOfResults );
		
		PageImpl<CurrencyUnit> result	= new PageImpl<CurrencyUnit>(resultList,pageable, numOfResults);
		return result;
	}

	private List<CurrencyUnit> decideOnSrcList(String type) {
		List<CurrencyUnit> retList	= null;
		if ( CurrencyConstants.NATIONAL_CURRENCIES_LIST.equals(type) )
			retList	=  currencyMetadataConfig.getNationalCurrencyUnitList();
		else if ( CurrencyConstants.OTHER_CURRENCIES_LIST.equals(type) ) {
			retList	= currencyMetadataConfig.getOtherCurrencyUnitList();
		}
		else if ( CurrencyConstants.ALL_CURRENCIES_LIST.equals(type) ){
			retList	= currencyMetadataConfig.getAllCurrencyUnitList();
		}
		else
			throw new InitializingCurrenciesException("There's no currency list of type: " + type);
		return retList;
	}


	private int findCurrenciesInCollection(String term, Pageable pageable,
			List<CurrencyUnit> resultList, List<CurrencyUnit> currencyUnitList,
			int numOfResults) {
		int pageSize = pageable.getPageSize();
		int pageNo = pageable.getPageNumber();
		for (CurrencyUnit c : currencyUnitList) {
			boolean termEmpty			= term == null || term.isEmpty();
			boolean currContainsTerm 	= termEmpty || (c.getCode().toLowerCase()
					.contains(term.toLowerCase()) );
			if (currContainsTerm) {
				numOfResults++;
				boolean inpage = numOfResults > pageNo * pageSize
						&& numOfResults <= (pageNo+1) * pageSize;
				if (inpage) {
					resultList.add(c);
				}
			}
		}
		return numOfResults;
	}
	
	

}
