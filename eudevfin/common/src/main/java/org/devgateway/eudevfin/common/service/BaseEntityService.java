/**
 * 
 */
package org.devgateway.eudevfin.common.service;

import java.util.List;

import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.Payload;

/**
 * @author Alex
 */
public interface BaseEntityService<T> {
	T save(T t);
	
	@Payload("new java.util.Date()")
	List<T> findAll();
	
	T findById(Long id);
	
	public PagingHelper<T> findByGeneralSearch(String searchString, 
			@Header("pageNumber")int pageNumber, @Header("pageSize")int pageSize);
}
