package org.devgateway.eudevfin.cda.test;

import org.apache.log4j.Logger;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.query.QueryOptions;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;

import java.io.File;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

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
        "classpath:META-INF/cdaContext.xml",
        "classpath:testCDAContext.xml"
})
// we need this so that Spring can start a new transaction surrounding the test methods and @Before/@After callbacks,
// rolling back at the end
@Transactional
public class OdaAtGlanceTest {
    protected static Logger logger = Logger.getLogger(OdaAtGlanceTest.class);

    public static final String TEST_LOCALE = "en";

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

        // create the database structure
        List<Category> listTypeOfFlow = catDao.findByCode("TYPE_OF_FLOW##10");
        List<Category> listSectors = catDao.findByCode("321");
        List<Category> listTypeOfFinance = catDao.findByCode("TYPE_OF_FINANCE##110");
        List<Category> listTypeofAid = catDao.findByCode("A");
        List<Category> listBiMultilateral = catDao.findByCode("BI_MULTILATERAL##1");
        List<Area> listAreas = areaDao.findAllAsList();
        ChannelCategory channel = channelCatDao.findByCode("21001").getEntity();

        Category sector = listSectors.get(0);
        Category typeOfFlow = listTypeOfFlow.get(0);
        Category typeOfFinance = listTypeOfFinance.get(0);
        Category typeOfAid = listTypeofAid.get(0);
        Category biMultilateral = listBiMultilateral.get(0);
        Area area = findAreaByName(listAreas, "Moldova");

        logger.info(typeOfFlow);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(sector);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(typeOfFinance);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(typeOfAid);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(biMultilateral);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(area);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(channel);

        Organization org = new Organization();
        org.setName("Testing auditing Org");
        orgDao.save(org);

        CustomFinancialTransaction ft = new CustomFinancialTransaction();

        ft.setDraft(false);
        ft.setChannel(channel);
        ft.setLocale("en");
        ft.setShortDescription( "Auditing test description");
        ft.setDescription("Long - " + ft.getDescription() );
        ft.setLocale("ro");
        ft.setShortDescription( "CDA Test Transaction " + " ro" );
        ft.setDescription("Long" + ft.getDescription() );
        ft.setSector(sector);
        ft.setTypeOfFlow(typeOfFlow);
        ft.setTypeOfFinance(typeOfFinance);
        ft.setTypeOfAid(typeOfAid);
        ft.setReportingYear(LocalDateTime.parse((2013) + "-07-01"));
        ft.setFormType("bilateralOda.advanceQuestionnaire");
        ft.setBiMultilateral(biMultilateral);
        ft.setRecipient(area);

        ft.setAmountsExtended(BigMoney.parse("EUR " + 1000));
        ft.setExtendingAgency(org);

        txDao.save(ft);
    }

    @Test
    public void testNetODATable () throws Exception {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Assert.assertNotNull(txDao);

        logger.info(txDao.findAllAsList());
        logger.info("size: " + txDao.findAllAsList().size());

//        Map<String, String> params = new HashMap<>();
//        params.put("dataAccessId", "netODATable");
//
//        QueryResult result = CdaService.doQuery(params);
//        logger.info("Result: " + result.toString());




        // Define an outputStream
        OutputStream out = System.out;

        final SettingsManager settingsManager = SettingsManager.getInstance();

        URL file = this.getClass().getResource("../service/financial.mondrian.cda");
        File settingsFile = new File(file.toURI());
        final CdaSettings cdaSettings = settingsManager.parseSettingsFile(settingsFile.getAbsolutePath());
        logger.debug("Doing query on Cda - Initializing CdaEngine");
        final CdaEngine engine = CdaEngine.getInstance();

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setDataAccessId("netODATable");
        queryOptions.setOutputType("json");

        logger.info("Doing query");
        engine.doQuery(out, cdaSettings, queryOptions);




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

    // test ODA/GNI/Population - daca le salvezi de 2 ori sunt doar 1 tranzactie

    private Area findAreaByName(List<Area> listAreas, String name) {
        for(Area area : listAreas) {
            if(area.getName().equals(name)) {
                return area;
            }
        }

        return null;
    }
}
