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
package org.devgateway.eudevfin.importing.metadata.mapping;

import liquibase.exception.SetupException;

import org.devgateway.eudevfin.importing.metadata.exception.EntityMapperGenerationException;
import org.devgateway.eudevfin.metadata.common.domain.Organization;

/**
 * @author Alex
 *
 */
public class OrganizationMapper extends AbstractMapper<Organization> {
	
	public OrganizationMapper() {
		super();
		try {
			this.setUp();
		} catch (final SetupException e) {
			throw new EntityMapperGenerationException("Problem with autowiring", e);
		}
	}

	@Override
	protected Organization instantiate() {
		return  new Organization();
	}
	
}
