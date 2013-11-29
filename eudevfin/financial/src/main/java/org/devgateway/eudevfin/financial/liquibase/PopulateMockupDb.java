package org.devgateway.eudevfin.financial.liquibase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component  
public class PopulateMockupDb {
	
	public static int NUM_OF_TX	= 10;
	
	
	@Autowired
	private FinancialTransactionDaoImpl txDao;
	
	@Autowired
	private OrganizationDaoImpl orgDao;

	@Autowired
	private CategoryDaoImpl catDao;
	
	@Transactional 
	public void populate() {
		
		createOrganizations();
		createSectors();
		createTypesOfAid();
		
		for (int i=1; i<=NUM_OF_TX; i++ ) {
			FinancialTransaction tx 	= new FinancialTransaction();
			tx.setAmount(new BigDecimal(1000));
			Organization org = null;
			List<Organization> list = orgDao.findAllAsList();
			if ( i<NUM_OF_TX/2 )
				org = list.get(0);
			else
				org = list.get(1);
			tx.setReportingOrganization( org );
			tx.setDescription("CDA Test Transaction " + i);
			txDao.save(tx);
		}
	}

	private void createTypesOfAid() {
	}

	private void createSectors() {

		Category s1 = new Category();
		s1.setCode("430");
		s1.setName("Others");
		catDao.save(s1);

		Category s2 = new Category();
		s2.setCode("510");
		s2.setName("General Budget Support");
		catDao.save(s2);

		Category sectorScheme = new Category();
		sectorScheme.setCode("000");
		sectorScheme.setName("DAC Sector Scheme");
		
		HashSet<Category> sectorList = new HashSet<Category>(catDao.findAllAsList());

		sectorScheme.setTags(sectorList);
		catDao.save(sectorScheme);
	
	}

	private void createOrganizations() {
		Organization o1 = new Organization();
		
		o1.setCode("anOrgCode1");
		o1.setName("CDA Test Org 1");
		
		orgDao.save(o1);
		
		Organization o2 = new Organization();
		o2.setCode("anOrgCode2");
		o2.setName("CDA Test Org 2");
		
		orgDao.save(o2);
	}

	

	
}
