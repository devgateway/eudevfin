/**
 * 
 */
package org.devgateway.eudevfin.financial.service;

import java.io.Serializable;

import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.joda.money.CurrencyUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 *
 */
public interface CurrencyMetadataService extends Serializable{
	public Page<CurrencyUnit> findBySearch(String term, @Header("pageable")Pageable pageable, @Header("type") String type);
	
	public NullableWrapper<CurrencyUnit> findByCode(String code) ;
		
}
