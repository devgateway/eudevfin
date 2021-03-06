/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components.util;

import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.springframework.data.domain.PageRequest;

/**
 * @author Alex
 *
 */
public class SectorListGenerator implements ListGeneratorInterface<FinancialTransaction> {

	private String sectorCode;
	private FinancialTransactionService txService;
	
	

	public SectorListGenerator(String sectorCode,
			FinancialTransactionService txService) {
		super();
		this.sectorCode = sectorCode;
		this.txService = txService;
	}



	@Override
	public PagingHelper<FinancialTransaction> getResultsList(int pageNumber, int pageSize) {
		return PagingHelper.createPagingHelperFromPage(this.txService.findBySectorCode(this.sectorCode, new PageRequest(pageNumber-1,pageSize)));
	}



	public String getSectorCode() {
		return sectorCode;
	}



	public void setSectorCode(String sectorCode) {
		this.sectorCode = sectorCode;
	}
	
	

}
