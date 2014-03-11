/**
 * 
 */
package org.devgateway.eudevfin.ui.common.components;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.devgateway.eudevfin.common.service.PagingItem;

/**
 * @author Alex
 *
 */
public class PagingAjaxLink extends AjaxLink<PagingItem> {

	private TableListPanel<?> tableListPanel;
	private List<PagingItem> pagingItems;
	
	public PagingAjaxLink(String id, IModel<PagingItem> model,
			TableListPanel<?> tableListPanel, List<PagingItem> pagingItems) {
		super(id, model);
		this.tableListPanel = tableListPanel;
		this.pagingItems	= pagingItems;
		this.setAfterDisabledLink("");
		this.setBeforeDisabledLink("");
		PagingItem pagingItem	= model.getObject();
		Label label				= new Label("page-number-label", pagingItem.getLabel());
		this.add(label);
		if ( pagingItem.isDisabled() ){
			this.setEnabled(false);
		}
	}
	
	private static final long serialVersionUID = 1L;

	@Override
	public void onClick(AjaxRequestTarget target) {
		int futurePageNo		= -1;
		PagingItem pagingItem	= this.getModelObject();
		if ( "<".equals(pagingItem.getLabel()) ) {
			futurePageNo	= pagingItem.getCurrentPageNo()-1;
		}
		else if ( ">".equals(pagingItem.getLabel())) {
			futurePageNo	= pagingItem.getCurrentPageNo()+1;
		}
		else
			futurePageNo	= Integer.parseInt(pagingItem.getLabel());
		
		if(futurePageNo<1 || futurePageNo > this.pagingItems.size()) return;
		
		this.tableListPanel.generateListOfItems(futurePageNo);
		target.add(this.tableListPanel);
	}


}
