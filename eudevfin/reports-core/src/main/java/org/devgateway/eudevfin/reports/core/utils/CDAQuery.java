package org.devgateway.eudevfin.reports.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.common.locale.LocaleHelper;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.query.QueryOptions;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cda.settings.SettingsManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

@Component
@Lazy(value=false)
public class CDAQuery implements ApplicationContextAware {

	public static final String FINANCIAL_MODRIAN_CDA_FILE="res://org/devgateway/eudevfin/reports/core/service/financial.mondrian.cda";
	
	protected static Logger logger = Logger.getLogger(CDAQuery.class);
	ApplicationContext applicationContext = null;

	/**
	 * Instantiates and issues a query to the CDA Engine and returns a POJO with the results.	
	 * @param params	Map with the configuration parameters used in the query as well as custom variables
	 * @return the QueryResult object with the metadata, column definition and rows	
	 * @throws Exception
	 */
	@ServiceActivator(inputChannel="getCDAQueryChannel", outputChannel="replyCDAQueryChannel")
	public QueryResult doQuery(Map<String,String> params) throws Exception { 
		
		// Define the variables that will hold the parameters for the query
		String dataAccessId = params.get("dataAccessId");
		Integer outputIndexId = (params.get("outputIndexId") != null) ? Integer.parseInt(params.get("outputIndexId")) : 1;
		Boolean paginate = Boolean.parseBoolean(params.get("paginateQuery"));
		Integer pageSize = (params.get("pageSize") != null) ? Integer.parseInt(params.get("pageSize")) : 0;
		Integer pageStart = (params.get("pageStart") != null) ? Integer.parseInt(params.get("pageStart")) : 0;
		String sortBy = (params.get("sortBy") != null) ? params.get("sortBy") : "";
	    ArrayList<String> sortByList = new ArrayList<String>();
		if(!sortBy.isEmpty()){
		    String[] myArray = sortBy.split(",");
		    Collections.addAll(sortByList, myArray);
		}
	    LocaleHelper locale = (LocaleHelper) applicationContext.getBean("localeHelperSession");
		String lang = (locale.getLocale() != null) ? locale.getLocale() : "en";
	    
	    // Stream that will hold the results of the CDA Query
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		// Settings object needed for the engine
	    final SettingsManager settingsManager = SettingsManager.getInstance();
	 


//	    File settingsFile = new File(file.toURI());
	    final CdaSettings cdaSettings = settingsManager.parseSettingsFile(FINANCIAL_MODRIAN_CDA_FILE);

		// Settings options based on the Map<String,String> params already processed
	    final QueryOptions queryOptions = new QueryOptions();
	    queryOptions.setDataAccessId(dataAccessId);
		queryOptions.setOutputIndexId(outputIndexId);
	    queryOptions.setPaginate(paginate);
	    queryOptions.setPageSize(pageSize);
	    queryOptions.setPageStart(pageStart);
	    if(sortByList.size() > 0)
	    	queryOptions.setSortBy(sortByList);
	    queryOptions.setOutputType("json");

	    org.springframework.context.i18n.LocaleContextHolder.setLocale(new Locale(lang));
	    
	    //Additional parameters come in the shape of "paramXXXXX"
	    for(Map.Entry<String, String> param : params.entrySet())
	    {
	    	String key = param.getKey();
	    	String value = param.getValue();
	    	if(key.startsWith("param")){
	    		String cdaKey = key.replace("param", "");
	    	    queryOptions.setParameter(cdaKey, value);
	    	}
	    }

	    final CdaEngine engine = CdaEngine.getInstance();
	    engine.doQuery(outStream, cdaSettings, queryOptions);
	    
	    //POJO that will hold the JSON result object
	    QueryResult result;
	    
	    if(outStream.size() > 0){
	    	//Jackson JSON processor to convert the string to the QueryResult object
	    	ObjectMapper mapper = new ObjectMapper(); 
	    	result = mapper.readValue(outStream.toString(), QueryResult.class);
	    }
	    else
	    {
	    	result = new QueryResult();
		}

		return result;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext = arg0;
	}

}
