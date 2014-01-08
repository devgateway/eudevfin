/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.tabs.AbstractTabWithKey;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.ui.common.components.util.ListGeneratorInterface;

/**
 * @author mihai
 *
 */
public class TransactionTableListPanel extends TableListPanel<FinancialTransaction> {

	private static final long serialVersionUID = -3321918474505239409L;

	/**
	 * @param id
	 * @param listGenerator
	 */
	public TransactionTableListPanel(String id,
			ListGeneratorInterface<FinancialTransaction> listGenerator) {
		super(id, listGenerator);
		// TODO Auto-generated constructor stub
	}
	
	   public static ITabWithKey newTab(final Component askingComponent,final String key, final ListGeneratorInterface<FinancialTransaction> listGenerator) {
	        return new AbstractTabWithKey(new StringResourceModel(key, askingComponent, null), key) {

				private static final long serialVersionUID = -3378395518127823461L;

				@Override
	            public WebMarkupContainer getPanel(String panelId) {
	                return new TransactionTableListPanel(panelId, listGenerator);
	            }
	        };
	    }
	
	@Override	
	protected void populateTable() {
		this.itemsListView		= new ListView<FinancialTransaction>("transaction-list", items  ) {

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
		
		this.add(itemsListView);
	}
}
