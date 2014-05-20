/**
 * 
 */
package org.devgateway.eudevfin.auth.test;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.PersistedUserGroupService;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author mihai
 * 
 */
@Transactional
@TransactionConfiguration(transactionManager="transactionManager")
public class UserServiceTest extends AbstractAuthTest {

	@Autowired
	PersistedUserService userService;
	
	@Autowired
	PersistedUserGroupService groupService;
	
	@Test
	public void testCreateGetUser() {
		PersistedUser u = new PersistedUser();
		u.setUsername("test");
		u.setPassword("testpassword");
		u.setEnabled(true);

		userService.save(u);

		PersistedUser userByUsername = userService.findByUsername("test").getEntity();

		Assert.assertNotNull(userByUsername);

	}

}