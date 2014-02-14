package org.devgateway.eudevfin.cda.rest;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.cda.domain.QueryResult;
import org.devgateway.eudevfin.cda.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

import java.util.Map;

@Controller
public class QueryController {
	@Autowired
	private QueryService queryService;

	@Autowired
	@Qualifier("jsonView")
	private View jsonView;

	private static final Logger logger = Logger
			.getLogger(QueryController.class);

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
	
	public void setJsonView(View view) {
		jsonView = view;
	}
	
}
