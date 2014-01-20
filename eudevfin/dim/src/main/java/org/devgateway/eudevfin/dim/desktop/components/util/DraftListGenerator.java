package org.devgateway.eudevfin.dim.desktop.components.util;

import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.springframework.data.domain.PageRequest;

public class DraftListGenerator implements ListGeneratorInterface<CustomFinancialTransaction> {

	private static final long serialVersionUID = -1660943015913413489L;
	
	private Boolean draft;
	private CustomFinancialTransactionService customTxService;
	
	public DraftListGenerator(Boolean draft,
			CustomFinancialTransactionService customTxService) {
		super();
		this.draft = draft;
		this.customTxService = customTxService;
	}





	@Override
	public PagingHelper<CustomFinancialTransaction> getResultsList(int pageNumber,
			int pageSize) {
		return PagingHelper.createPagingHelperFromPage(this.customTxService.findByDraftPageable(this.draft, new PageRequest(pageNumber-1,pageSize)));
	}

}
