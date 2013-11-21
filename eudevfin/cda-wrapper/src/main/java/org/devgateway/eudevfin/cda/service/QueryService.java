package org.devgateway.eudevfin.cda.service;

import java.util.Map;

import org.devgateway.eudevfin.cda.domain.QueryResult;
import org.springframework.integration.annotation.Gateway;

public interface QueryService {

	@Gateway(requestChannel="getCDAQueryChannel",replyChannel="replyCDAQueryChannel")
	QueryResult doQuery(Map<String, String> params);//, int pageSize, int pageStart, String sortBy;

}
