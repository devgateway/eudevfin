package org.devgateway.eudevfin.cda.rest;

import java.util.Map;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.cda.domain.QueryResult;
import org.devgateway.eudevfin.cda.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@Controller
public class QueryController {
	@Autowired
	private QueryService queryService;

	@Autowired
	private View jsonView;

	private static final String DATA_FIELD = "data";
	private static final String ERROR_FIELD = "error";

	private static final Logger logger = Logger
			.getLogger(QueryController.class);

	@RequestMapping(value = "/cda/doQuery", method = RequestMethod.GET)
	public ModelAndView doQuery(
			@RequestParam Map<String,String> allRequestParams
		) {
		QueryResult result = null;
		System.out.println("Todos:" + allRequestParams.toString());

		if (isEmpty(allRequestParams.get("dataAccessId"))) {
			String sMessage = "Error invoking doQuery - Invalid datasource Id parameter";
			return createErrorResponse(sMessage);
		}

		try {
			result = queryService.doQuery(allRequestParams);
		} catch (Exception e) {
			String sMessage = "Error invoking doQuery. [%1$s]";
			return createErrorResponse(String.format(sMessage, e.toString()));
		}

		logger.debug("Returing result: " + result.toString());
		return new ModelAndView(jsonView, DATA_FIELD, result);
	}

	private ModelAndView createErrorResponse(String sMessage) {
		return new ModelAndView(jsonView, ERROR_FIELD, sMessage);
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
