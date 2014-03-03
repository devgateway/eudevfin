/**
 * 
 */
package org.devgateway.eudevfin.financial.test.locale;

import java.util.List;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.Constants;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.financial.service.OrganizationService;
import org.devgateway.eudevfin.financial.util.LocaleHelperInterface;
import org.joda.money.BigMoney;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class LocaleTest {
	
	private static Logger logger = Logger
			.getLogger(LocaleTest.class);
	
	public static final String  TEST_LOCALE = "fr";
	
	@Autowired
	FinancialTransactionService service;
	
	@Autowired
	OrganizationService orgService;
	
	@Autowired
	@Qualifier("localeHelperRequest")
	LocaleHelperInterface localeHelper;
	
	@Before
	public void setupTest() {
		localeHelper.setLocale(TEST_LOCALE);
	}
	
	@Test
	public void testLocalesOnFinancialTransaction() {
		for (int i = 1; i <= 10; i++) {
			Organization org = new Organization();
			org.setName("Org name " + i);
			org 	= orgService.save(org).getEntity();
			
			FinancialTransaction tx = new FinancialTransaction();
			int amount	= i * 100;
			tx.setAmountsReceived(BigMoney.parse("EUR " + amount));
			tx.setCommitments(BigMoney.parse("EUR " + amount));
			String testLocaleString			= "This is transaction (test locale) " + i;
			String defaultLocaleString		= "This is transaction (default locale) " + i;
			tx.setDescription(testLocaleString);
			tx.setLocale(Constants.DEFAULT_LOCALE);
			tx.setDescription(defaultLocaleString);
			tx.setExtendingAgency(org);

			logger.info(tx);
			service.save(tx);
			FinancialTransaction result =  service.findOne(tx.getId()).getEntity();

			Assert.assertNotNull(result.getId());
			logger.info(result);
			
			Assert.assertNotNull( result.getTranslations().get(TEST_LOCALE) );
			Assert.assertEquals( result.getDescription(), testLocaleString );
			
			Assert.assertNotNull( result.getTranslations().get( Constants.DEFAULT_LOCALE ) );
			result.setLocale(Constants.DEFAULT_LOCALE);
			Assert.assertEquals( result.getDescription(), defaultLocaleString );
			
			result.setLocale("Inexistent locale");
			Assert.assertEquals( result.getDescription(), defaultLocaleString );

		}
		List<FinancialTransaction> list = service.findAll();
		for (FinancialTransaction financialTransaction : list) {
			Assert.assertNotNull( financialTransaction.getExtendingAgency() );
		}

	}
}
