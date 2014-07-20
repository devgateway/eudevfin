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
package org.devgateway.eudevfin.common.service;

import java.io.Serializable;
import java.util.List;

import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.Payload;

/**
 * @author Alex
 */
public interface BaseEntityService<T> extends Serializable {
	NullableWrapper<T> save(T t);
	
	void delete(T t);
	
	@Payload("new java.util.Date()")
	List<T> findAll();
	
	NullableWrapper<T> findOne(Long id);
	
	public  Page<T> findByGeneralSearchPageable(String searchString,
			@Header(value="locale",required=false) String locale, @Header("pageable") Pageable pageable);
	

	/**
	 * @deprecated do we really need an unpaginated search by searchstring and locale?
	 * @param locale
	 * @param searchString
	 * @return
	 */
	public List<T> findByGeneralSearch(@Header("locale")String locale, String searchString);
	
	
}
