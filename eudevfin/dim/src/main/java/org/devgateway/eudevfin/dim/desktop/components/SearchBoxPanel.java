/*
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

/**
 * 
 */
package org.devgateway.eudevfin.dim.desktop.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.devgateway.eudevfin.dim.desktop.components.util.GeneralSearchListGenerator;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.TextInputField;

/**
 * @author Alex
 *
 */
public class SearchBoxPanel extends Panel {

	private TableListPanel resultsPanel;
	private GeneralSearchListGenerator listGenerator;
	private WebMarkupContainer searchWrapperPanel;
	/**
	 * @param id
	 * @param generalSearchListGenerator 
	 */
	public SearchBoxPanel(String id, TableListPanel resultsPanel, GeneralSearchListGenerator generalSearchListGenerator) {
        super(id);
        this.resultsPanel		= resultsPanel;
		this.listGenerator		= generalSearchListGenerator;
		this.searchWrapperPanel	= new WebMarkupContainer("search-results-panel-wrapper") ;
		this.searchWrapperPanel.setOutputMarkupId(true);
		this.populate(null);
	}
	
	protected void populate(String searchString) {
		this.resultsPanel.setVisible(false);
		this.searchWrapperPanel.add(this.resultsPanel);
		this.add(this.searchWrapperPanel);
		//ProxyModel<String> labelTextModel = new ProxyModel<String>(new StringResourceModel("desktop.search.label", this, null));
		//this.add(new Label("search-box-label",labelTextModel));
		
		TextInputField<String> searchInputField	= new TextInputField<String>("search-box", Model.of(""), "desktop.search");
		//TextField<String> searchInputField	= new TextField<String>("search-box", Model.of(""));
        searchInputField.typeString();
        searchInputField.getField().add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Access the updated model object:
	            final String value 	= (String)getComponent().getDefaultModelObject();
	           
				
				
	            if ( value != null && value.length() > 1 ) {
//	            	System.out.println("VALUE IS" + value );
	            	SearchBoxPanel.this.listGenerator.setSearchString(value);
	            	SearchBoxPanel.this.resultsPanel.generateListOfItems(1);
	            	SearchBoxPanel.this.resultsPanel.setVisible(true);
	            }
	            else
	            	SearchBoxPanel.this.resultsPanel.setVisible(false);
	            target.add(SearchBoxPanel.this.searchWrapperPanel);
			}
		});
		
		this.add(searchInputField);
	}
	
}
