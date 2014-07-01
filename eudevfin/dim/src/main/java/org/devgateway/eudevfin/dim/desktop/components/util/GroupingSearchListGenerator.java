/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components.util;

import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.devgateway.eudevfin.ui.common.forms.GroupingBoxPanelForm;
import org.springframework.data.domain.PageRequest;

/**
 * @author mihai
 *
 */
public class GroupingSearchListGenerator implements ListGeneratorInterface<FinancialTransaction> {

	private static final long serialVersionUID = 2851842899904434912L;
	private GroupingBoxPanelForm searchBoxPanelForm;
	private CustomFinancialTransactionService txService;
	
	
	public GroupingSearchListGenerator(
			CustomFinancialTransactionService txService) {
		super();		
		this.txService = txService;
	}




	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.dim.desktop.components.config.ListGeneratorInterface#getResultsList(int, int)
	 */
	@Override
	public PagingHelper<FinancialTransaction> getResultsList(int pageNumber, int pageSize) {
		if (searchBoxPanelForm != null)
			return PagingHelper.createPagingHelperFromPage(this.txService.findByDonorIdCrsIdActive(searchBoxPanelForm
					.getDonorIdSearch(), searchBoxPanelForm.getCrsIdSearch(),searchBoxPanelForm.getActive(), null, new PageRequest(pageNumber - 1,
					pageSize)));
		else
			return null;
	}



	/**
	 * @return the searchBoxPanelForm
	 */
	public GroupingBoxPanelForm getSearchBoxPanelForm() {
		return searchBoxPanelForm;
	}




	/**
	 * @param searchBoxPanelForm the searchBoxPanelForm to set
	 */
	public void setSearchBoxPanelForm(GroupingBoxPanelForm searchBoxPanelForm) {
		this.searchBoxPanelForm = searchBoxPanelForm;
	}
	
	


	

}
