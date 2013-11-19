package org.devgateway.eudevfin.cda.service;

import org.devgateway.eudevfin.cda.domain.QueryResult;
import org.springframework.integration.annotation.Gateway;

public interface QueryService {

	@Gateway(requestChannel="getCDAQueryChannel",replyChannel="replyCDAQueryChannel")
	QueryResult doQuery(String dataAccessId);

}
