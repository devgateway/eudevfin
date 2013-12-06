/**
 * 
 */
package org.devgateway.eudevfin.common.liquibase;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.common.spring.ContextHelper;

/**
 * @author Alex,Mihai
 * 
 */
public abstract class AbstractSpringCustomTaskChange implements
		CustomTaskChange {

	ContextHelper ctxHelper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see liquibase.change.custom.CustomChange#getConfirmationMessage()
	 */
	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * liquibase.change.custom.CustomChange#setFileOpener(liquibase.resource
	 * .ResourceAccessor)
	 */
	@Override
	public void setFileOpener(ResourceAccessor arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see liquibase.change.custom.CustomChange#setUp()
	 */
	@Override
	public void setUp() throws SetupException {
		this.ctxHelper = ContextHelper.newInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * liquibase.change.custom.CustomChange#validate(liquibase.database.Database
	 * )
	 */
	@Override
	public ValidationErrors validate(Database arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * liquibase.change.custom.CustomTaskChange#execute(liquibase.database.Database
	 * )
	 */
	@Override
	public void execute(Database arg0) throws CustomChangeException {
		this.ctxHelper.getBean(getPopulator()).populate();

	}

	/**
	 * Implement this to provide a class file for the {@link MockupDbPopulator}
	 * 
	 * @return a subclass of {@link MockupDbPopulator}
	 */
	public abstract Class<? extends MockupDbPopulator> getPopulator();

}
