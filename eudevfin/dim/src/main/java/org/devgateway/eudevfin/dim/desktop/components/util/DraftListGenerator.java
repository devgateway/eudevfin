/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.dim.desktop.components.util;

import org.devgateway.eudevfin.auth.common.domain.PersistedUserGroup;
import org.devgateway.eudevfin.common.service.PagingHelper;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;
import org.springframework.data.domain.PageRequest;

public class DraftListGenerator implements ListGeneratorInterface<CustomFinancialTransaction> {

	private static final long serialVersionUID = -1660943015913413489L;
	
	private Boolean draft;
	private PersistedUserGroup persistedUserGroup;
	private CustomFinancialTransactionService customTxService;
	private boolean superUser;
	
	public DraftListGenerator(Boolean draft,PersistedUserGroup persistedUserGroup,
			CustomFinancialTransactionService customTxService,boolean superUser) {
		super();
		this.draft = draft;
		this.persistedUserGroup=persistedUserGroup;
		this.customTxService = customTxService;
		this.superUser=superUser;
	}


	@Override
	public PagingHelper<CustomFinancialTransaction> getResultsList(int pageNumber, int pageSize) {
		if (superUser)
			return PagingHelper.createPagingHelperFromPage(this.customTxService.findByDraftPageable(this.draft,
					new PageRequest(pageNumber - 1, pageSize)));
		else
			return PagingHelper.createPagingHelperFromPage(this.customTxService
					.findByDraftAndPersistedUserGroupPageable(this.draft, this.persistedUserGroup, new PageRequest(
							pageNumber - 1, pageSize)));
	}

}
