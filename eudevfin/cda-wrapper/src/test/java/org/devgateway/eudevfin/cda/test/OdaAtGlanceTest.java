package org.devgateway.eudevfin.cda.test;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.cda.domain.QueryResult;
import org.devgateway.eudevfin.cda.service.QueryService;
import org.devgateway.eudevfin.financial.Area;
import org.devgateway.eudevfin.financial.Category;
import org.devgateway.eudevfin.financial.ChannelCategory;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.dao.AreaDaoImpl;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.dao.ChannelCategoryDao;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.devgateway.eudevfin.financial.util.LocaleHelperInterface;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ionut Dobre on 2/20/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/commonAuthContext.xml",
        "classpath:META-INF/commonContext.xml",
        "classpath:META-INF/authContext.xml",
        "classpath:META-INF/financialContext.xml",
        "classpath:META-INF/commonFinancialContext.xml",
        "classpath:META-INF/importMetadataContext.xml",
        "classpath:META-INF/exchangeContext.xml",
        "classpath:META-INF/commonExchangeContext.xml",
        "classpath:META-INF/cdaContext.xml",
        "classpath:testCDAContext.xml"
})
public class OdaAtGlanceTest {
    protected static Logger logger = Logger.getLogger(OdaAtGlanceTest.class);

    protected List<CustomFinancialTransaction> transactions;
    protected Organization organization;

    private static final String TEST_LOCALE = "en";

    @Autowired
    @Qualifier("localeHelperRequest")
    LocaleHelperInterface localeHelper;

    @Autowired
    QueryService CdaService;

    @Autowired
    private FinancialTransactionDaoImpl txDao;

    @Autowired
    OrganizationDaoImpl orgDao;

    @Autowired
    private CategoryDaoImpl catDao;

    @Autowired
    private ChannelCategoryDao channelCatDao;

    @Autowired
    private AreaDaoImpl areaDao;

    @Before
    public void setUp () {
        localeHelper.setLocale(TEST_LOCALE);

        transactions = new ArrayList<>();
        List<Category> listTypeOfFlow,
                listSectors,
                listTypeOfFinance,
                listTypeOfAid,
                listBiMultilateral;
        List<Area> listAreas;
        ChannelCategory channel;
        Category sector,
                typeOfFlow,
                typeOfFinance,
                typeOfAid,
                biMultilateral;
        Area area;

        // create the database structure
        organization = new Organization();
        organization.setName("Testing auditing Org");
        orgDao.save(organization);

        // add 1st transaction
        listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##10");
        listSectors = catDao.findByCode("321");
        listTypeOfFinance = catDao.findByCode("TYPE_OF_FINANCE##110");
        listTypeOfAid = catDao.findByCode("A");
        listBiMultilateral = catDao.findByCode("BI_MULTILATERAL##1");
        listAreas = areaDao.findAllAsList();
        channel = channelCatDao.findByCode("21001").getEntity();

        sector = listSectors.get(0);
        typeOfFlow = listTypeOfFlow.get(0);
        typeOfFinance = listTypeOfFinance.get(0);
        typeOfAid = listTypeOfAid.get(0);
        biMultilateral = listBiMultilateral.get(0);
        area = findAreaByName(listAreas, "Moldova");

        CustomFinancialTransaction ft = new CustomFinancialTransaction();

        ft.setDraft(false);
        ft.setChannel(channel);
        ft.setLocale("en");
        ft.setShortDescription( "Auditing test description 1");
        ft.setDescription("Long - " + ft.getDescription() );
        ft.setSector(sector);
        ft.setTypeOfFlow(typeOfFlow);
        ft.setTypeOfFinance(typeOfFinance);
        ft.setTypeOfAid(typeOfAid);
        ft.setReportingYear(LocalDateTime.parse((2012) + "-07-01"));
        ft.setFormType("bilateralOda.advanceQuestionnaire");
        ft.setBiMultilateral(biMultilateral);
        ft.setRecipient(area);

        ft.setAmountsExtended(BigMoney.parse("EUR " + 100000));
        ft.setExtendingAgency(organization);

        txDao.save(ft);
        transactions.add(ft);           // save the transaction so we can delete it later

        // add 2nd transaction
        listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##10");
        listSectors = catDao.findByCode("321");
        listTypeOfFinance = catDao.findByCode("TYPE_OF_FINANCE##110");
        listTypeOfAid = catDao.findByCode("A");
        listBiMultilateral = catDao.findByCode("BI_MULTILATERAL##1");
        listAreas = areaDao.findAllAsList();
        channel = channelCatDao.findByCode("21001").getEntity();

        sector = listSectors.get(0);
        typeOfFlow = listTypeOfFlow.get(0);
        typeOfFinance = listTypeOfFinance.get(0);
        typeOfAid = listTypeOfAid.get(0);
        biMultilateral = listBiMultilateral.get(0);
        area = findAreaByName(listAreas, "Romania");

        ft = new CustomFinancialTransaction();

        ft.setDraft(false);
        ft.setChannel(channel);
        ft.setLocale("en");
        ft.setShortDescription( "Auditing test description 2");
        ft.setDescription("Long - " + ft.getDescription() );
        ft.setSector(sector);
        ft.setTypeOfFlow(typeOfFlow);
        ft.setTypeOfFinance(typeOfFinance);
        ft.setTypeOfAid(typeOfAid);
        ft.setReportingYear(LocalDateTime.parse((2013) + "-07-01"));
        ft.setFormType("bilateralOda.advanceQuestionnaire");
        ft.setBiMultilateral(biMultilateral);
        ft.setRecipient(area);

        ft.setAmountsExtended(BigMoney.parse("EUR " + 1000000));
        ft.setExtendingAgency(organization);

        txDao.save(ft);
        transactions.add(ft);           // save the transaction so we can delete it later

        // add 3rd transaction
        listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##10");
        listSectors = catDao.findByCode("321");
        listTypeOfFinance = catDao.findByCode("TYPE_OF_FINANCE##110");
        listTypeOfAid = catDao.findByCode("A");
        listBiMultilateral = catDao.findByCode("BI_MULTILATERAL##2");
        listAreas = areaDao.findAllAsList();
        channel = channelCatDao.findByCode("41301").getEntity();        // add a chanel with a coefficient (it should be 51% in this case)

        sector = listSectors.get(0);
        typeOfFlow = listTypeOfFlow.get(0);
        typeOfFinance = listTypeOfFinance.get(0);
        typeOfAid = listTypeOfAid.get(0);
        biMultilateral = listBiMultilateral.get(0);
        area = findAreaByName(listAreas, "Romania");

        ft = new CustomFinancialTransaction();

        ft.setDraft(false);
        ft.setChannel(channel);
        ft.setLocale("en");
        ft.setShortDescription( "Auditing test description 3");
        ft.setDescription("Long - " + ft.getDescription() );
        ft.setSector(sector);
        ft.setTypeOfFlow(typeOfFlow);
        ft.setTypeOfFinance(typeOfFinance);
        ft.setTypeOfAid(typeOfAid);
        ft.setReportingYear(LocalDateTime.parse((2013) + "-07-01"));
        ft.setFormType("bilateralOda.advanceQuestionnaire");
        ft.setBiMultilateral(biMultilateral);
        ft.setRecipient(area);

        ft.setAmountsExtended(BigMoney.parse("EUR " + 1000000));
        ft.setExtendingAgency(organization);

        txDao.save(ft);
        transactions.add(ft);           // save the transaction so we can delete it later

        // add non flow data - GNI: Gross National Income
        listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##40");
        listTypeOfFinance = catDao.findByCode("TYPE_OF_FINANCE##1");
        typeOfFlow = listTypeOfFlow.get(0);
        typeOfFinance = listTypeOfFinance.get(0);

        ft = new CustomFinancialTransaction();

        ft.setDraft(null);
        ft.setReportingYear(LocalDateTime.parse((2013) + "-07-01"));
        ft.setTypeOfFlow(typeOfFlow);
        ft.setTypeOfFinance(typeOfFinance);
        ft.setAmountsExtended(BigMoney.parse("EUR " + 60));

        txDao.save(ft);
        transactions.add(ft);           // save the transaction so we can delete it later

        // add non flow data - ODA % GNI
        listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##40");
        listTypeOfFinance = catDao.findByCode("TYPE_OF_FINANCE##2");
        typeOfFlow = listTypeOfFlow.get(0);
        typeOfFinance = listTypeOfFinance.get(0);

        ft = new CustomFinancialTransaction();

        ft.setDraft(null);
        ft.setReportingYear(LocalDateTime.parse((2013) + "-07-01"));
        ft.setTypeOfFlow(typeOfFlow);
        ft.setTypeOfFinance(typeOfFinance);
        ft.setAmountsExtended(BigMoney.parse("EUR " + 30));

        txDao.save(ft);
        transactions.add(ft);           // save the transaction so we can delete it later

        // add non flow data - Total flows % GNI
        listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##40");
        listTypeOfFinance = catDao.findByCode("TYPE_OF_FINANCE##3");
        typeOfFlow = listTypeOfFlow.get(0);
        typeOfFinance = listTypeOfFinance.get(0);

        ft = new CustomFinancialTransaction();

        ft.setDraft(null);
        ft.setReportingYear(LocalDateTime.parse((2013) + "-07-01"));
        ft.setTypeOfFlow(typeOfFlow);
        ft.setTypeOfFinance(typeOfFinance);
        ft.setAmountsExtended(BigMoney.parse("EUR " + 46));

        txDao.save(ft);
        transactions.add(ft);           // save the transaction so we can delete it later

        // add non flow data - Population
        listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##40");
        listTypeOfFinance = catDao.findByCode("TYPE_OF_FINANCE##4");
        typeOfFlow = listTypeOfFlow.get(0);
        typeOfFinance = listTypeOfFinance.get(0);

        ft = new CustomFinancialTransaction();

        ft.setDraft(null);
        ft.setReportingYear(LocalDateTime.parse((2013) + "-07-01"));
        ft.setTypeOfFlow(typeOfFlow);
        ft.setTypeOfFinance(typeOfFinance);
        ft.setAmountsExtended(BigMoney.parse("EUR " + 10000));

        txDao.save(ft);
        transactions.add(ft);           // save the transaction so we can delete it later
    }

    @After
    public void tearDown () {
        // remove the data from the database
        for (CustomFinancialTransaction ft : transactions) {
            txDao.delete(ft);
        }

        // remove organization
        orgDao.delete(organization);
    }

    @Test
    public void testConnection () {
        Assert.assertNotNull(txDao);
        Assert.assertNotNull(orgDao);

        Assert.assertEquals("Check number of transactions", 7, txDao.findAllAsList().size());
    }

    @Test
    public void testNetODATable () throws Exception {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

//        logger.info(txDao.findAllAsList());
        logger.info("txDao size: " + txDao.findAllAsList().size());

        Map<String, String> params = new HashMap<>();
        params.put("dataAccessId", "netODATable");

        QueryResult result = CdaService.doQuery(params);
        logger.info("getResultset: " + result.getResultset().toString());

        /*
        * From PopulateExchangeDbChange.java file we have the following exchange rates:
        * 2012 - 1.28
        * 2013 - 1.32
        */
        Assert.assertEquals("Check Net ODA Table query number of rows", "4", result.getQueryInfo().getTotalRows());
        Assert.assertEquals("Check Net ODA Table query response",
                "[[Curent (USD), 128000, 1993200], [National Currency, 100000, 1510000], [ODA/GNI, null, 30], [Bilateral share, 100, 50]]",
                result.getResultset().toString());


        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

//    @Test
//    public void testTopTenRecipientsTable () {
//
//    }
//
//    @Test
//    public void testTopTenMemoShareTAble () {
//
//    }
//
//    @Test
//    public void testOdaByRegionChart () {
//
//    }
//
//    @Test
//    public void testOdaByIncomeGroupChart () {
//
//    }
//
//    @Test
//    public void testOdaBySectorChart () {
//
//    }

    // test ODA/GNI/Population (non flow)

    private Area findAreaByName(List<Area> listAreas, String name) {
        for(Area area : listAreas) {
            if(area.getName().equals(name)) {
                return area;
            }
        }

        return null;
    }
}
