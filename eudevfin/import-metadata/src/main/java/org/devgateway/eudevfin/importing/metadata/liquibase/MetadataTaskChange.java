/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.liquibase;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.devgateway.eudevfin.importing.metadata.MetadataImporterEngine;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Alex
 *
 */
public class MetadataTaskChange extends AbstractSpringCustomTaskChange {
	
	@Autowired
	MetadataImporterEngine engine;

	/* (non-Javadoc)
	 * @see liquibase.change.custom.CustomTaskChange#execute(liquibase.database.Database)
	 */
	@Override
	public void execute(final Database database) throws CustomChangeException {
//		this.engine.process();

	}

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
	public void setFileOpener(final ResourceAccessor resourceAccessor) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see liquibase.change.custom.CustomChange#validate(liquibase.database.Database)
	 */
	@Override
	public ValidationErrors validate(final Database database) {
		// TODO Auto-generated method stub
		return null;
	}

}
