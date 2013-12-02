package org.devgateway.eudevfin.financial.liquibase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.joda.money.BigMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component  
public class PopulateMockupDb {
	
	public static int NUM_OF_TX	= 50;
	
	
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
		Random r = new Random();
		for (int i=1; i<=NUM_OF_TX; i++ ) {
			FinancialTransaction tx 	= new FinancialTransaction();
			tx.setCommitments(BigMoney.parse("EUR " + Math.ceil(Math.random()*100000)));
			Organization org = null;
			Category sector = null;
			List<Organization> listOrgs = orgDao.findAllAsList();
			List<Category> listSec = catDao.findAllAsList();
			int randomIndex1=r.nextInt(5);
			int randomIndex2=r.nextInt(5);
			
			System.out.println(randomIndex1);
			System.out.println(randomIndex2);
			
			org = listOrgs.get(randomIndex1);
			sector = listSec.get(randomIndex2);
			tx.setReportingOrganization( org );
			tx.setDescription("CDA Test Transaction " + i);
			tx.setSector(sector);
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

		Category s3 = new Category();
		s3.setCode("210");
		s3.setName("Transport and Storage");
		catDao.save(s3);

		Category s4 = new Category();
		s4.setCode("230");
		s4.setName("Energy Generation and Support");
		catDao.save(s4);

		Category s5 = new Category();
		s5.setCode("311");
		s5.setName("Agriculture");
		catDao.save(s5);

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

		Organization o3 = new Organization();
		o3.setCode("anOrgCode3");
		o3.setName("CDA Test Org 3");
		
		orgDao.save(o3);

		Organization o4 = new Organization();
		o4.setCode("anOrgCode4");
		o4.setName("CDA Test Org 4");
		
		orgDao.save(o4);

		Organization o5 = new Organization();
		o5.setCode("anOrgCode5");
		o5.setName("CDA Test Org 5");
		
		orgDao.save(o5);
	}

	

	
}
