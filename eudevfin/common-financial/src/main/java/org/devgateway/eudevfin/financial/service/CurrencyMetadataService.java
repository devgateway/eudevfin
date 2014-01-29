/**
 * 
 */
package org.devgateway.eudevfin.financial.service;

import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.joda.money.CurrencyUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 *
 */
public interface CurrencyMetadataService {
	public Page<CurrencyUnit> findBySearch(String term, @Header("pageable")Pageable pageable) ;
	
	public NullableWrapper<CurrencyUnit> findByCode(String code) ;
		
}
