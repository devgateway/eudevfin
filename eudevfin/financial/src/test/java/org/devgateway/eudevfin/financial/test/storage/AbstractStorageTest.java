package org.devgateway.eudevfin.financial.test.storage;

import java.math.BigDecimal;
import java.util.List;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractStorageTest {
	
	public static int NUM_OF_ORGS	= 3;
	public static int NUM_OF_TXS	= 3;

	
	@Autowired
	FinancialTransactionDaoImpl txDao;
	
	@Autowired
	OrganizationDaoImpl orgDao;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public  void setUpBefore() throws Exception {
		//Adding organizations
		for (int i=0; i<NUM_OF_ORGS; i++) {
			Organization o			= new Organization();
			o.setName("Testing versioning org " + i);
			o.setCode("This is a code " + i);
			orgDao.save(o);
		}
		List<Organization> allOrgs	= orgDao.findAllAsList();
		//Adding transactions
		for (int i=0, j=0; i< NUM_OF_TXS; i++,j=(j+1)%allOrgs.size() ) {
			FinancialTransaction ft = new FinancialTransaction();
			ft.setAmount(new BigDecimal(100*i));
			ft.setDescription("Transaction description " + i);
			ft.setDonorProjectNumber("3333/2013");
			ft.setReportingOrganization( allOrgs.get(j) );
			ft	= txDao.save(ft).getEntity();
		}
	
	}
	@After
	public void setUpAfter() throws Exception {
		txDao.delete( txDao.findAll() );
		orgDao.delete( orgDao.findAll() );
	}
}
