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
package org.devgateway.eudevfin.auth.liquibase;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.apache.commons.codec.digest.DigestUtils;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedAuthority;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.auth.dao.PersistedAuthorityDaoImplEndpoint;
import org.devgateway.eudevfin.auth.dao.PersistedUserDaoImplEndpoint;
import org.devgateway.eudevfin.auth.dao.PersistedUserGroupDaoImplEndpoint;
import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mihai
 * 
 */
public class PopulateAuthDbChange extends AbstractSpringCustomTaskChange {

	@Autowired
	private PersistedUserDaoImplEndpoint userDao;

	@Autowired
	private PersistedUserGroupDaoImplEndpoint groupDao;
	
	@Autowired
	private OrganizationDaoImpl organizationDao;

	@Autowired
	private PersistedAuthorityDaoImplEndpoint authorityDao;

	@Override
	@Transactional
	public void execute(Database database) throws CustomChangeException {
		PersistedAuthority authorityUser = new PersistedAuthority(
				AuthConstants.Roles.ROLE_USER);
		authorityUser = authorityDao.save(authorityUser).getEntity();

		PersistedAuthority authoritySuper = new PersistedAuthority(
				AuthConstants.Roles.ROLE_SUPERVISOR);
		authoritySuper = authorityDao.save(authoritySuper).getEntity();
		
		PersistedAuthority authorityLead = new PersistedAuthority(
				AuthConstants.Roles.ROLE_TEAMLEAD);
		authorityLead = authorityDao.save(authorityLead).getEntity();
		
		PersistedUserGroup defaultGroup = new PersistedUserGroup();
		defaultGroup.setName("The Default Test Group");
		defaultGroup.setOrganization(organizationDao.findOne(1L).getEntity());
		groupDao.save(defaultGroup);

		PersistedUser user = new PersistedUser();
		user.setUsername("admin");
		user.setPassword(DigestUtils.sha256Hex("admin"));
		user.getPersistedAuthorities().add(authoritySuper);
		user.getPersistedAuthorities().add(authorityUser);
		user.getPersistedAuthorities().add(authorityLead);
		user.setGroup(defaultGroup);

		userDao.save(user);

		PersistedUser user2 = new PersistedUser();
		user2.setUsername("user");
		user2.setPassword(DigestUtils.sha256Hex("user"));
		user2.getPersistedAuthorities().add(authorityUser);
		user2.setGroup(defaultGroup);

		userDao.save(user2);
	}

	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationErrors validate(Database database) {
		// TODO Auto-generated method stub
		return null;
	}

}
