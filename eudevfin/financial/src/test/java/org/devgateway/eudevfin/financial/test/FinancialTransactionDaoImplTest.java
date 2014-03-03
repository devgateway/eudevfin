package org.devgateway.eudevfin.financial.test;

import java.math.BigDecimal;
import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testFinancialContext.xml" })
@TransactionConfiguration(defaultRollback=false, transactionManager="transactionManager")
public class FinancialTransactionDaoImplTest {

	@Autowired
	FinancialTransactionDaoImpl txDao;
	
	@Autowired
	OrganizationDaoImpl orgDao;
	
	@Test
	public void testLoadAllFinancialTransactions() {
		List<FinancialTransaction> allTx	= txDao.findAllAsList();
		Assert.assertNotNull(allTx);
	}
	@Test
	public void testSaveFinancialTransaction() {
		Organization o			= new Organization();
		o.setName("Testing auditing Org");
		orgDao.save(o);
		FinancialTransaction ft = new FinancialTransaction();
		ft.setAmount(new BigDecimal(123));
		ft.setDescription("Auditing test descr");
		ft.setExtendingAgency(o);
		ft	= txDao.save(ft).getEntity();
		Assert.assertNotNull(ft);
	}

}
