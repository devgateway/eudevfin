package org.devgateway.eudevfin.reports.test;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.sql.DataSource;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Util.PropertyList;
import mondrian.rolap.RolapConnectionProperties;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.olap.JRMondrianQueryExecuterFactory;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.Area;
import org.devgateway.eudevfin.financial.ChannelCategory;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.AreaDaoImpl;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.dao.ChannelCategoryDao;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/commonAuthContext.xml",
		"classpath:META-INF/commonContext.xml",
		"classpath:META-INF/authContext.xml",
		"classpath:META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml",
		"classpath:META-INF/importMetadataContext.xml",
		"classpath:META-INF/exchangeContext.xml",
		"classpath:META-INF/commonExchangeContext.xml",
		"classpath:META-INF/cdaContext.xml",
		"classpath:META-INF/cdfContext.xml" })
public class ReportsDataTest {
	protected static Logger logger = Logger.getLogger(ReportsSimpleTest.class);

	@Autowired
	private DataSource cdaDataSource;

	@Autowired
	private FinancialTransactionDaoImpl txDao;

	@Autowired
	private OrganizationDaoImpl orgDao;

	@Autowired
	private CategoryDaoImpl catDao;

	@Autowired
	private ChannelCategoryDao channelCatDao;

	@Autowired
	private AreaDaoImpl areaDao;

	Set<Map<String, String>> mockupData;
	Set<CustomFinancialTransaction> transactions = new HashSet<CustomFinancialTransaction>();
	Map<String, Object> expectedResults = new HashMap<String, Object>();

	@Test
	public void testReport() {
		JasperPrint jp = buildReport();
		runAssertions(jp);
	}
	
    @Before
	public void prepareMockupData() {
		List<Area> areas = areaDao.findAllAsList();
		Organization extendingOrg = orgDao.findOne(1l).getEntity();
		List<ChannelCategory> channels = getMultilaterals(channelCatDao.findAllAsList());
		
		CustomFinancialTransaction tx1 = createMockup("bilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##1", "A", "700", findAreaByName(areas, "Nepal"), null, "TYPE_OF_FINANCE##100", "Test Description 1", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx1);
		transactions.add(tx1);

		CustomFinancialTransaction tx2 = createMockup("bilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##1", "B", "700", findAreaByName(areas, "Mozambique"), null, "TYPE_OF_FINANCE##100", "Test Description 2", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx2);
		transactions.add(tx2);

		CustomFinancialTransaction tx3 = createMockup("bilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##1", "C", "600", findAreaByName(areas, "Uganda"), null, "TYPE_OF_FINANCE##100", "Test Description 3", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx3);
		transactions.add(tx3);

		CustomFinancialTransaction tx4 = createMockup("bilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##1", "D", "520", findAreaByName(areas, "Uganda"), null, "TYPE_OF_FINANCE##400", "Test Description 4", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx4);
		transactions.add(tx4);

		CustomFinancialTransaction tx5 = createMockup("bilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##1", "F", "600", findAreaByName(areas, "Mozambique"), null, "TYPE_OF_FINANCE##610", "Test Description 5", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx5);
		transactions.add(tx5);

		CustomFinancialTransaction tx6 = createMockup("bilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##1", "G", "730", findAreaByName(areas, "Afghanistan"), null, "TYPE_OF_FINANCE##400", "Test Description 6", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx6);
		transactions.add(tx6);

		CustomFinancialTransaction tx7 = createMockup("bilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##1", "F", "73010", findAreaByName(areas, "Iraq"), null, "TYPE_OF_FINANCE##400", "Test Description 7", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx7);
		transactions.add(tx7);

		CustomFinancialTransaction tx8 = createMockup("bilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##1", "F", "510", findAreaByName(areas, "Afghanistan"), null, "TYPE_OF_FINANCE##100", "Test Description 8", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx8);
		transactions.add(tx8);

		CustomFinancialTransaction tx9 = createMockup("bilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##1", "A", "700", findAreaByName(areas, "Nepal"), null, "TYPE_OF_FINANCE##100", "Test Description 9", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx9);
		transactions.add(tx9);

		CustomFinancialTransaction tx10 = createMockup("bilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##1", "B", "700", findAreaByName(areas, "Mozambique"), null, "TYPE_OF_FINANCE##100", "Test Description 10", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx10);
		transactions.add(tx10);

		CustomFinancialTransaction tx11 = createMockup("bilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##1", "C", "600", findAreaByName(areas, "Uganda"), null, "TYPE_OF_FINANCE##100", "Test Description 11", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx11);
		transactions.add(tx11);

		CustomFinancialTransaction tx12 = createMockup("bilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##1", "D", "520", findAreaByName(areas, "Uganda"), null, "TYPE_OF_FINANCE##400", "Test Description 12", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx12);
		transactions.add(tx12);

		CustomFinancialTransaction tx13 = createMockup("bilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##1", "F", "600", findAreaByName(areas, "Mozambique"), null, "TYPE_OF_FINANCE##610", "Test Description 13", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx13);
		transactions.add(tx13);

		CustomFinancialTransaction tx14 = createMockup("bilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##1", "G", "730", findAreaByName(areas, "Afghanistan"), null, "TYPE_OF_FINANCE##400", "Test Description 14", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx14);
		transactions.add(tx14);

		CustomFinancialTransaction tx15 = createMockup("bilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##1", "F", "73010", findAreaByName(areas, "Iraq"), null, "TYPE_OF_FINANCE##400", "Test Description 15", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx15);
		transactions.add(tx15);

		CustomFinancialTransaction tx16 = createMockup("bilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##1", "F", "510", findAreaByName(areas, "Afghanistan"), null, "TYPE_OF_FINANCE##100", "Test Description 16", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx16);
		transactions.add(tx16);

		CustomFinancialTransaction tx17 = createMockup("multilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##2", "A", "510", null, findChannelByName(channels, "41000"), "TYPE_OF_FINANCE##100", "Test Description 17", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx17);
		transactions.add(tx17);
		
		CustomFinancialTransaction tx18 = createMockup("multilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##2", "A", "510", null, findChannelByName(channels, "42000"), "TYPE_OF_FINANCE##100", "Test Description 17", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx18);
		transactions.add(tx18);
		
		CustomFinancialTransaction tx19 = createMockup("multilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##2", "A", "510", null, findChannelByName(channels, "44000"), "TYPE_OF_FINANCE##100", "Test Description 17", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx19);
		transactions.add(tx19);
		
		CustomFinancialTransaction tx20 = createMockup("multilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##2", "A", "510", null, findChannelByName(channels, "46000"), "TYPE_OF_FINANCE##100", "Test Description 17", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx20);
		transactions.add(tx20);

		CustomFinancialTransaction tx21 = createMockup("multilateralOda.advanceQuestionnaire", "2011", "BI_MULTILATERAL##2", "A", "510", null, findChannelByName(channels, "47000"), "TYPE_OF_FINANCE##100", "Test Description 17", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx21);
		transactions.add(tx21);

		CustomFinancialTransaction tx22 = createMockup("multilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##2", "A", "510", null, findChannelByName(channels, "41000"), "TYPE_OF_FINANCE##100", "Test Description 17", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx22);
		transactions.add(tx22);
		
		CustomFinancialTransaction tx23 = createMockup("multilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##2", "A", "510", null, findChannelByName(channels, "42000"), "TYPE_OF_FINANCE##100", "Test Description 17", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx23);
		transactions.add(tx23);
		
		CustomFinancialTransaction tx24 = createMockup("multilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##2", "A", "510", null, findChannelByName(channels, "44000"), "TYPE_OF_FINANCE##100", "Test Description 17", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx24);
		transactions.add(tx24);
		
		CustomFinancialTransaction tx25 = createMockup("multilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##2", "A", "510", null, findChannelByName(channels, "46000"), "TYPE_OF_FINANCE##100", "Test Description 17", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx25);
		transactions.add(tx25);

		CustomFinancialTransaction tx26 = createMockup("multilateralOda.advanceQuestionnaire", "2012", "BI_MULTILATERAL##2", "A", "510", null, findChannelByName(channels, "47000"), "TYPE_OF_FINANCE##100", "Test Description 17", "EUR 1000000", "EUR 100000", extendingOrg);
		txDao.save(tx26);
		transactions.add(tx26);

	}

    @After
    public void deleteMockupData(){
		for (CustomFinancialTransaction ft : transactions) {
			txDao.delete(ft);
		}
    }

	private JasperPrint buildReport() {
		try {
			PropertyList properties = getPropertyList();
			Connection connection = DriverManager.getConnection(properties,
					null, cdaDataSource);
            InputStream inputStream = ReportsDataTest.class.getClassLoader().
                    getResourceAsStream("org/devgateway/eudevfin/reports/core/aq/aq_master.jasper");
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(JRParameter.REPORT_LOCALE, new Locale("en"));
			parameters
					.put(JRMondrianQueryExecuterFactory.PARAMETER_MONDRIAN_CONNECTION,
							connection);
			parameters.put("FIRST_YEAR", 2011);
			parameters.put("SECOND_YEAR", 2012);
			parameters.put("CURRENCY", "USD");

            String subdirPath =  "org/devgateway/eudevfin/reports/core/aq";
            parameters.put("SUBDIR_PATH", subdirPath);

			// set resource bundle
            ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("org/devgateway/eudevfin/reports/i18n", new Locale("en"));

			parameters.put("REPORTING_COUNTRY", "Donor Name");
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(inputStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters);

			return jasperPrint;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private CustomFinancialTransaction createMockup(String formType, String year,
			String biMultilateral, String typeOfAid, String sector, Area area, ChannelCategory channel, 
			String typeOfFinance, String description, String extendedAmount, String receivedAmount, 
			Organization extendingOrg) {

		CustomFinancialTransaction tx = new CustomFinancialTransaction();

		tx.setDraft(false);
		tx.setCreatedBy("admin");
		tx.setModifiedBy("admin");
		tx.setFormType(formType);
		tx.setExtendingAgency(extendingOrg);
		tx.setTypeOfFlow(catDao.findByCode("TYPE_OF_FLOW##10").get(0));
		tx.setReportingYear(LocalDateTime.parse((year) + "-07-01"));
		tx.setBiMultilateral(catDao.findByCode(biMultilateral).get(0));
		tx.setTypeOfAid(catDao.findByCode(typeOfAid).get(0));
		tx.setSector(catDao.findByCode(sector).get(0));
		tx.setRecipient(area);
		tx.setChannel(channel);
		tx.setTypeOfFinance(catDao.findByCode(typeOfFinance).get(0));
		tx.setDescription(description);
		tx.setAmountsExtended(BigMoney.parse(extendedAmount));
		tx.setAmountsReceived(BigMoney.parse(receivedAmount));
		return tx;
	}

	private PropertyList getPropertyList() {
		PropertyList propertyList = new PropertyList();
		propertyList.put("Provider", "mondrian");
		propertyList.put("Catalog",	this.getClass().getResource("/org/devgateway/eudevfin/reports/core/service/financial.mondrian.xml").toString());
		propertyList.put(RolapConnectionProperties.DynamicSchemaProcessor.toString(), "org.devgateway.eudevfin.reports.core.utils.SchemaProcessor");
		propertyList.put("CURRENCY", "USD");
		propertyList.put("LOCALE", "en");
		propertyList.put("COUNTRY_CURRENCY", "EUR");

		return propertyList;
	}

	private void runAssertions(JasperPrint print) {
		loadExpectedResults();
		List<JRPrintPage> pages = print.getPages();
		for (Iterator<JRPrintPage> iterator = pages.iterator(); iterator.hasNext();) {

			JRPrintPage page = iterator.next();
			List<JRPrintElement> elements = page.getElements();

			JRTemplatePrintText textField = null;
			for (Iterator<JRPrintElement> elementsIterator = elements
					.iterator(); elementsIterator.hasNext();) {
				JRPrintElement element = elementsIterator.next();
				if (element instanceof JRTemplatePrintText && element.getKey() != null && !element.getKey().equals("")) {
					textField = (JRTemplatePrintText) element;
					logger.info("Testing values: "  +  textField.getValue() + ", should be: " + expectedResults.get(element.getKey()));
					Assert.assertEquals(expectedResults.get(element.getKey()), textField.getValue());
				}
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

	private Area findAreaByName(List<Area> listAreas, String name) {
		for (Area area : listAreas) {
			if (area.getName().equals(name)) {
				return area;
			}
		}

		return null;
	}
	private ChannelCategory findChannelByName(List<ChannelCategory> listChannels, String name) {
		for (ChannelCategory channel : listChannels) {
			if (channel.getCode().equals(name)) {
				return channel;
			}
		}

		return null;
	}
	
	//Expected results may vary depending on the logic.
	private void loadExpectedResults() {
		expectedResults.put("extendedAmountYear1", 16.263);
		expectedResults.put("extendedAmountYear2", 14.976);
		expectedResults.put("budgetSupportYear1", 1.251);
		expectedResults.put("coreContributionsYear1", 1.251);
		expectedResults.put("projectTypeYear1", 1.251);
		expectedResults.put("expertsYear1", 1.251);
		expectedResults.put("debtReliefYear1", 3.753);
		expectedResults.put("administrativeCostsYear1", 1.251);
		expectedResults.put("allTypesYear1", 10.008);
		expectedResults.put("projectTypeYear2", 1.152);
		expectedResults.put("budgetSupportYear2", 1.152);
		expectedResults.put("expertsYear2", 1.152);
		expectedResults.put("debtReliefYear2", 3.456);
		expectedResults.put("administrativeCostsYear2", 1.152);
		expectedResults.put("coreContributionsYear2", 1.152);
		expectedResults.put("allTypesYear2", 9.216);
		expectedResults.put("UnitedNationsYear1", 1.251);
		expectedResults.put("EuropeanUnionYear1", 1.251);
		expectedResults.put("WorldBankYear1", 1.251);
		expectedResults.put("RegionalBanksYear1", 1.251);
		expectedResults.put("OthersYear1", 1.251);
		expectedResults.put("allCodesYear1", 6.255);
		expectedResults.put("EuropeanUnionYear2", 1.152);
		expectedResults.put("OthersYear2", 1.152);
		expectedResults.put("WorldBankYear2", 1.152);
		expectedResults.put("allCodesYear2", 5.76);
		expectedResults.put("RegionalBanksYear2", 1.152);
		expectedResults.put("UnitedNationsYear2", 1.152);
		expectedResults.put("LDCYear1", 8.757);
		expectedResults.put("AfricaYear1", 5.004);
		expectedResults.put("SouthOfSaharaYear1", 5.004);
		expectedResults.put("SouthOfSaharaDebtReliefYear1", 1.251);
		expectedResults.put("AfghanistanYear1", 2.502);
		expectedResults.put("AfghanistanReconstructionYear1", 1.251);
		expectedResults.put("IraqYear1", 1.251);
		expectedResults.put("IraqReconstructionYear1", 1.251);
		expectedResults.put("LDCYear2", 8.064);
		expectedResults.put("AfricaYear2", 4.608);
		expectedResults.put("SouthOfSaharaYear2", 4.608);
		expectedResults.put("SouthOfSaharaDebtReliefYear2", 1.152);
		expectedResults.put("AfghanistanYear2", 2.304);
		expectedResults.put("AfghanistanReconstructionYear2", 1.152);
		expectedResults.put("IraqYear2", 1.152);
		expectedResults.put("IraqReconstructionYear2", 1.152);
		expectedResults.put("humanitarianAidYear1", 5.004);
		expectedResults.put("shortTermReconstructionYear1", 1.251);
		expectedResults.put("developmentFoodAidYear1", 1.251);
		expectedResults.put("humanitarianAidYear2", 4.608);
		expectedResults.put("shortTermReconstructionYear2", 1.152);
		expectedResults.put("developmentFoodAidYear2", 1.152);
		expectedResults.put("debtReliefGrantsYear1", 1.251);
		expectedResults.put("debtReliefGrantsYear2", 1.152);
		expectedResults.put("totalGrossODAYear1", 18.07);
		expectedResults.put("totalGrossODAYear2", 16.64);		
	}


}