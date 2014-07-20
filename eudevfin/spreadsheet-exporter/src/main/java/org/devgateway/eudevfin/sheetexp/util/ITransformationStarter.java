/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
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