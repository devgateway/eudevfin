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
import org.junit.Ignore;
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

        /* **************************** add 1st transaction **************************** */
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

        /* **************************** add 2nd transaction **************************** */
        listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##10");
        listSectors = catDao.findByCode("322");
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
        area = findAreaByName(listAreas, "Tunisia");

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

        /* **************************** add 3rd transaction **************************** */
        listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##10");
        listSectors = catDao.findByCode("112");
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
        area = findAreaByName(listAreas, "Turkey");

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

        /* **************************** add 4th transaction that is not ODA **************************** */
        listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##30");
        listSectors = catDao.findByCode("321");
        listTypeOfFinance = catDao.findByCode("TYPE_OF_FINANCE##110");
        listTypeOfAid = catDao.findByCode("A");
        listBiMultilateral = catDao.findByCode("BI_MULTILATERAL##2");
        channel = channelCatDao.findByCode("41301").getEntity();        // add a chanel with a coefficient (it should be 51% in this case)

        sector = listSectors.get(0);
        typeOfFlow = listTypeOfFlow.get(0);
        typeOfFinance = listTypeOfFinance.get(0);
        typeOfAid = listTypeOfAid.get(0);
        biMultilateral = listBiMultilateral.get(0);

        ft = new CustomFinancialTransaction();

        ft.setDraft(false);
        ft.setChannel(channel);
        ft.setLocale("en");
        ft.setShortDescription( "Auditing test description 4");
        ft.setDescription("Long - " + ft.getDescription() );
        ft.setSector(sector);
        ft.setTypeOfFlow(typeOfFlow);
        ft.setTypeOfFinance(typeOfFinance);
        ft.setTypeOfAid(typeOfAid);
        ft.setReportingYear(LocalDateTime.parse((2013) + "-07-01"));
        ft.setFormType("bilateralOda.advanceQuestionnaire");
        ft.setBiMultilateral(biMultilateral);
        ft.setRecipient(null);

        ft.setAmountsExtended(BigMoney.parse("EUR " + 1000000));
        ft.setExtendingAgency(organization);

        txDao.save(ft);
        transactions.add(ft);           // save the transaction so we can delete it later

        /* **************************** add non flow data - GNI: Gross National Income **************************** */
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

        /* **************************** add non flow data - ODA % GNI **************************** */
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

        /* **************************** add non flow data - Total flows % GNI **************************** */
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

        /* **************************** add non flow data - Population **************************** */
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

        Assert.assertEquals("Check number of transactions", 8, txDao.findAllAsList().size());
    }

    @Test
    public void testNetODATable () throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("dataAccessId", "netODATable");

        QueryResult result = CdaService.doQuery(params);

        /*
        * From PopulateExchangeDbChange.java file we have the following exchange rates:
        * 2012 - 1.28
        * 2013 - 1.32
        */
        Assert.assertEquals("Check Net ODA Table query number of rows", "4", result.getQueryInfo().getTotalRows());
        Assert.assertEquals("Check Net ODA Table query response",
                "[[Curent (USD), 128000, 1993200], [National Currency, 100000, 1510000], [ODA/GNI, null, 30], [Bilateral share, 100, 50]]",
                result.getResultset().toString());
    }

    @Test
    public void testTopTenRecipientsTable () {
        Map<String, String> params = new HashMap<>();
        params.put("dataAccessId", "topTenRecipients");

        QueryResult result = CdaService.doQuery(params);

        // there are 3 transactions with 3 different recipient countries: Tunisia, Turkey, Moldova
        Assert.assertEquals("Check Top Ten Recipients query number of rows", "3", result.getQueryInfo().getTotalRows());
        Assert.assertEquals("Check Top Ten Recipients query response",
                "[[Tunisia, 1320000], [Turkey, 673200], [Moldova, 128000]]",
                result.getResultset().toString());
    }

    @Test
    public void testTopTenMemoShareTable () {
        Map<String, String> params = new HashMap<>();
        params.put("dataAccessId", "topTenMemoShare");

        QueryResult result = CdaService.doQuery(params);

        Assert.assertEquals("Check Top Ten Memo Share query number of rows", "1", result.getQueryInfo().getTotalRows());
        Assert.assertEquals("Check Top Ten Memo Share query response",
                "[[100]]",
                result.getResultset().toString());
    }

    @Ignore("Not ready yet. We need to find a relation between country and regions")
    @Test
    public void testOdaByRegionChart () {
        Map<String, String> params = new HashMap<>();
        params.put("dataAccessId", "odaByRegionChart");

        QueryResult result = CdaService.doQuery(params);

        Assert.assertEquals("Check Oda By Region query number of rows", "1", result.getQueryInfo().getTotalRows());
        Assert.assertEquals("Check Oda By Region query response",
                "[[something]]",
                result.getResultset().toString());
    }

    @Test
    public void testOdaByIncomeGroupChart () {
        Map<String, String> params = new HashMap<>();
        params.put("dataAccessId", "odaByIncomeGroupChart");

        QueryResult result = CdaService.doQuery(params);

        Assert.assertEquals("Check Oda By Income Group query number of rows", "2", result.getQueryInfo().getTotalRows());
        // Tunisia and Turkey are in UMICs and Moldova in LMICs
        Assert.assertEquals("Check Oda By Income Group query response",
                "[[UMICs, 1993200], [LMICs, 128000]]",
                result.getResultset().toString());
    }

    @Test
    public void testOdaBySectorChart () {
        Map<String, String> params = new HashMap<>();
        params.put("dataAccessId", "odaBySectorChart");

        QueryResult result = CdaService.doQuery(params);

        // we used 3 different sectors
        Assert.assertEquals("Check Oda By Sector query number of rows", "3", result.getQueryInfo().getTotalRows());
        Assert.assertEquals("Check Oda By Sector query response",
                "[[Basic education, ODA (Official Development Assistance), 673200], [INDUSTRY, ODA (Official Development Assistance), 128000], [MINERAL RESOURCES AND MINING, ODA (Official Development Assistance), 1320000]]",
                result.getResultset().toString());
    }

    @Test
    public void testNonFlowData () {
        Map<String, String> params = new HashMap<>();
        params.put("dataAccessId", "nonFlowData");

        QueryResult result = CdaService.doQuery(params);

        Assert.assertEquals("Check Non Flow Data query number of rows", "4", result.getQueryInfo().getTotalRows());
        Assert.assertEquals("Check Non Flow Data query response",
                "[[GNI: Gross National Income, 60], [ODA % GNI, 30], [Total flows % GNI, 46], [Population, 10000]]",
                result.getResultset().toString());
    }

    private Area findAreaByName(List<Area> listAreas, String name) {
        for(Area area : listAreas) {
            if(area.getName().equals(name)) {
                return area;
            }
        }

        return null;
    }
}
