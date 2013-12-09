/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.devgateway.eudevfin.dim.core.components.tabs.BootstrapCssTabbedPanel;
import org.devgateway.eudevfin.dim.core.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.financial.FinancialTransaction;

/**
 * @author Alex
 *
 */
public class TopTransactionPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6875484694317258867L;

	public TopTransactionPanel(String id, IModel<List<FinancialTransaction>> model) {
		super(id, model);
		// TODO Auto-generated constructor stub
	}

	public TopTransactionPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	
	
	
	
}
