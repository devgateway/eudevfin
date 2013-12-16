/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.devgateway.eudevfin.dim.core.components.tabs.AbstractTabWithKey;
import org.devgateway.eudevfin.dim.core.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.dim.desktop.components.config.ListGeneratorInterface;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.util.PagingHelper;
import org.devgateway.eudevfin.financial.util.PagingItem;

/**
 * @author Alex
 *
 */
public class AllLastTransactionPanel extends Panel implements Pageable {
	
	private static final long serialVersionUID = -779595395189185422L;

	public static final int PAGE_SIZE		= 10;
	
	private ListView<FinancialTransaction> transactionListView;
	
	private List<FinancialTransaction> transactions 					= null;
	private List<PagingItem> pagingItems								= null;
	private ListGeneratorInterface<FinancialTransaction> listGenerator	= null;
	
    public static ITabWithKey newTab(final Component askingComponent,final String key, final ListGeneratorInterface<FinancialTransaction> listGenerator) {
        return new AbstractTabWithKey(new StringResourceModel(key, askingComponent, null), key) {

			private static final long serialVersionUID = -3378395518127823461L;

			@Override
            public WebMarkupContainer getPanel(String panelId) {
                return new AllLastTransactionPanel(panelId, listGenerator);
            }
        };
    }
	

	public AllLastTransactionPanel(String id, ListGeneratorInterface<FinancialTransaction> listGenerator) {
		super(id);

		this.listGenerator	= listGenerator;
		
		transactions		= new ArrayList<>();
		pagingItems			= new ArrayList<>();

		populate(1);
	}


	public void populate(int pageNumber) {
		this.generateListOfTransactions( pageNumber);
		this.setOutputMarkupId(true);
		
		this.populateTable();
		this.populatePaging();
		
		
	}

	private void populateTable() {
		this.transactionListView		= new ListView<FinancialTransaction>("transaction-list", transactions  ) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<FinancialTransaction> ftListItem) {
				FinancialTransaction tempTx			= ftListItem.getModelObject();
				Label idLabel						= new Label("transaction-name", tempTx.getDescription() );
				ftListItem.add(idLabel);
				Label amountLabel					= new Label("transaction-commitment-value", tempTx.getCommitments().toString() );
				ftListItem.add(amountLabel);
				Label descriptionLabel	= null;
				if ( tempTx.getSector() != null ) 
					descriptionLabel				= new Label("transaction-sector-name", tempTx.getSector().getName() );
				else
					descriptionLabel				= new Label("transaction-sector-name", "none" );
				ftListItem.add(descriptionLabel);
				Label orgLabel						= new Label("transaction-organization-name", tempTx.getReportingOrganization().getName() );
				ftListItem.add(orgLabel);
			}

		};
		
		this.add(transactionListView);
	}
	
	private void populatePaging() {
		SortingPanel sortingPanel 	= new SortingPanel("sorting-panel", new ListModel<>(this.pagingItems) );
		this.add(sortingPanel);
	}
	
	public void generateListOfTransactions( int pageNumber) {
		this.transactions.clear();
		this.pagingItems.clear();
		
		PagingHelper<FinancialTransaction> results	= this.listGenerator.getResultsList(pageNumber, PAGE_SIZE); 
		this.transactions.addAll( results.getEntities() );
		this.pagingItems.addAll( results.createPagingItems() );
		
	}
	
	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.dim.desktop.components.Pageable#createLink(T)
	 */
	@Override
	public AjaxLink<PagingItem> createLink ( final PagingItem modelObj ) {
		AjaxLink<PagingItem> link	= new AjaxLink<PagingItem>("page-number-link", Model.of(modelObj) ) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				int futurePageNo		= -1;
				PagingItem pagingItem	= this.getModelObject();
				if ( "<".equals(pagingItem.getLabel()) ) {
					futurePageNo	= pagingItem.getCurrentPageNo()-1;
				}
				else if ( ">".equals(pagingItem.getLabel()) ) {
					futurePageNo	= pagingItem.getCurrentPageNo()+1;
				}
				else
					futurePageNo	= Integer.parseInt(pagingItem.getLabel());
					
				
				AllLastTransactionPanel.this.generateListOfTransactions(futurePageNo);
				target.add(AllLastTransactionPanel.this);
			}
			
		};
		return link;
	}

}
