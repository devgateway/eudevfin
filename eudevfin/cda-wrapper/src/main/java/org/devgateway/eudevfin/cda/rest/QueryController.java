package org.devgateway.eudevfin.cda.rest;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.cda.domain.QueryResult;
import org.devgateway.eudevfin.cda.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@Controller
public class QueryController {
	@Autowired
	private QueryService service;

	@Autowired
	private View jsonView;

	private static final String DATA_FIELD = "data";
	private static final String ERROR_FIELD = "error";

	private static final Logger logger = Logger
			.getLogger(QueryController.class);

	@RequestMapping(value = "/rest/doQuery/{dataSourceId}", method = RequestMethod.GET)
	public ModelAndView doQuery(
			@PathVariable("dataSourceId") String dataSourceId) {
		QueryResult result = null;

		if (isEmpty(dataSourceId) || dataSourceId.length() < 5) {
			String sMessage = "Error invoking doQuery - Invalid datasource Id parameter";
			return createErrorResponse(sMessage);
		}

		try {
			result = service.doQuery(dataSourceId);
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
}
