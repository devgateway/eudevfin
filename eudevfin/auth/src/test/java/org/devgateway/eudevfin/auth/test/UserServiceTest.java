/**
 * 
 */
package org.devgateway.eudevfin.auth.test;

import org.devgateway.eudevfin.auth.common.domain.User;
import org.devgateway.eudevfin.auth.common.service.UserService;
import org.devgateway.eudevfin.auth.common.test.AbstractAuthTest;
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
		User u = new User();
		u.setUsername("test");
		u.setPassword("testpassword");
		u.setEnabled(true);

		service.createUser(u);

		User userByUsername = service.getUserByUsername("test");

		Assert.assertNotNull(userByUsername);

	}

}