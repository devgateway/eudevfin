/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 */

package org.devgateway.eudevfin.dim.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dim.core.components.tabs.BootstrapJSTabbedPanel;
import org.devgateway.eudevfin.dim.core.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.devgateway.eudevfin.dim.desktop.components.AllLastTransactionPanel;
import org.devgateway.eudevfin.dim.desktop.components.SearchBoxPanel;
import org.devgateway.eudevfin.dim.desktop.components.config.ListGeneratorInterface;
import org.devgateway.eudevfin.dim.desktop.components.util.GeneralSearchListGenerator;
import org.devgateway.eudevfin.dim.desktop.components.util.SectorListGenerator;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.service.CategoryService;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.financial.service.OrganizationService;
import org.devgateway.eudevfin.financial.util.PagingHelper;
//import org.devgateway.eudevfin.financial.test.services.CategoryServiceTest;
import org.joda.money.BigMoney;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/home")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class HomePage extends HeaderFooter {
	
	private static final String DESKTOP_LAST_TX_BY_TRANSPORT = "desktop.lastTxByTransport";

	private static final String DESKTOP_LAST_TX_BY_AGRICULTURE = "desktop.lastTxByAgriculture";

	private static final String SECTOR_CODE_AGRIC = "311";

	private static final String SECTOR_CODE_TRANSPORT = "210";
	
    protected ListView<FinancialTransaction> transactionListView = null;
    @SpringBean
    protected FinancialTransactionService txService;

    @SpringBean
    protected OrganizationService orgService;
    
    @SpringBean
    protected CategoryService categoryService;

    public HomePage() {
        super();

		Organization o			= new Organization();
        o.setName("WicketTest Org - default locale");
        o.setLocale("ro");
        o.setName("WicketTest Org - ro locale");
		orgService.save(o);
		
		FinancialTransaction ft = new FinancialTransaction();
		ft.setCommitments(BigMoney.parse("EUR 777"));
		ft.setDescription("Wicket test descr - default locale");
		ft.setLocale("ro");
		ft.setDescription("Wicket test descr - ro locale");
		
		ft.setReportingOrganization(o);
		txService.save(ft);
		
//		Category sectorsRoot = new Category();
//		sectorsRoot.setName("Sectors Root");
//		sectorsRoot.setCode(CategoryServiceTest.SECTORS_ROOT_TEST );
//		sectorsRoot.setTags(new HashSet<Category>());
//		
//		Category sector1	= new Category();
//		sector1.setName("Sector 1");
//		sector1.setCode("sector_1_test " + System.currentTimeMillis() );
//		sector1.setParentCategory(sectorsRoot);
//		sector1.setTags(new HashSet<Category>());
//		
//		Category sector2	= new Category();
//		sector2.setName("Sector 2");
//		sector2.setCode("sector_2_test" + System.currentTimeMillis() );
//		sector2.setParentCategory(sectorsRoot);
//		sector2.setTags(new HashSet<Category>());
//		
//		categoryService.save(sectorsRoot);
//		
//		categoryService.findByCode(CategoryServiceTest.SECTORS_ROOT_TEST, true);
		
//		List<FinancialTransaction> allTransactions = txService.findAll();
//		this.transactionListView				= new ListView<FinancialTransaction>("transaction-list", allTransactions  ) {
//
//			@Override
//			protected void populateItem(ListItem<FinancialTransaction> ftListItem) {
//				// TODO Auto-generated method stub
//				FinancialTransaction tempTx		= ftListItem.getModelObject();
//				Label idLabel						= new Label("transaction-id", tempTx.getId() );
//				ftListItem.add(idLabel);
//				Label amountLabel						= new Label("transaction-value", tempTx.getCommitments().toString() );
//				ftListItem.add(amountLabel);
//				Label descriptionLabel						= new Label("transaction-description", tempTx.getDescription() );
//				ftListItem.add(descriptionLabel);
//				Label orgLabel						= new Label("organization-name", tempTx.getReportingOrganization().getName() );
//				ftListItem.add(orgLabel);
//				
//			}
//			
//		};
//		
//		this.add(transactionListView);
		
		this.populateTopsPanel();
    }

    protected void populateTopsPanel() {
    	
    	//List<FinancialTransaction> transactions	= txService.findAll();
    	
  
    	List<ITabWithKey> tabList = new ArrayList<>();

    	
    	tabList.add( AllLastTransactionPanel.newTab( this,DESKTOP_LAST_TX_BY_AGRICULTURE, new SectorListGenerator(SECTOR_CODE_AGRIC, txService) ) );
    	tabList.add( AllLastTransactionPanel.newTab( this,DESKTOP_LAST_TX_BY_TRANSPORT, new SectorListGenerator(SECTOR_CODE_TRANSPORT, txService) ) );
    	
    	BootstrapJSTabbedPanel<ITabWithKey> bc = new BootstrapJSTabbedPanel<>("tops-panel", tabList).
                positionTabs(BootstrapJSTabbedPanel.Orientation.RIGHT);
    	
    	this.add(bc);
    	
    	GeneralSearchListGenerator generalSearchListGenerator	= new GeneralSearchListGenerator(null, txService);
    	this.add( new SearchBoxPanel("search-box-panel", new AllLastTransactionPanel("search-results-panel",generalSearchListGenerator ), generalSearchListGenerator ) );
    }
}
