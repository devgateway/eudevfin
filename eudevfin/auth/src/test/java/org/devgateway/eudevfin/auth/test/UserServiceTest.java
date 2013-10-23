/**
 * 
 */
package org.devgateway.eudevfin.auth.test;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.auth.common.domain.User;
import org.devgateway.eudevfin.auth.common.service.UserService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author Alex
 *
 */
public class UserServiceTest {

	/**
	 * @throws java.lang.Exception
	 */
	private static ApplicationContext springContext;
	private static Logger logger = Logger.getLogger(UserServiceTest.class);
	
	@BeforeClass
	public static void setUpClass() throws Exception {
	//	System.setProperty("derby.system.home", "derby");
		springContext = new ClassPathXmlApplicationContext(
			"classpath:/META-INF/authContext.xml", "classpath:/META-INF/commonAuthContext.xml", "classpath:/META-INF/financialContext.xml");

	}


	@Test(expected = DataIntegrityViolationException.class)
	public void testCreateUser() {
		UserService service = springContext.getBean(UserService.class);
		
		User u=new User();
		u.setUsername("test");
		u.setPassword("testpassword");
		u.setEnabled(true);
		
		service.createUser(u);
		
		
		User userByUsername = service.getUserByUsername("test");
		
		Assert.assertNotNull(userByUsername);
		

	}
	
	
	
	
}