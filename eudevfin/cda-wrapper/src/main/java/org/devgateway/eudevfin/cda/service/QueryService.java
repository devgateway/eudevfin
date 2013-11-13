package org.devgateway.eudevfin.cda.service;

import java.io.OutputStream;

import org.springframework.integration.annotation.Gateway;

public interface QueryService {

	@Gateway(requestChannel="getCDAQueryChannel",replyChannel="replyCDAQueryChannel")
	OutputStream doQuery(String dataAccessId);

}
