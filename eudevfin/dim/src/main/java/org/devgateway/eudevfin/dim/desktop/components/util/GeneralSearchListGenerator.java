/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components.util;

import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.devgateway.eudevfin.ui.common.forms.SearchBoxPanelForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.annotation.Header;

/**
 * @author Alex
 *
 */
public class GeneralSearchListGenerator implements ListGeneratorInterface<FinancialTransaction> {

	private static final long serialVersionUID = 2851842899904434912L;
	private SearchBoxPanelForm searchBoxPanelForm;
	private FinancialTransactionService txService;
	
	
	public GeneralSearchListGenerator(
			FinancialTransactionService txService) {
		super();		
		this.txService = txService;
	}




	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.dim.desktop.components.config.ListGeneratorInterface#getResultsList(int, int)
	 */
	@Override
	public PagingHelper<FinancialTransaction> getResultsList(int pageNumber,
			int pageSize) {
		if (searchBoxPanelForm!=null && searchBoxPanelForm.getSearchString()!= null && searchBoxPanelForm.getSearchString().length() > 1 )
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
