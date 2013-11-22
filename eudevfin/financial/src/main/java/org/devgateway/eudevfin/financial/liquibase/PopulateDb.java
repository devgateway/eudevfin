package org.devgateway.eudevfin.financial.liquibase;

import java.math.BigDecimal;

import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component  
public class PopulateDb {
	
	public static int NUM_OF_TX	= 10;
	
	
	@Autowired
	private FinancialTransactionDaoImpl txDao;
	
	@Autowired
	private OrganizationDaoImpl orgDao;
	
	@Transactional 
	public void populate() {
		
		Organization o1 = new Organization();
		o1.setCode("anOrgCode1");
		o1.setName("CDA Test Org 1");
		
		orgDao.save(o1);
		
		Organization o2 = new Organization();
		o2.setCode("anOrgCode2");
		o2.setName("CDA Test Org 2");
		
		orgDao.save(o2);
		
		for (int i=1; i<=NUM_OF_TX; i++ ) {
			FinancialTransaction tx 	= new FinancialTransaction();
			tx.setAmount(new BigDecimal(1000));
			Organization org = null;
			if ( i<NUM_OF_TX/2 )
				org = o1;
			else
				org = o2;
			tx.setReportingOrganization( org );
			tx.setDescription("CDA Test Transaction " + i);
			txDao.save(tx);
		}
	}

	

	
}
