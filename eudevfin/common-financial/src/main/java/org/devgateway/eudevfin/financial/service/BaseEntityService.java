/**
 * 
 */
package org.devgateway.eudevfin.financial.service;

import java.util.List;

import org.springframework.integration.annotation.Payload;

/**
 * @author Alex
 *
 */
public interface BaseEntityService<T> {
	T save(T t);
	
	@Payload("new java.util.Date()")
	List<T> findAll();
	
	T findById(Long id);
}
