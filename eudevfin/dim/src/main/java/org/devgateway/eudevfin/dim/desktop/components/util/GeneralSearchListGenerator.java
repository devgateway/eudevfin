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
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.devgateway.eudevfin.ui.common.forms.SearchBoxPanelForm;
import org.springframework.data.domain.PageRequest;

/**
 * @author Alex
 *
 */
public class GeneralSearchListGenerator implements ListGeneratorInterface<FinancialTransaction> {

	private static final long serialVersionUID = 2851842899904434912L;
	private SearchBoxPanelForm searchBoxPanelForm;
	private CustomFinancialTransactionService txService;
	
	
	public GeneralSearchListGenerator(
			CustomFinancialTransactionService txService) {
		super();		
		this.txService = txService;
	}




	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.dim.desktop.components.config.ListGeneratorInterface#getResultsList(int, int)
	 */
	@Override
	public PagingHelper<FinancialTransaction> getResultsList(int pageNumber,
			int pageSize) {
		if (searchBoxPanelForm!=null )
			return PagingHelper.createPagingHelperFromPage(this.txService.findBySearchFormPageable(
					searchBoxPanelForm.getYear(),
					searchBoxPanelForm.getSector(),
					searchBoxPanelForm.getRecipient(),					
					searchBoxPanelForm.getSearchString(),					
					searchBoxPanelForm.getFormType(),
					searchBoxPanelForm.getExtendingAgency(),
					null, new PageRequest(pageNumber-1, pageSize)));
		else
			return null;
	}
	
	


	/**
	 * @return the searchBoxPanelForm
	 */
	public SearchBoxPanelForm getSearchBoxPanelForm() {
		return searchBoxPanelForm;
	}




	/**
	 * @param searchBoxPanelForm the searchBoxPanelForm to set
	 */
	public void setSearchBoxPanelForm(SearchBoxPanelForm searchBoxPanelForm) {
		this.searchBoxPanelForm = searchBoxPanelForm;
	}


	

}
