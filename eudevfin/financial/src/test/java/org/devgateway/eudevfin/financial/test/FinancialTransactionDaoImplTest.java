/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.financial.test;

import java.math.BigDecimal;
import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/commonContext.xml", "classpath:/META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testFinancialContext.xml" })
//@TransactionConfiguration(defaultRollback=false, transactionManager="transactionManager")
@Transactional
public class FinancialTransactionDaoImplTest {

	@Autowired
	FinancialTransactionDaoImpl txDao;
	
	@Autowired
	OrganizationDaoImpl orgDao;
	
	@Test
	public void testLoadAllFinancialTransactions() {
		final List<FinancialTransaction> allTx	= this.txDao.findAllAsList();
		Assert.assertNotNull(allTx);
	}
	@Test
	public void testSaveFinancialTransaction() {
		final Organization o			= new Organization();
		o.setName("Testing auditing Org");
		this.orgDao.save(o);
		FinancialTransaction ft = new FinancialTransaction();
		ft.setAmount(new BigDecimal(123));
		ft.setDescription("Auditing test descr");
		ft.setExtendingAgency(o);
		ft	= this.txDao.save(ft).getEntity();
		Assert.assertNotNull(ft);
	}

}
