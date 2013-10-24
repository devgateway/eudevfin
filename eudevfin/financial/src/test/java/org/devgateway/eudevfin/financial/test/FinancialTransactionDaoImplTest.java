package org.devgateway.eudevfin.financial.test;

import java.math.BigDecimal;
import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FinancialTransactionDaoImplTest {

	private static ApplicationContext springContext;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		springContext = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/financialContext.xml");
	}

	@Test
	public void testLoadAllFinancialTransactions() {
		FinancialTransactionDaoImpl dao 	= springContext.getBean(FinancialTransactionDaoImpl.class);
		List<FinancialTransaction> allTx	= dao.loadAllFinancialTransactions();
		Assert.assertNotNull(allTx);
	}
	@Test
	public void testSaveFinancialTransaction() {
		FinancialTransactionDaoImpl dao 	= springContext.getBean(FinancialTransactionDaoImpl.class);
		Organization o			= new Organization();
		o.setName("Testing auditing Org");
		FinancialTransaction ft = new FinancialTransaction();
		ft.setAmount(new BigDecimal(123));
		ft.setDescription("Auditing test descr");
		ft.setSourceOrganization(o);
		ft	= dao.saveFinancialTransaction(ft);
		Assert.assertNotNull(ft);
	}

}
