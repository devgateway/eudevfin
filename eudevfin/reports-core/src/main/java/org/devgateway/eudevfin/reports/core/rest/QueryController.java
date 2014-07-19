/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.reports.core.rest;

import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class QueryController {
	@Autowired
	private QueryService queryService;

    @PreAuthorize("hasRole('" + AuthConstants.Roles.ROLE_USER + "')")
	@RequestMapping(value = "/doQuery", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody
	QueryResult doQuery(
			@RequestParam Map<String,String> allRequestParams
		) {
		QueryResult result = null;

		if (isEmpty(allRequestParams.get("dataAccessId"))) {
			String sMessage = "Error invoking doQuery - Invalid datasource Id parameter";
			QueryResult errorResult = new QueryResult();
			errorResult.setAdditionalProperties("error", sMessage);
			return errorResult;
		}

		try {
			result = queryService.doQuery(allRequestParams);
		} catch (Exception e) {
			String sMessage = "Error invoking doQuery.";
			QueryResult errorResult = new QueryResult();
			errorResult.setAdditionalProperties("error", sMessage);
			return errorResult;
		}
		return result;
	}

	public static boolean isEmpty(String s_p) {
		return (null == s_p) || s_p.trim().length() == 0;
	}

	public void setFundService(QueryService service) {
		queryService = service;
	}
}
