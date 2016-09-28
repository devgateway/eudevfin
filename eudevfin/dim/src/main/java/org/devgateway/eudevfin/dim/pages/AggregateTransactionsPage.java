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
package org.devgateway.eudevfin.dim.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dim.desktop.components.GroupingBoxPanel;
import org.devgateway.eudevfin.dim.desktop.components.GroupingTransactionTableListPanel;
import org.devgateway.eudevfin.dim.desktop.components.util.GroupingSearchListGenerator;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mihai
 *
 */
@MountPath(value = "/aggregate")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class AggregateTransactionsPage extends HeaderFooter {

	private static final long serialVersionUID = 817361503195961105L;

    @SpringBean
    protected CustomFinancialTransactionService customTxService;

    @SpringBean
	private CategoryProviderFactory categoryFactory;

    @SpringBean
    private OrganizationChoiceProvider organizationProvider;

    @SpringBean
    private AreaChoiceProvider areaProvider;

	public AggregateTransactionsPage() {
		GroupingSearchListGenerator generalSearchListGenerator = new GroupingSearchListGenerator(customTxService);
		this.add(new GroupingBoxPanel("search-box-panel", new GroupingTransactionTableListPanel<FinancialTransaction>(
				"search-results-panel", generalSearchListGenerator), generalSearchListGenerator, categoryFactory,
				organizationProvider, areaProvider));
	}

}
