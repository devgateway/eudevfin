/**
 * 
 */
package org.devgateway.eudevfin.financial.test.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.financial.service.OrganizationService;
import org.devgateway.eudevfin.financial.util.LocaleHelperInterface;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
		"classpath:META-INF/commonFinancialContext.xml","classpath:testFinancialContext.xml" })
@Component 
public class FinancialTransactionServiceTest {

	@Autowired
	FinancialTransactionService service;
	
	@Autowired
	OrganizationService orgService;
	
	@Autowired
	@Qualifier("localeHelperRequest")
	LocaleHelperInterface localeHelper;
	
	
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
					.save(tx).getEntity();

			// Assert.assertNotNull(result.getId() );
			logger.info(result);

		}
		List<FinancialTransaction> list = service.findAll();
		logger.info("Number of txs" + list.size());
	}

	@Test
	public void testFindAll() {


		List<FinancialTransaction> list = service.findAll();
		Assert.assertNotNull(list);
		logger.info("Number of txs" + list.size());
	}

	@Test
	public void testSave() {
		FinancialTransaction tx	= this.createTransaction();
		tx	= service.save(tx).getEntity();
		Assert.assertNotNull(tx.getId());
		Assert.assertNotNull(tx.getCreatedBy());
		Assert.assertNotNull(tx.getCreatedDate());
		Assert.assertNotNull(tx.getModifiedBy());
		Assert.assertNotNull(tx.getModfiedDate());
		
	}
	
	@Test
	public void testFindById() {
		FinancialTransaction tx	= this.createTransaction();
		tx	= service.save(tx).getEntity();
		
		FinancialTransaction result	= service.findById(tx.getId() ).getEntity();
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getCommitmentDate());
	}
	
	private FinancialTransaction createTransaction() {
		Organization org = new Organization();
		org.setName("Org name for tx service testing");
		org 	= orgService.save(org).getEntity();
		
		FinancialTransaction tx = new FinancialTransaction();
		tx.setAmountsReceived(BigMoney.parse("EUR 1230"));
		tx.setCommitments(BigMoney.parse("EUR 4320"));
		tx.setDescription("Some description for this tx");
		tx.setCommitmentDate(new LocalDateTime());
		tx.setReportingOrganization(org);

		logger.info(tx);
	
		return tx;
	}

}