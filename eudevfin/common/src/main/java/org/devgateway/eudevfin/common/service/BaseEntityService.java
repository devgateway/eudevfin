/**
 * 
 */
package org.devgateway.eudevfin.common.service;

import java.util.List;

import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
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
	
	public PagingHelper<T> findByGeneralSearchPageable(String searchString, 
			@Header("pageNumber")int pageNumber, @Header("pageSize")int pageSize);
	
	public List<T> findByGeneralSearch(@Header("locale")String locale, String searchString);
}
