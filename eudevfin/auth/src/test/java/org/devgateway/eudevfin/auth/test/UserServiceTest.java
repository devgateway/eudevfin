/**
 * 
 */
package org.devgateway.eudevfin.auth.test;

import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.service.PersistedUserService;
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
	PersistedUserService service;

	@Test
	public void testCreateGetUser() {
		PersistedUser u = new PersistedUser();
		u.setUsername("test");
		u.setPassword("testpassword");
		u.setEnabled(true);

		service.save(u);

		PersistedUser userByUsername = service.findByUsername("test").getEntity();

		Assert.assertNotNull(userByUsername);

	}

}