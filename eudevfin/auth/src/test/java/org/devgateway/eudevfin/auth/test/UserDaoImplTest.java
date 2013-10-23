package org.devgateway.eudevfin.auth.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDaoImplTest {

	private static ApplicationContext springContext;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		springContext = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/authContext.xml");
	}

	@Test
	public void testCreateUser() {
	}
	@Test
	public void testSaveFinancialTransaction() {

	}

}
