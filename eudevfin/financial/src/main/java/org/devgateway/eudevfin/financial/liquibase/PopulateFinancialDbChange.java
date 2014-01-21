package org.devgateway.eudevfin.financial.liquibase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import org.devgateway.eudevfin.common.liquibase.AbstractSpringCustomTaskChange;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.ChannelCategory;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.dao.ChannelCategoryDao;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.devgateway.eudevfin.financial.util.CategoryConstants;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PopulateFinancialDbChange extends AbstractSpringCustomTaskChange {

	//be gentle here, commit lower numbers
	public static int NUM_OF_TX	= 100;
	public static int NUM_OF_YEARS = 5;
	
	
	@Autowired
	private FinancialTransactionDaoImpl txDao;
	
	@Autowired
	private OrganizationDaoImpl orgDao;

	@Autowired
	private CategoryDaoImpl catDao;
	
	@Autowired
	private ChannelCategoryDao channelCatDao;

	@Override
	@Transactional
	public void execute(Database database) throws CustomChangeException {		
		Random r = new Random();
		List<Organization> listOrgs = orgDao.findAllAsList();
		
		List<Category> listSectors = catDao.findByTagsCode(CategoryConstants.ALL_SECTOR_TAG);
		List<Category> listTypeOfFlow = catDao.findByTagsCode(CategoryConstants.TYPE_OF_FLOW_TAG);
		List<Category> listTypeOfFinance = catDao.findByTagsCode(CategoryConstants.TYPE_OF_FINANCE_TAG);
		List<Category> listTypeofAid = catDao.findByTagsCode(CategoryConstants.TYPE_OF_AID_TAG);
		List<Category> listBiMultilateral = catDao.findByTagsCode(CategoryConstants.BI_MULTILATERAL_TAG);
		List<ChannelCategory> listChannel = channelCatDao.findAllAsList();
		List<ChannelCategory> listMultilateralChannel = getMultilaterals(listChannel);

		for (int j=0; j<=NUM_OF_YEARS; j++) {
			for (int i=1; i<=NUM_OF_TX; i++ ) {
				double amount				= Math.ceil(Math.random()*100000);
				boolean draft				= ((int)amount)%2==0 ? true:false;
				CustomFinancialTransaction tx 	= new CustomFinancialTransaction();
				tx.setDraft(draft);
				tx.setCommitments(BigMoney.parse("EUR " + amount) );
				Organization org = null;
				Organization extAgency = null;
				Category sector = null;
				Category typeOfFlow = null;
				Category typeOfFinance = null;
				Category typeOfAid = null;
				Category biMultilateral = null;
				ChannelCategory channel = null;
				
				int orgRandomIndex = r.nextInt(listOrgs.size());
				int extAgencyRandomIndex = r.nextInt(listOrgs.size());
				int sectorRandomIndex = r.nextInt(listSectors.size());
				int typeOfFlowRandomIndex = r.nextInt(listTypeOfFlow.size());
				int typeOfFinanceRandomIndex = r.nextInt(listTypeOfFinance.size());
				int typeOfAidRandomIndex = r.nextInt(listTypeofAid.size());
				int biMultilateralRandomIndex = r.nextInt(listBiMultilateral.size());
				int channelRandomIndex = r.nextInt(listMultilateralChannel.size());
				
				org = listOrgs.get(orgRandomIndex);
				extAgency = listOrgs.get(extAgencyRandomIndex);
				sector = listSectors.get(sectorRandomIndex);
				typeOfFlow = listTypeOfFlow.get(typeOfFlowRandomIndex);
				typeOfFinance = listTypeOfFinance.get(typeOfFinanceRandomIndex);
				typeOfAid = listTypeofAid.get(typeOfAidRandomIndex);
				biMultilateral = listBiMultilateral.get(biMultilateralRandomIndex);
				channel = listMultilateralChannel.get(channelRandomIndex);
				
				tx.setReportingOrganization(org);
				tx.setChannel(channel);
				tx.setExtendingAgency(extAgency);
				tx.setLocale("en");
				tx.setDescription("CDA Test Transaction " + i + " en");
				tx.setLocale("ro");
				tx.setDescription("CDA Test Transaction " + i + " ro"); 
				tx.setSector(sector);
				tx.setTypeOfFlow(typeOfFlow);
				tx.setTypeOfFinance(typeOfFinance);
				tx.setTypeOfAid(typeOfAid);
				tx.setBiMultilateral(biMultilateral);
				tx.setReportingYear(LocalDateTime.parse((2009+j) + "-07-01"));
				txDao.save(tx);
			}
			
		}
	}
	
	private List<ChannelCategory> getMultilaterals(
			List<ChannelCategory> listChannel) {
		List<ChannelCategory> filteredChannelList = new ArrayList<ChannelCategory>();
		for(Iterator<ChannelCategory> i = listChannel.iterator(); i.hasNext(); ) {
			ChannelCategory item = i.next();
		    if(item.getCode().equals("41000") || item.getCode().equals("42000") || item.getCode().equals("44000") || item.getCode().equals("46000") || item.getCode().equals("47000") ){
		    	filteredChannelList.add(item);
		    }
		}
		return filteredChannelList;
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
