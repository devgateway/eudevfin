package org.devgateway.eudevfin.cda.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.cda.domain.QueryResult;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.query.QueryOptions;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;

@Component
public class CDAQuery {
	
	protected static Logger logger = Logger.getLogger(CDAQuery.class);

	@ServiceActivator(inputChannel="getCDAQueryChannel", outputChannel="replyCDAQueryChannel")
	
	public QueryResult doQuery(String dataSourceId) throws Exception {

	    // Define an outputStream
		ByteArrayOutputStream out = new ByteArrayOutputStream();

	    final SettingsManager settingsManager = SettingsManager.getInstance();
	    
	    URL file = this.getClass().getResource("../service/financial.mondrian.cda");
	    File settingsFile = new File(file.toURI());
	    final CdaSettings cdaSettings = settingsManager.parseSettingsFile(settingsFile.getAbsolutePath());
	    final CdaEngine engine = CdaEngine.getInstance();

	    QueryOptions queryOptions = new QueryOptions();
	    queryOptions.setDataAccessId(dataSourceId);
	    queryOptions.setOutputType("json");
	    //TODO: Support parameters/Pagination
	    
	    engine.doQuery(out, cdaSettings, queryOptions);
	    
	    QueryResult result;
	    
	    if(out.size() > 0){
	    	Gson json = new Gson();
	    	result = json.fromJson(out.toString(), QueryResult.class);
	    }
	    else
	    {
	    	result = new QueryResult();
	    }
	    
		return result;
	}

}
