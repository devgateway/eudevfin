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
