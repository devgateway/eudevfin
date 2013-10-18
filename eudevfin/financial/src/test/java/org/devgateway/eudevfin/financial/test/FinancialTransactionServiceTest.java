/**
 * 
 */
package org.devgateway.eudevfin.financial.test;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Alex
 *
 */
public class FinancialTransactionServiceTest {

	/**
	 * @throws java.lang.Exception
	 */
	private static ApplicationContext springContext;
	private static Logger logger = Logger.getLogger(FinancialTransactionServiceTest.class);
	
	@BeforeClass
	public static void setUpClass() throws Exception {
	//	System.setProperty("derby.system.home", "derby");
		springContext = new ClassPathXmlApplicationContext(
			"classpath:/META-INF/financialContext.xml");

	}


	@Test
	public void testCreateFinancialTransaction() {
		FinancialTransactionService service = springContext.getBean(FinancialTransactionService.class);
		for (int i=1; i<=10; i++) {
			FinancialTransaction tx	 = new FinancialTransaction();
			tx.setAmount(new BigDecimal(i*100) );
			tx.setDescription("This is transaction " + i);
			
			logger.info(tx);
			FinancialTransaction result = service.createFinancialTransaction(tx);
			
			Assert.assertNotNull(result.getId() );
			logger.info(result);
			
			List<FinancialTransaction> list = service.getAllFinancialTransactions();
			logger.info("Number of txs" +  list.size());
			
		}
	}
	
	@Test
	public void testGetFinancialTransactions() {
		FinancialTransactionService service = springContext.getBean(FinancialTransactionService.class);
		
//		for (int i=1; i<=10; i) {
//			FinancialTransaction tx	 = new FinancialTransaction();
//			tx.setAmount(new BigDecimal(i*100) );
//			tx.setDescription("This is transaction "  i);
//			
//			FinancialTransaction result = service.createFinancialTransaction(tx);
//			Assert.assertNotNull(result.getId() );
//			
//		}
		
		List<FinancialTransaction> list = service.getAllFinancialTransactions();
		Assert.assertNotNull(list);
		logger.info("Number of txs" + list.size());
	}

}