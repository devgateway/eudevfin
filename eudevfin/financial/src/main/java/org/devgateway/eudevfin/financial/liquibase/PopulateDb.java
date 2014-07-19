/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.financial.liquibase;

import java.math.BigDecimal;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PopulateDb  extends AbstractSpringCustomTaskChange {
	
	public static int NUM_OF_TX	= 10;
	
	
	@Autowired
	private FinancialTransactionDaoImpl txDao;
	
	@Autowired
	private OrganizationDaoImpl orgDao;
	
	@Transactional 
	@Override
	public void execute(Database database) throws CustomChangeException {
		
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
			tx.setExtendingAgency( org );
			tx.setDescription("CDA Test Transaction " + i);
			txDao.save(tx);
		}
	}

	
	

	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ValidationErrors validate(Database database) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}
