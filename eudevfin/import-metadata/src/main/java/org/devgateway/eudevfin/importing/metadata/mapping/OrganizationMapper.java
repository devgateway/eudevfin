/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.mapping;

import liquibase.exception.SetupException;

import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.importing.metadata.exception.EntityMapperGenerationException;

/**
 * @author Alex
 *
 */
public class OrganizationMapper extends AbstractMapper<Organization> {
	
	public OrganizationMapper() {
		super();
		try {
			this.setUp();
		} catch (SetupException e) {
			throw new EntityMapperGenerationException("Problem with autowiring", e);
		}
	}

	@Override
	protected Organization instantiate() {
		return  new Organization();
	}
	

}
