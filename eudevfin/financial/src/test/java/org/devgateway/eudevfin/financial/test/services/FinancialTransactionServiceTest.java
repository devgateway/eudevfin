/**
 * 
 */
package org.devgateway.eudevfin.financial.test.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.locale.LocaleHelperInterface;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.service.OrganizationService;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/financialContext.xml",
		"classpath:/META-INF/commonContext.xml", "classpath:/META-INF/commonMetadataContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testFinancialContext.xml" })
@Component 
@Transactional
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
	

	@Test
	public void testFindAll() {


		final List<FinancialTransaction> list = this.service.findAll();
		Assert.assertNotNull(list);
		logger.info("Number of txs" + list.size());
	}

	@Test
	public void testSave() {
		FinancialTransaction tx	= this.createTransaction();
		tx	= this.service.save(tx).getEntity();
		Assert.assertNotNull(tx.getId());
		Assert.assertNotNull(tx.getCreatedBy());
		Assert.assertNotNull(tx.getCreatedDate());
		Assert.assertNotNull(tx.getModifiedBy());
		Assert.assertNotNull(tx.getModfiedDate());
		
	}
	
	@Test
	public void testfindOne() {
		FinancialTransaction tx	= this.createTransaction();
		tx	= this.service.save(tx).getEntity();
		
		final FinancialTransaction result	= this.service.findOne(tx.getId() ).getEntity();
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getCommitmentDate());
	}
	
	private FinancialTransaction createTransaction() {
		Organization org = new Organization();
		org.setName("Org name for tx service testing");
		org 	= this.orgService.save(org).getEntity();
		
		final FinancialTransaction tx = new FinancialTransaction();
		tx.setAmountsReceived(BigMoney.parse("EUR 1230"));
		tx.setCommitments(BigMoney.parse("EUR 4320"));
		tx.setDescription("Some description for this tx");
		tx.setCommitmentDate(new LocalDateTime());
		tx.setExtendingAgency(org);

		logger.info(tx);
	
		return tx;
	}

}