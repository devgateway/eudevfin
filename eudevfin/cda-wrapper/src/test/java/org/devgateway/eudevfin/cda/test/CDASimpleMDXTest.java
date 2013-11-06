package org.devgateway.eudevfin.cda.test;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.query.QueryOptions;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:META-INF/commonAuthContext.xml",
		"classpath:META-INF/financialContext.xml",
		"classpath:META-INF/cdaContext.xml"
		})
public class CDASimpleMDXTest
{
	
	protected static Logger logger = Logger.getLogger(CDASimpleMDXTest.class);


  @Test
  public void testSqlQuery() throws Exception
  {

	    // Define an outputStream
	    OutputStream out = System.out;

	    logger.info("Building CDA settings from sample file");

	    final SettingsManager settingsManager = SettingsManager.getInstance();
	    URL file = this.getClass().getResource("sample-mondrian-jndi.cda");
	    File settingsFile = new File(file.toURI());
	    final CdaSettings cdaSettings = settingsManager.parseSettingsFile(settingsFile.getAbsolutePath());
	    logger.debug("Doing query on Cda - Initializing CdaEngine");
	    final CdaEngine engine = CdaEngine.getInstance();

	    QueryOptions queryOptions = new QueryOptions();
	    queryOptions.setDataAccessId("2");
	    queryOptions.setOutputType("json");
	    queryOptions.addParameter("status", "Shipped");

	    logger.info("Doing query");
	    engine.doQuery(out, cdaSettings, queryOptions);

  
  }
  
}