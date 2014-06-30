package org.devgateway.eudevfin.sheetexp.util;

import javax.servlet.http.HttpServletResponse;

import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.sheetexp.integration.api.SpreadsheetTransformerService;
import org.devgateway.eudevfin.sheetexp.ui.model.Filter;

public interface ITransformationStarter {

	public ITransformationStarter prepareTransformation(Filter filter,
			CustomFinancialTransactionService txService);

	public void executeTransformation(HttpServletResponse response,
			SpreadsheetTransformerService transformerService);

}