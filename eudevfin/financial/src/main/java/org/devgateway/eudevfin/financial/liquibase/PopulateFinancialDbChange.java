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
import org.devgateway.eudevfin.financial.util.FinancialConstants;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PopulateFinancialDbChange extends AbstractSpringCustomTaskChange {

	public static int NUM_OF_TX	= 100;
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
		createOrganizations();
		createSectors();
		createTypesOfFlow();
		createTypesOfAid();
		createBiMultilateral();
		Random r = new Random();
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
				
				List<Organization> listOrgs = orgDao.findAllAsList();
				List<Category> listSec = catDao.findByTagsCode(FinancialConstants.SUBSECTORS_TAG);
				List<Category> listTof = catDao.findByTagsCode(FinancialConstants.TYPEOFFLOW_TAG);
				List<Category> listToa = catDao.findByTagsCode(FinancialConstants.TYPEOFAID_TAG);
				List<Category> listGroup = catDao.findByTagsCode(FinancialConstants.BIMULTILATERAL_TAG);
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
				tx.setReportingYear(LocalDateTime.parse((2009+j) + "-06-06"));
				txDao.save(tx);
			}
			
		}
	}
	
	private LocalDateTime getRandomDate() {
		//Generate random date between 2010/2014
		int startYear = 2010;
		int endYear = 2011;
		int selectedYear = startYear + (int)(Math.random() * ((endYear - startYear) + 1));

		LocalDateTime time = LocalDateTime.parse(selectedYear + "-06-06");
		
		return time;
	}

	private void createBiMultilateral() {
		//First create the tags that define a Group
		Category odaGroupTag = new Category();
		odaGroupTag.setName("ODA Bi/Multilateral Tag");
		odaGroupTag.setCode(FinancialConstants.BIMULTILATERAL_TAG);
		catDao.save(odaGroupTag);
		
		Category group1 = new Category();
		group1.setCode("MULTI");
		group1.setLocale("en");
		group1.setName("Multilateral en");
		group1.setLocale("ro");
		group1.setName("Multilateral ro");
		group1.setTags(new HashSet<Category>());
		group1.getTags().add(odaGroupTag);
		catDao.save(group1);

		Category group2 = new Category();
		group2.setCode("BILAT");
		group2.setLocale("en");
		group2.setName("Bilateral en");
		group2.setLocale("ro");
		group2.setName("Bilateral ro");
		group2.setTags(new HashSet<Category>());
		group2.getTags().add(odaGroupTag);
		catDao.save(group2);


	}	
	private void createTypesOfAid() {
		//First create the tags that define a Sector
		Category typeOfAidTag = new Category();
		typeOfAidTag.setName("Type of Aid Tag");
		typeOfAidTag.setCode(FinancialConstants.TYPEOFAID_TAG);
		catDao.save(typeOfAidTag);
		
		Category toa1 = new Category();
		toa1.setCode("A");
		toa1.setLocale("en");
		toa1.setName("Budget support en");
		toa1.setLocale("ro");
		toa1.setName("Budget support ro");
		toa1.setTags(new HashSet<Category>());
		toa1.getTags().add(typeOfAidTag);
		catDao.save(toa1);

		Category toa2 = new Category();
		toa2.setCode("B");
		toa2.setLocale("en");
		toa2.setName("Core contributions and pooled programmes and funds en");
		toa2.setLocale("ro");
		toa2.setName("Core contributions and pooled programmes and funds ro");
		toa2.setTags(new HashSet<Category>());
		toa2.getTags().add(typeOfAidTag);
		catDao.save(toa2);

		Category toa3 = new Category();
		toa3.setCode("C");
		toa3.setLocale("en");
		toa3.setName("Project-type interventions en");
		toa3.setLocale("ro");
		toa3.setName("Project-type interventions ro");
		toa3.setTags(new HashSet<Category>());
		toa3.getTags().add(typeOfAidTag);
		catDao.save(toa3);

		Category toa4 = new Category();
		toa4.setCode("D");
		toa4.setLocale("en");
		toa4.setName("Experts and other technical assistance en");
		toa4.setLocale("ro");
		toa4.setName("Experts and other technical assistance ro");
		toa4.setTags(new HashSet<Category>());
		toa4.getTags().add(typeOfAidTag);
		catDao.save(toa4);

		Category toa5 = new Category();
		toa5.setCode("E");
		toa5.setLocale("en");
		toa5.setName("Scholarships and student costs in donor countries en");
		toa5.setLocale("ro");
		toa5.setName("Scholarships and student costs in donor countries ro");
		toa5.setTags(new HashSet<Category>());
		toa5.getTags().add(typeOfAidTag);
		catDao.save(toa5);

		Category toa6 = new Category();
		toa6.setCode("F");
		toa6.setLocale("en");
		toa6.setName("Debt relief en");
		toa6.setLocale("ro");
		toa6.setName("Debt relief ro");
		toa6.setTags(new HashSet<Category>());
		toa6.getTags().add(typeOfAidTag);
		catDao.save(toa6);

		Category toa7 = new Category();
		toa7.setCode("G");
		toa7.setLocale("en");
		toa7.setName("Administrative costs not included elsewhere en");
		toa7.setLocale("ro");
		toa7.setName("Administrative costs not included elsewhere ro");
		toa7.setTags(new HashSet<Category>());
		toa7.getTags().add(typeOfAidTag);
		catDao.save(toa7);

		Category toa8 = new Category();
		toa8.setCode("H");
		toa8.setLocale("en");
		toa8.setName("Other in-donor expenditures en");
		toa8.setLocale("ro");
		toa8.setName("Other in-donor expenditures ro");
		toa8.setTags(new HashSet<Category>());
		toa8.getTags().add(typeOfAidTag);
		catDao.save(toa8);

	}
	private void createTypesOfFlow() {
		//First create the tags that define a Sector
		Category typeOfFlowTag = new Category();
		typeOfFlowTag.setName("Type of Flow Tag");
		typeOfFlowTag.setCode(FinancialConstants.TYPEOFFLOW_TAG);
		catDao.save(typeOfFlowTag);
		
		Category tof1 = new Category();
		tof1.setCode("11");
		tof1.setLocale("en");
		tof1.setName("ODA Grant en");
		tof1.setLocale("ro");
		tof1.setName("ODA Grant ro");
		tof1.setTags(new HashSet<Category>());
		tof1.getTags().add(typeOfFlowTag);
		catDao.save(tof1);

		Category tof2 = new Category();
		tof2.setCode("12");
		tof2.setLocale("en");
		tof2.setName("ODA Grant-like en");
		tof2.setLocale("ro");
		tof2.setName("ODA Grant-like ro");
		tof2.setTags(new HashSet<Category>());
		tof2.getTags().add(typeOfFlowTag);
		catDao.save(tof2);

		Category tof3 = new Category();
		tof3.setCode("13");
		tof3.setLocale("en");
		tof3.setName("ODA Loan en");
		tof3.setLocale("ro");
		tof3.setName("ODA Loan ro");
		tof3.setTags(new HashSet<Category>());
		tof3.getTags().add(typeOfFlowTag);
		catDao.save(tof3);

		Category tof4 = new Category();
		tof4.setCode("19");
		tof4.setLocale("en");
		tof4.setName("ODA equity investment en");
		tof4.setLocale("ro");
		tof4.setName("ODA equity investment ro");
		tof4.setTags(new HashSet<Category>());
		tof4.getTags().add(typeOfFlowTag);
		catDao.save(tof4);

		Category tof5 = new Category();
		tof5.setCode("14");
		tof5.setLocale("en");
		tof5.setName("OOF loan en");
		tof5.setLocale("ro");
		tof5.setName("OOF loan ro");
		tof5.setTags(new HashSet<Category>());
		tof5.getTags().add(typeOfFlowTag);
		catDao.save(tof5);
		
	}

	private void createSectors() {

		//First create the tags that define a Sector
		Category sectorsTag = new Category();
		sectorsTag.setName("Sectors Tag");
		sectorsTag.setCode(FinancialConstants.SECTORS_TAG);
		catDao.save(sectorsTag);
		
		Category subsectorsTag = new Category();
		subsectorsTag.setName("Sub-Sectors Tag");
		subsectorsTag.setCode(FinancialConstants.SUBSECTORS_TAG);
		subsectorsTag.setParentCategory(sectorsTag);		
		catDao.save(subsectorsTag);

		Category sectorsRoot = new Category();
		sectorsRoot.setLocale("en");
		sectorsRoot.setName("DAC Sector Scheme");
		sectorsRoot.setLocale("ro");
		sectorsRoot.setName("DAC Sector Scheme ro");
		sectorsRoot.setCode(FinancialConstants.SECTORS_ROOT);
		sectorsRoot.setTags(new HashSet<Category>());
		sectorsRoot.getTags().add(sectorsTag);
		catDao.save(sectorsRoot);
		
		
		Category s1 = new Category();
		s1.setCode("x430");
		s1.setLocale("en");
		s1.setName("Others en");
		s1.setLocale("ro");
		s1.setName("Others ro");
		s1.setTags(new HashSet<Category>());
		s1.getTags().add(sectorsTag);
		s1.getTags().add(subsectorsTag);
		s1.setParentCategory(sectorsRoot);
		catDao.save(s1);
		
		Category s2 = new Category();
		s2.setCode("x510");
		s2.setLocale("en");
		s2.setName("Humanitarian Aid");
		s2.setLocale("ro");
		s2.setName("Humanitarian Aid");
		s2.setTags(new HashSet<Category>());
		s2.getTags().add(sectorsTag);
		s2.getTags().add(subsectorsTag);
		s2.setParentCategory(sectorsRoot);
		catDao.save(s2);

		Category s3 = new Category();
		s3.setCode("x210");
		s3.setLocale("en");
		s3.setName("Development food aid");
		s3.setLocale("ro");
		s3.setName("Development food aid");
		s3.setTags(new HashSet<Category>());
		s3.getTags().add(sectorsTag);
		s3.getTags().add(subsectorsTag);
		s3.setParentCategory(sectorsRoot);
		catDao.save(s3);

		Category s4 = new Category();
		s4.setCode("x230");
		s4.setLocale("en");
		s4.setName("Energy Generation and Support en");
		s4.setLocale("ro");
		s4.setName("Energy Generation and Support ro");
		s4.setTags(new HashSet<Category>());
		s4.getTags().add(sectorsTag);
		s4.getTags().add(subsectorsTag);
		s4.setParentCategory(sectorsRoot);
		catDao.save(s4);

		Category s5 = new Category();
		s5.setCode("x311");
		s5.setLocale("en");
		s5.setName("Agriculture en");
		s5.setLocale("ro");
		s5.setName("Agriculture ro");
		s5.setTags(new HashSet<Category>());
		s5.getTags().add(sectorsTag);
		s5.getTags().add(subsectorsTag);
		s5.setParentCategory(sectorsRoot);
		catDao.save(s5);
	
	}

	private void createOrganizations() {
		Organization o1 = new Organization();
		
		o1.setCode("ONU");
		o1.setLocale("en");
		o1.setName("United Nations");
		o1.setLocale("ro");
		o1.setName("United Nations");
		
		orgDao.save(o1);
		
		Organization o2 = new Organization();
		o2.setCode("EU");
		o2.setLocale("en");
		o2.setName("EU");
		o2.setLocale("ro");
		o2.setName("EU");
		
		orgDao.save(o2);

		Organization o3 = new Organization();
		o3.setCode("WB");
		o3.setLocale("en");
		o3.setName("World Bank");
		o3.setLocale("ro");
		o3.setName("World Bank");
		
		orgDao.save(o3);

		Organization o4 = new Organization();
		o4.setCode("GRP1");
		o4.setLocale("en");
		o4.setName("Regional development banks and funds");
		o4.setLocale("ro");
		o4.setName("Regional development banks and funds");
		
		orgDao.save(o4);

		Organization o5 = new Organization();
		o5.setCode("OTHER");
		o5.setLocale("en");
		o5.setName("Other");
		o5.setLocale("ro");
		o5.setName("Other");
		
		orgDao.save(o5);
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
