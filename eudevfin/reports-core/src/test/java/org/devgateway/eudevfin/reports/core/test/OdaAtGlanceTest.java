package org.devgateway.eudevfin.reports.core.test;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.locale.LocaleHelperInterface;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.dao.AreaDaoImpl;
import org.devgateway.eudevfin.financial.dao.CategoryDaoImpl;
import org.devgateway.eudevfin.financial.dao.ChannelCategoryDao;
import org.devgateway.eudevfin.financial.dao.FinancialTransactionDaoImpl;
import org.devgateway.eudevfin.financial.dao.OrganizationDaoImpl;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.devgateway.eudevfin.reports.core.service.QueryService;
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
    protected List<FinancialTransaction> oldTransactions;
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

        // keep old transactions if there are any
        oldTransactions = new ArrayList<>();
        for(FinancialTransaction tx : txDao.findAllAsList()) {
            oldTransactions.add(tx);
            txDao.delete(tx);
        }

        transactions = new ArrayList<>();

        // create the database structure
        organization = new Organization();
        organization.setName("Testing auditing Org");
        orgDao.save(organization);

        /* **************************** add 1st transaction **************************** */
        CustomFinancialTransaction tx1 = createMockup(false, "2012", "bilateralOda.advanceQuestionnaire", "TYPE_OF_FLOW##10", "TYPE_OF_FINANCE##110",
                "A", "321", "21001", "BI_MULTILATERAL##1", "Moldova", "en", "Auditing test description 1", "EUR 100000", "EUR 0", organization);
        txDao.save(tx1);
        transactions.add(tx1);          // save the transaction so we can delete it later

        /* **************************** add 2nd transaction **************************** */
        CustomFinancialTransaction tx2 = createMockup(false, "2013", "bilateralOda.advanceQuestionnaire", "TYPE_OF_FLOW##10", "TYPE_OF_FINANCE##110",
                "A", "322", "21001", "BI_MULTILATERAL##1", "Tunisia", "en", "Auditing test description 2", "EUR 1000000", "EUR 0", organization);
        txDao.save(tx2);
        transactions.add(tx2);          // save the transaction so we can delete it later

        /* **************************** add 3rd transaction **************************** */
        // add a chanel with a coefficient (it should be 51% in this case)
        CustomFinancialTransaction tx3 = createMockup(false, "2013", "bilateralOda.advanceQuestionnaire", "TYPE_OF_FLOW##10", "TYPE_OF_FINANCE##110",
                "A", "112", "41301", "BI_MULTILATERAL##2", "Turkey", "en", "Auditing test description 3", "EUR 1000000", "EUR 0", organization);
        txDao.save(tx3);
        transactions.add(tx3);          // save the transaction so we can delete it later

        /* **************************** add 4th transaction that is not ODA **************************** */
        CustomFinancialTransaction tx4 = createMockup(false, "2013", "bilateralOda.advanceQuestionnaire", "TYPE_OF_FLOW##30", "TYPE_OF_FINANCE##110",
                "A", "321", "41301", "BI_MULTILATERAL##2", "Turkey", "en", "Auditing test description 4", "EUR 1000000", "EUR 0", organization);
        txDao.save(tx4);
        transactions.add(tx4);          // save the transaction so we can delete it later

        /* **************************** add non flow data - GNI: Gross National Income **************************** */
        CustomFinancialTransaction tx5 = createMockup(null, "2013", null, "TYPE_OF_FLOW##40", "TYPE_OF_FINANCE##1",
                null, null, null, null, "", "en", "GNI: Gross National Income", "EUR 60", "EUR 0", organization);
        txDao.save(tx5);
        transactions.add(tx5);          // save the transaction so we can delete it later

        /* **************************** add non flow data - ODA % GNI **************************** */
        CustomFinancialTransaction tx6 = createMockup(null, "2013", null, "TYPE_OF_FLOW##40", "TYPE_OF_FINANCE##2",
                null, null, null, null, "", "en", "ODA % GNI", "EUR 30", "EUR 0", organization);
        txDao.save(tx6);
        transactions.add(tx6);          // save the transaction so we can delete it later

        /* **************************** add non flow data - Total flows % GNI **************************** */
        CustomFinancialTransaction tx7 = createMockup(null, "2013", null, "TYPE_OF_FLOW##40", "TYPE_OF_FINANCE##3",
                null, null, null, null, "", "en", "Total flows % GNI", "EUR 46", "EUR 0", organization);
        txDao.save(tx7);
        transactions.add(tx7);          // save the transaction so we can delete it later

        /* **************************** add non flow data - Population **************************** */
        CustomFinancialTransaction tx8 = createMockup(null, "2013", null, "TYPE_OF_FLOW##40", "TYPE_OF_FINANCE##4",
                null, null, null, null, "", "en", "Population", "EUR 10000", "EUR 0", organization);
        txDao.save(tx8);
        transactions.add(tx8);          // save the transaction so we can delete it later
    }

    @After
    public void tearDown () {
        // remove the data from the database
        for (CustomFinancialTransaction ft : transactions) {
            txDao.delete(ft);
        }

        // remove organization
        orgDao.delete(organization);

        // save again the pre-existing transactions
        for(FinancialTransaction tx : oldTransactions) {
            txDao.save(tx);
        }
    }

    @Test
    public void testConnection () {
        Assert.assertNotNull(txDao);
        Assert.assertNotNull(orgDao);

        logger.info("Check number of transactions: "  + txDao.findAllAsList().size() + ", should be: 8");
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
        logger.info("Check Net ODA Table query number of rows: "  + result.getQueryInfo().getTotalRows() + ", should be: 4");
        logger.info("Check Net ODA Table query response: "  + result.getResultset().toString() + ", should be: [[Curent (USD), 128000, 1993200], [National Currency, 100000, 1510000], [ODA/GNI, null, 30], [Bilateral share, 100, 50]]");
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

        logger.info("Check Top Ten Recipients query number of rows: "  + result.getQueryInfo().getTotalRows() + ", should be: 3");
        logger.info("Check Top Ten Recipients query response: "  + result.getResultset().toString() + ", should be: [[Tunisia, 1320000], [Turkey, 673200], [Moldova, 128000]]");
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

        logger.info("Check Top Ten Memo Share query number of rows: "  + result.getQueryInfo().getTotalRows() + ", should be: 1");
        logger.info("Check Top Ten Memo Share query response: "  + result.getResultset().toString() + ", should be: [[100]]");
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

        logger.info("Check Oda By Region query number of rows: "  + result.getQueryInfo().getTotalRows() + ", should be: 1");
        logger.info("Check Oda By Region query response: "  + result.getResultset().toString() + ", should be: [[something]]");
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

        logger.info("Check Oda By Income Group query number of rows: "  + result.getQueryInfo().getTotalRows() + ", should be: 2");
        logger.info("Check Oda By Income Group query response: "  + result.getResultset().toString() + ", should be: [[UMICs, 1993200], [LMICs, 128000]]");
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

        logger.info("Check Oda By Sector query number of rows: "  + result.getQueryInfo().getTotalRows() + ", should be: 3");
        logger.info("Check Oda By Sector query response: "  + result.getResultset().toString() + ", should be: [[Basic education, ODA (Official Development Assistance), 673200], [INDUSTRY, ODA (Official Development Assistance), 128000], [MINERAL RESOURCES AND MINING, ODA (Official Development Assistance), 1320000]]");
        // we used 3 different sectors
        Assert.assertEquals("Check Oda By Sector query number of rows", "3", result.getQueryInfo().getTotalRows());
        Assert.assertEquals("Check Oda By Sector query response",
                "[[MINERAL RESOURCES AND MINING, ODA (Official Development Assistance), 1320000], [Basic education, ODA (Official Development Assistance), 673200], [INDUSTRY, ODA (Official Development Assistance), 128000]]",
                result.getResultset().toString());
    }

    @Test
    public void testNonFlowData () {
        Map<String, String> params = new HashMap<>();
        params.put("dataAccessId", "nonFlowData");

        QueryResult result = CdaService.doQuery(params);

        logger.info("Check Non Flow Data query number of rows: "  + result.getQueryInfo().getTotalRows() + ", should be: 4");
        logger.info("Check Non Flow Data query response: "  + result.getResultset().toString() + ", should be: [[GNI: Gross National Income, 60], [ODA % GNI, 30], [Total flows % GNI, 46], [Population, 10000]]");
        Assert.assertEquals("Check Non Flow Data query number of rows", "4", result.getQueryInfo().getTotalRows());
        Assert.assertEquals("Check Non Flow Data query response",
                "[[GNI: Gross National Income, 60], [ODA % GNI, 30], [Total flows % GNI, 46], [Population, 10000]]",
                result.getResultset().toString());
    }

    private CustomFinancialTransaction createMockup(Boolean draft, String year, String formType, String typeOfFlow, String typeOfFinance,
                                                    String typeOfAid, String sector, String channel, String biMultilateral, String area,
                                                    String locale, String description, String extendedAmount, String receivedAmount,
                                                    Organization extendingOrg) {
        CustomFinancialTransaction ft = new CustomFinancialTransaction();

        ft.setDraft(draft); // true | false | null
        ft.setCreatedBy("admin");
        ft.setModifiedBy("admin");
        ft.setReportingYear(LocalDateTime.parse((year) + "-07-01"));                                    // 2013
        ft.setFormType(formType);                                                                       // bilateralOda.advanceQuestionnaire
        ft.setTypeOfFlow(typeOfFlow == null ? null : catDao.findByCode(typeOfFlow).get(0));             // TYPE_OF_FLOW##10
        ft.setTypeOfFinance(typeOfFinance == null ? null : catDao.findByCode(typeOfFinance).get(0));    // TYPE_OF_FINANCE##110
        ft.setTypeOfAid(typeOfAid == null ? null : catDao.findByCode(typeOfAid).get(0));                // A
        ft.setSector(sector == null ? null : catDao.findByCode(sector).get(0));                         // 321
        ft.setChannel(channel == null ? null : (channelCatDao.findByCode(channel).getEntity()));        // 21001
        ft.setBiMultilateral(biMultilateral == null ? null : catDao.findByCode(biMultilateral).get(0)); // BI_MULTILATERAL##1
        ft.setRecipient(findAreaByName(area == null ? null : areaDao.findAllAsList(), area));           // Moldova
        ft.setLocale(locale);                                                                           // en
        ft.setShortDescription(description);                                                            // Auditing test description 1
        ft.setDescription("Long - " + ft.getDescription() );
        ft.setAmountsExtended(BigMoney.parse(extendedAmount));                                          // EUR 100000
        ft.setAmountsReceived(BigMoney.parse(receivedAmount));                                          // EUR 0
        ft.setExtendingAgency(extendingOrg);                                                            // organization

        return ft;
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
