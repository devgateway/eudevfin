package org.devgateway.eudevfin.reports.core.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.dataaccess.DataAccessEnums;
import pt.webdetails.cda.query.QueryOptions;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;

/**
 * Provides testing of a simple MDX query through Mondrian JNDI. 
 * This spawns a Mondrian MDX server over a provided JNDI connection.
 * The JNDI connection is exposed in financialContext.xml through Spring and {@link SimpleNamingContextBuilder}
 * @see {@link DataAccessEnums.ConnectionInstanceType#MONDRIAN_JNDI}
 * @throws Exception
 *
 * @author mihai
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
        "classpath:META-INF/cdaContext.xml"
})
public class CDASimpleMDXTest {
    protected static Logger logger = Logger.getLogger(CDASimpleMDXTest.class);


    @Test
    public void testMdxQuery() throws Exception {
        // Define an outputStream
        OutputStream out = System.out;

        final SettingsManager settingsManager = SettingsManager.getInstance();

        URL file = this.getClass().getResource("../service/financial.mondrian.cda");
        File settingsFile = new File(file.toURI());
        final CdaSettings cdaSettings = settingsManager.parseSettingsFile(settingsFile.getAbsolutePath());
        logger.debug("Doing query on Cda - Initializing CdaEngine");
        final CdaEngine engine = CdaEngine.getInstance();

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setDataAccessId("simpleMDXQuery");
        queryOptions.setOutputType("json");

        logger.info("Doing query");
        engine.doQuery(out, cdaSettings, queryOptions);
    }
}