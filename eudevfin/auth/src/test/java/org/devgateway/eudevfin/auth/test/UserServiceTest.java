/**
 * 
 */
package org.devgateway.eudevfin.auth.test;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author mihai
 * 
 */
public class UserServiceTest extends AbstractAuthTest {

	@Autowired
	UserService service;

	@Test
	public void testCreateGetUser() {
		PersistedUser u = new PersistedUser();
		u.setUsername("test");
		u.setPassword("testpassword");
		u.setEnabled(true);

		service.createUser(u);

		PersistedUser userByUsername = service.getUserByUsername("test");

		Assert.assertNotNull(userByUsername);

	}

}