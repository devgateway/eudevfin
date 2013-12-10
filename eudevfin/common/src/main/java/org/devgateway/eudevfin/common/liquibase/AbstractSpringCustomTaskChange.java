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

/**
 * @author Alex,Mihai
 * 
 */
public abstract class AbstractSpringCustomTaskChange implements
		CustomTaskChange{

	protected static Logger logger = Logger.getLogger(AbstractSpringCustomTaskChange.class);
	
	/**
	 * 
	 * This bean was created by non-spring managed Liquibase in
	 * {@link CustomChangeWrapper#setClass(String)} Therefore it is clueless
	 * about the spring infrastructure, so we autowire its fields manually
	 * 
	 * @see liquibase.change.custom.CustomChange#setUp()
	 */
	@Override
	public void setUp() throws SetupException {
		AutowireCapableBeanFactory factory = ContextHelper.newInstance()
				.getAutowireCapableBeanFactory();
		logger.info("Autowiring properties of bean "+this.getClass()+" before execute");
		factory.autowireBean(this);
	}




}
