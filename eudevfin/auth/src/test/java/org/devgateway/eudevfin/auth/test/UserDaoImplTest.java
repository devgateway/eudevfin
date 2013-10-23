package org.devgateway.eudevfin.auth.test;

import java.math.BigDecimal;
import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.service.Organization;
import org.junit.Assert;
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
	public void testLoadAllFinancialTransactions() {
	}
	@Test
	public void testSaveFinancialTransaction() {

	}

}
