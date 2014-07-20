/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
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
