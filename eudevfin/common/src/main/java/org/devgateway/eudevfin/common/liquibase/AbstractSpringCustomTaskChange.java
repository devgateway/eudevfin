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
package org.devgateway.eudevfin.common.liquibase;

import liquibase.change.custom.CustomChangeWrapper;
import liquibase.change.custom.CustomTaskChange;
import liquibase.exception.SetupException;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.spring.ContextHelper;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author Alex,Mihai 
 * Abstract class linking {@link CustomTaskChange} with
 *         Spring so that access to spring beans is possible (for example for
 *         JPA persistence).
 * Do not use {@link Component} in subclasses, this is not instantiated by Spring         
 */
public abstract class AbstractSpringCustomTaskChange implements
		CustomTaskChange{

	protected static Logger logger = Logger.getLogger(AbstractSpringCustomTaskChange.class);
	
	/**
	 * 
	 * {@link CustomTaskChange} has bean was created by non-spring managed Liquibase in
	 * {@link CustomChangeWrapper#setClass(String)} Therefore it is clueless
	 * about the spring infrastructure, so we autowire its fields manually
	 * 
	 * @see liquibase.change.custom.CustomChange#setUp()
	 */
	@Override
	public void setUp() throws SetupException {
		AutowireCapableBeanFactory factory = ContextHelper.newInstance()
				.getAutowireCapableBeanFactory();
		logger.debug("Autowiring properties of bean "+this.getClass()+" before execute.");
		factory.autowireBean(this);
	}




}
