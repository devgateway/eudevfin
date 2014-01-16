/**
 * 
 */
package org.devgateway.eudevfin.common.service;

import java.util.List;

import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.Payload;

/**
 * @author Alex
 */
public interface BaseEntityService<T> {
	NullableWrapper<T> save(T t);
	
	@Payload("new java.util.Date()")
	List<T> findAll();
	
	NullableWrapper<T> findOne(Long id);
	
	public  Page<T> findByGeneralSearchPageable(String searchString,
			@Header(value="locale",required=false) String locale, @Header("pageable") Pageable pageable);
	
	public List<T> findByGeneralSearch(@Header("locale")String locale, String searchString);
	
	
}
