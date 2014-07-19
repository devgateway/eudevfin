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

public class ApprovedListGenerator implements ListGeneratorInterface<CustomFinancialTransaction> {

	private static final long serialVersionUID = -1660943015913413489L;
	
	private Boolean approved;
	private PersistedUserGroup persistedUserGroup;
	private CustomFinancialTransactionService customTxService;
	private boolean superUser;
	
	public ApprovedListGenerator(Boolean approved,PersistedUserGroup persistedUserGroup,
			CustomFinancialTransactionService customTxService,boolean superUser) {
		super();
		this.approved = approved;
		this.persistedUserGroup=persistedUserGroup;
		this.customTxService = customTxService;
		this.superUser=superUser;
	}


	@Override
	public PagingHelper<CustomFinancialTransaction> getResultsList(int pageNumber, int pageSize) {
		if (superUser)
			return PagingHelper.createPagingHelperFromPage(this.customTxService.findByApprovedPageable(this.approved,
					new PageRequest(pageNumber - 1, pageSize)));
		else
			return PagingHelper.createPagingHelperFromPage(this.customTxService
					.findByApprovedAndPersistedUserGroupPageable(this.approved, this.persistedUserGroup, new PageRequest(
							pageNumber - 1, pageSize)));
	}

}
