package org.devgateway.eudevfin.cda;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.query.QueryOptions;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;

@Component
public class CDAQuery {
	
	protected static Logger logger = Logger.getLogger(CDAQuery.class);

	@ServiceActivator(inputChannel="getCDAQueryChannel", outputChannel="replyCDAQueryChannel")
	
	public OutputStream doQuery(String dataSourceId) throws Exception {

	    // Define an outputStream
	    OutputStream out = new ByteArrayOutputStream();

	    logger.info("Building CDA settings from sample file");

	    final SettingsManager settingsManager = SettingsManager.getInstance();
	    
	    URL file = this.getClass().getResource("service/financial.mondrian.cda");
	    File settingsFile = new File(file.toURI());
	    final CdaSettings cdaSettings = settingsManager.parseSettingsFile(settingsFile.getAbsolutePath());
	    logger.debug("Doing query on Cda - Initializing CdaEngine");
	    final CdaEngine engine = CdaEngine.getInstance();

	    QueryOptions queryOptions = new QueryOptions();
	    queryOptions.setDataAccessId(dataSourceId);
	    queryOptions.setOutputType("json");
	    //queryOptions.addParameter("status", "Shipped");

	    logger.info("Doing query");
	    engine.doQuery(out, cdaSettings, queryOptions);
	    
		return out;
	}

}
