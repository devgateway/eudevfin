/**
 * 
 */
package org.devgateway.eudevfin.financial.liquibase;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.financial.util.ContextHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Alex
 *
 */
public class PopulateDbChange implements CustomTaskChange {
	
	ContextHelper ctxHelper;
	
	/* (non-Javadoc)
	 * @see liquibase.change.custom.CustomChange#getConfirmationMessage()
	 */
	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see liquibase.change.custom.CustomChange#setFileOpener(liquibase.resource.ResourceAccessor)
	 */
	@Override
	public void setFileOpener(ResourceAccessor arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see liquibase.change.custom.CustomChange#setUp()
	 */
	@Override
	public void setUp() throws SetupException {
		this.ctxHelper	= ContextHelper.newInstance();
	}

	/* (non-Javadoc)
	 * @see liquibase.change.custom.CustomChange#validate(liquibase.database.Database)
	 */
	@Override
	public ValidationErrors validate(Database arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see liquibase.change.custom.CustomTaskChange#execute(liquibase.database.Database)
	 */
	@Override
	public void execute(Database arg0) throws CustomChangeException {
		this.ctxHelper.getBean(PopulateDb.class).populate();

	}

}
