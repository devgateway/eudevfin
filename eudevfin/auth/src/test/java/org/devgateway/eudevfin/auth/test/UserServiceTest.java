/**
 * 
 */
package org.devgateway.eudevfin.auth.test;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.auth.common.domain.User;
import org.devgateway.eudevfin.auth.common.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author mihai
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/authContext.xml",
		"classpath:/META-INF/commonAuthContext.xml",
		"classpath:/META-INF/financialContext.xml" })
public class UserServiceTest {

	@Autowired
	UserService service;

	private static Logger logger = Logger.getLogger(UserServiceTest.class);

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