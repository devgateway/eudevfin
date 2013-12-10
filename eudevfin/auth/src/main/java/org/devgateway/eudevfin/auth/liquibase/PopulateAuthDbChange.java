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
import org.devgateway.eudevfin.auth.repository.PersistedAuthorityRepository;
import org.devgateway.eudevfin.auth.repository.PersistedUserGroupRepository;
import org.devgateway.eudevfin.auth.repository.PersistedUserRepository;
import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mihai
 * 
 */
public class PopulateAuthDbChange extends AbstractSpringCustomTaskChange {

	@Autowired
	private PersistedUserRepository userRepo;

	@Autowired
	private PersistedUserGroupRepository groupRepo;

	@Autowired
	private PersistedAuthorityRepository authorityRepo;

	@Override
	@Transactional
	public void execute(Database database) throws CustomChangeException {
		PersistedAuthority authorityUser = new PersistedAuthority(
				AuthConstants.Roles.ROLE_USER);
		authorityUser = authorityRepo.save(authorityUser);

		PersistedAuthority authoritySuper = new PersistedAuthority(
				AuthConstants.Roles.ROLE_SUPERVISOR);
		authoritySuper = authorityRepo.save(authoritySuper);

		PersistedUserGroup defaultGroup = new PersistedUserGroup();
		defaultGroup.setName("The Default Test Group");
		defaultGroup = groupRepo.save(defaultGroup);

		PersistedUser user = new PersistedUser();
		user.setUsername("admin");
		user.setPassword(DigestUtils.sha256Hex("admin"));
		user.getAuthorites().add(authoritySuper);
		user.getAuthorites().add(authorityUser);
		user.getGroups().add(defaultGroup);

		userRepo.save(user);

		PersistedUser user2 = new PersistedUser();
		user2.setUsername("user");
		user2.setPassword(DigestUtils.sha256Hex("user"));
		user2.getAuthorites().add(authorityUser);
		user2.getGroups().add(defaultGroup);

		userRepo.save(user2);
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
