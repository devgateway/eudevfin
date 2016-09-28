/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.dim.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.dim.desktop.components.SearchBoxPanel;
import org.devgateway.eudevfin.dim.desktop.components.TransactionTableListPanel;
import org.devgateway.eudevfin.dim.desktop.components.util.ApprovedListGenerator;
import org.devgateway.eudevfin.dim.desktop.components.util.DraftListGenerator;
import org.devgateway.eudevfin.dim.desktop.components.util.GeneralSearchListGenerator;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.service.CategoryService;
import org.devgateway.eudevfin.metadata.common.service.OrganizationService;
import org.devgateway.eudevfin.ui.common.components.tabs.BootstrapJSTabbedPanel;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.wicketstuff.annotation.mount.MountPath;


@MountPath(value = "/home")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class HomePage extends HeaderFooter {

	private static final long serialVersionUID = -7339282093922672085L;

	private static final String DESKTOP_LAST_TX_BY_DRAFT		= "desktop.lastTxByDraft";
	private static final String DESKTOP_LAST_TX_BY_FINAL		= "desktop.lastTxByFinal";
	private static final String DESKTOP_LAST_TX_BY_APPROVED		= "desktop.lastTxByApproved";

	protected ListView<FinancialTransaction> transactionListView = null;

    @SpringBean
    protected CustomFinancialTransactionService customTxService;

    @SpringBean
    protected OrganizationService orgService;

    @SpringBean
    protected CategoryService categoryService;

    @SpringBean
	private CategoryProviderFactory categoryFactory;

    @SpringBean
    private OrganizationChoiceProvider organizationProvider;

    @SpringBean
    private AreaChoiceProvider areaProvider;


    public HomePage() {
        super();
		this.populateTopsPanel();
    }

    protected void populateTopsPanel() {

    	List<ITabWithKey> tabList = new ArrayList<>();

    	boolean superUser=AuthUtils.currentUserHasRole(AuthConstants.Roles.ROLE_SUPERVISOR);
    	PersistedUser currentUser = AuthUtils.getCurrentUser();

		tabList.add(TransactionTableListPanel.<CustomFinancialTransaction> newTab(this, DESKTOP_LAST_TX_BY_DRAFT,
				new DraftListGenerator(true, currentUser.getGroup(), this.customTxService, superUser)));
		tabList.add(TransactionTableListPanel.<CustomFinancialTransaction> newTab(this, DESKTOP_LAST_TX_BY_FINAL,
				new DraftListGenerator(false, currentUser.getGroup(), this.customTxService, superUser)));
		tabList.add(TransactionTableListPanel.<CustomFinancialTransaction> newTab(this, DESKTOP_LAST_TX_BY_APPROVED,
				new ApprovedListGenerator(true, currentUser.getGroup(), this.customTxService, superUser)));

   	BootstrapJSTabbedPanel<ITabWithKey> bc = new BootstrapJSTabbedPanel<>("tops-panel", tabList).
                positionTabs(BootstrapJSTabbedPanel.Orientation.RIGHT);

    	this.add(bc);

    	GeneralSearchListGenerator generalSearchListGenerator	= new GeneralSearchListGenerator(customTxService);
    	this.add(new SearchBoxPanel("search-box-panel",
    			new TransactionTableListPanel<FinancialTransaction>("search-results-panel",generalSearchListGenerator),
    			generalSearchListGenerator,categoryFactory,organizationProvider,areaProvider));
    }
}
