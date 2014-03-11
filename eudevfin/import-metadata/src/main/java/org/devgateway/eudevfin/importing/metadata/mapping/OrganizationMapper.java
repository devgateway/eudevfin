/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.mapping;

import java.util.List;

import liquibase.exception.SetupException;

import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.service.OrganizationService;
import org.devgateway.eudevfin.importing.metadata.exception.EntityMapperGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
