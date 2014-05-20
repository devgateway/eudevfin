package org.devgateway.eudevfin.reports.core.service;

import java.util.Map;

import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.springframework.integration.annotation.Gateway;

public interface QueryService {
	/**
	 * Instantiates and issues a query to the CDA Engine and returns a POJO with the results.	
	 * @param params	Map with the configuration parameters used in the query as well as custom variables
	 * @return the QueryResult object with the metadata, column definition and rows	
	 * @throws Exception
	 */
	@Gateway(requestChannel="getCDAQueryChannel",replyChannel="replyCDAQueryChannel")
	QueryResult doQuery(Map<String, String> params);//, int pageSize, int pageStart, String sortBy;

}
