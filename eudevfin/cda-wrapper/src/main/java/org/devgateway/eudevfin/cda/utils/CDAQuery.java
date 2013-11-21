package org.devgateway.eudevfin.cda.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.cda.domain.QueryResult;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.query.QueryOptions;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;

@Component
public class CDAQuery {
	
	protected static Logger logger = Logger.getLogger(CDAQuery.class);

	@ServiceActivator(inputChannel="getCDAQueryChannel", outputChannel="replyCDAQueryChannel")
	
	public QueryResult doQuery(Map<String,String> params) throws Exception { //, int pageSize, int pageStart, String sortBy
							  
	    // Define an outputStream
		ByteArrayOutputStream out = new ByteArrayOutputStream();

	    final SettingsManager settingsManager = SettingsManager.getInstance();
	    
	    URL file = this.getClass().getResource("../service/financial.mondrian.cda");
	    File settingsFile = new File(file.toURI());
	    final CdaSettings cdaSettings = settingsManager.parseSettingsFile(settingsFile.getAbsolutePath());
	    final CdaEngine engine = CdaEngine.getInstance();

	    QueryOptions queryOptions = new QueryOptions();
	    queryOptions.setDataAccessId(params.get("dataAccessId"));
	    queryOptions.setPaginate(Boolean.parseBoolean(params.get("paginateQuery")));
/*	    queryOptions.setPageSize(pageSize);

	    ArrayList<String> sortByList = new ArrayList<String>();
	    String[] myArray = sortBy.split(",");
	    Collections.addAll(sortByList, myArray);

	    queryOptions.setSortBy(sortByList);*/
	    queryOptions.setOutputType("json");
	    
	    engine.doQuery(out, cdaSettings, queryOptions);
	    
	    QueryResult result;
	    
	    if(out.size() > 0){
	    	ObjectMapper mapper = new ObjectMapper(); 
	    	result = mapper.readValue(out.toString(), QueryResult.class);
	    }
	    else
	    {
	    	result = new QueryResult();
	    }
	    
		return result;
	}

}
