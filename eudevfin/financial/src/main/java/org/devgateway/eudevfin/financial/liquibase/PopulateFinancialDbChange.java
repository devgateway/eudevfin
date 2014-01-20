package org.devgateway.eudevfin.financial.liquibase;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.devgateway.eudevfin.financial.util.CategoryConstants;
import org.devgateway.eudevfin.financial.util.FinancialConstants;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PopulateFinancialDbChange extends AbstractSpringCustomTaskChange {

	public static int NUM_OF_TX	= 1000;
	public static int NUM_OF_YEARS = 5;
	
	
	@Autowired
	private FinancialTransactionDaoImpl txDao;
	
	@Autowired
	private OrganizationDaoImpl orgDao;

	@Autowired
	private CategoryDaoImpl catDao;
	
	@Override
	@Transactional
	public void execute(Database database) throws CustomChangeException {		
		Random r = new Random();
		List<Organization> listOrgs = orgDao.findAllAsList();
		
		List<Category> listSec = catDao.findByTagsCode(CategoryConstants.SUB_SECTOR_TAG);
		List<Category> listTof = catDao.findByTagsCode(CategoryConstants.TYPE_OF_FINANCE_TAG);
		List<Category> listToa = catDao.findByTagsCode(CategoryConstants.TYPE_OF_AID_TAG);
		List<Category> listGroup = catDao.findByTagsCode(CategoryConstants.BI_MULTILATERAL_TAG);

		for (int j=0; j<=NUM_OF_YEARS; j++){
			for (int i=1; i<=NUM_OF_TX; i++ ) {
				FinancialTransaction tx 	= new FinancialTransaction();
				tx.setCommitments(BigMoney.parse("EUR " + Math.ceil(Math.random()*100000)));
				Organization org = null;
				Organization extAgency = null;
				Category sector = null;
				Category typeOfFlow = null;
				Category typeOfAid = null;
				Category biMultilateral = null;
				
				int orgRandomIndex=r.nextInt(listOrgs.size());
				int extAgencyRandomIndex=r.nextInt(listOrgs.size());
				int secRandomIndex=r.nextInt(listSec.size());
				int tofRandomIndex=r.nextInt(listTof.size());
				int toaRandomIndex=r.nextInt(listToa.size());
				int groupRandomIndex=r.nextInt(listGroup.size());
				
				org = listOrgs.get(orgRandomIndex);
				extAgency = listOrgs.get(extAgencyRandomIndex);
				sector = listSec.get(secRandomIndex);
				typeOfFlow = listTof.get(tofRandomIndex);
				typeOfAid = listToa.get(toaRandomIndex);
				biMultilateral = listGroup.get(groupRandomIndex);
				
				tx.setReportingOrganization( org );
				tx.setExtendingAgency(extAgency);
				tx.setLocale("en");
				tx.setDescription("CDA Test Transaction " + i + " en");
				tx.setLocale("ro");
				tx.setDescription("CDA Test Transaction " + i + " ro"); 
				tx.setSector(sector);
				tx.setTypeOfFlow(typeOfFlow);
				tx.setTypeOfAid(typeOfAid);
				tx.setBiMultilateral(biMultilateral);
				tx.setReportingYear(LocalDateTime.parse((2009+j) + "-07-01"));
				txDao.save(tx);
			}
			
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
