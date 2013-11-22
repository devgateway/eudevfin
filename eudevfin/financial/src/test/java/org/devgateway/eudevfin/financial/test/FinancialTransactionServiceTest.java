/**
 * 
 */
package org.devgateway.eudevfin.financial.test;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Alex
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml" })
@Component 
public class FinancialTransactionServiceTest {

	@Autowired
	FinancialTransactionService service;

	/**
	 * @throws java.lang.Exception
	 */

	private static Logger logger = Logger
			.getLogger(FinancialTransactionServiceTest.class);

	@Test(expected = DataIntegrityViolationException.class)
	public void testCreateFinancialTransaction() {
		for (int i = 1; i <= 10; i++) {
			FinancialTransaction tx = new FinancialTransaction();
			tx.setAmount(new BigDecimal(i * 100));
			tx.setDescription("This is transaction " + i);

			logger.info(tx);
			FinancialTransaction result = service
					.createFinancialTransaction(tx);

			// Assert.assertNotNull(result.getId() );
			logger.info(result);

		}
		List<FinancialTransaction> list = service.getAllFinancialTransactions();
		logger.info("Number of txs" + list.size());
	}

	@Test
	public void testGetFinancialTransactions() {


		List<FinancialTransaction> list = service.getAllFinancialTransactions();
		Assert.assertNotNull(list);
		logger.info("Number of txs" + list.size());
	}

	@Ignore
	@Test
	public void testCreateFinancialTransactionWithOrganization() {
		for (int i = 1; i <= 10; i++) {
			Organization org = new Organization();
			org.setName("Org name " + i);
			FinancialTransaction tx = new FinancialTransaction();
			tx.setAmount(new BigDecimal(i * 100));
			tx.setDescription("This is transaction " + i);
			tx.setReportingOrganization(org);

			logger.info(tx);
			FinancialTransaction result = service
					.createFinancialTransaction(tx);

			Assert.assertNotNull(result.getId());
			logger.info(result);

		}
		List<FinancialTransaction> list = service.getAllFinancialTransactions();
		for (FinancialTransaction financialTransaction : list) {
			Assert.assertNotNull( financialTransaction.getReportingOrganization() );
		}

	}

}