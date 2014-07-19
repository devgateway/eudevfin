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
package org.devgateway.eudevfin.importing.metadata.storing;

import org.devgateway.eudevfin.importing.metadata.mapping.MapperInterface;

/**
 * @author Alex
 *
 */
public interface IStoringEngine<T> {
	
	boolean process(T entityFromFile);
	
	
	/**
	 * 
	 * @param entityFromFile the entity created by the MapperInterface implementation from file
	 * @return the equivalent entity from the database or null if it doesn't exist
	 */
	T findEquivalentEntity(T entityFromFile);
	boolean checkSame(T entityFromFile, T entityFromDb);
	
	void save(T entityFromFile);
	
	int importHashcode(T entity);
	
	Class<? extends MapperInterface> getRelatedMapper();
}
