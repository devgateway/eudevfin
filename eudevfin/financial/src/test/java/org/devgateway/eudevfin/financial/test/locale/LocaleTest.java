/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.financial.test.locale;

import java.util.List;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.Constants;
import org.devgateway.eudevfin.common.locale.LocaleHelperInterface;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.service.OrganizationService;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/commonContext.xml", 
		"classpath:/META-INF/financialContext.xml", "classpath:/META-INF/commonMetadataContext.xml", 
		"classpath:META-INF/commonFinancialContext.xml","classpath:testFinancialContext.xml" })
@Component 
@Transactional
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
		this.localeHelper.setLocale(TEST_LOCALE);
	}
	
	@Test
	public void testLocalesOnFinancialTransaction() {
		for (int i = 1; i <= 10; i++) {
			Organization org = new Organization();
			org.setName("Org name " + i);
			org 	= this.orgService.save(org).getEntity();
			
			final FinancialTransaction tx = new FinancialTransaction();
			final int amount	= i * 100;
			tx.setAmountsReceived(BigMoney.parse("EUR " + amount));
			tx.setCommitments(BigMoney.parse("EUR " + amount));
			final String testLocaleString			= "This is transaction (test locale) " + i;
			final String defaultLocaleString		= "This is transaction (default locale) " + i;
			tx.setDescription(testLocaleString);
			tx.setLocale(Constants.DEFAULT_LOCALE);
			tx.setDescription(defaultLocaleString);
			tx.setExtendingAgency(org);

			tx.setLocale(null);
			
			logger.info(tx);
			this.service.save(tx);
			final FinancialTransaction result =  this.service.findOne(tx.getId()).getEntity();

			Assert.assertNotNull(result.getId());
			logger.info(result);
			
			Assert.assertNotNull( result.getTranslations().get(TEST_LOCALE) );
			Assert.assertEquals( testLocaleString, result.getDescription() );
			
			Assert.assertNotNull( result.getTranslations().get( Constants.DEFAULT_LOCALE ) );
			result.setLocale(Constants.DEFAULT_LOCALE);
			Assert.assertEquals( defaultLocaleString, result.getDescription() );
			
			result.setLocale("Inexistent locale");
			Assert.assertEquals( defaultLocaleString, result.getDescription() );

		}
		final List<FinancialTransaction> list = this.service.findAll();
		for (final FinancialTransaction financialTransaction : list) {
			Assert.assertNotNull( financialTransaction.getExtendingAgency() );
		}

	}
}
