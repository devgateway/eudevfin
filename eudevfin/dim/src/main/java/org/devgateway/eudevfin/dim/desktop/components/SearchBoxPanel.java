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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.dim.desktop.components.util.GeneralSearchListGenerator;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.TableListPanel;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.forms.SearchBoxPanelForm;
import org.devgateway.eudevfin.ui.common.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.devgateway.eudevfin.ui.common.providers.FormTypeProvider;
import org.devgateway.eudevfin.ui.common.providers.OrganizationChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.YearProvider;
import org.joda.time.LocalDateTime;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;

/**
 * @author Alex,mihai
 *
 */
public class SearchBoxPanel extends Panel {

	private static final long serialVersionUID = 6025430438643716484L;
	private TableListPanel<?> resultsPanel;
	private GeneralSearchListGenerator listGenerator;
	private WebMarkupContainer searchWrapperPanel;
		
	private CategoryProviderFactory categoryFactory;
	
    private OrganizationChoiceProvider organizationProvider;
    
	private AreaChoiceProvider areaProvider;
	
	@SpringBean
	private CustomFinancialTransactionService txService;
	private boolean superUser;
	private DropDownField<Organization> extendingAgency;
	
	
	/**
	 * @param id
	 * @param generalSearchListGenerator 
	 * @param categoryFactory 
	 * @param areaProvider
	 * @param organizationProvider 
	 */
	public SearchBoxPanel(String id, TableListPanel<?> resultsPanel, GeneralSearchListGenerator generalSearchListGenerator,
			CategoryProviderFactory categoryFactory, OrganizationChoiceProvider organizationProvider, AreaChoiceProvider areaProvider) {
        super(id);
        this.resultsPanel		= resultsPanel;
		this.listGenerator		= generalSearchListGenerator;
		this.searchWrapperPanel	= new WebMarkupContainer("search-results-panel-wrapper") ;
		this.searchWrapperPanel.setOutputMarkupId(true);
		this.categoryFactory=categoryFactory;
		this.organizationProvider=organizationProvider;
		this.areaProvider=areaProvider;
		this.populate(null);
		this.setOutputMarkupId(true);
	}
	
	
	protected void populate(String searchString) {
		this.resultsPanel.setVisible(false);
		this.searchWrapperPanel.add(this.resultsPanel);
		this.add(this.searchWrapperPanel);

	 	superUser=AuthUtils.currentUserHasRole(AuthConstants.Roles.ROLE_SUPERVISOR);
	 	
		final SearchBoxPanelForm boxPanelForm=new SearchBoxPanelForm();
		CompoundPropertyModel<SearchBoxPanelForm> boxPanelFormModel=new CompoundPropertyModel<SearchBoxPanelForm>(boxPanelForm);
		Form<?> form = new Form<>("searchForm",boxPanelFormModel);
		form.setOutputMarkupId(false);
		
		  
		final DropDownField<Integer> year = new DropDownField<Integer>("year", new YearToLocalDateTimeModel(new RWComponentPropertyModel<LocalDateTime>(
				"year")), new YearProvider(this.txService.findDistinctReportingYears()));
		year.setSize(InputBehavior.Size.Medium);
   		year.removeSpanFromControlGroup();
   		year.hideLabel();
		form.add(year);
		
		final DropDownField<Category> sectorPurposeCode = new DropDownField<>("sector",
				new RWComponentPropertyModel<Category>("sector"), categoryFactory.get(CategoryConstants.ALL_SECTOR_TAG));
		//sectorPurposeCode.setSize(InputBehavior.Size.Medium);
		sectorPurposeCode.hideLabel();
		sectorPurposeCode.removeSpanFromControlGroup();
		form.add(sectorPurposeCode);
		
		final TextInputField<String> searchInputField	= new TextInputField<String>("searchString", new RWComponentPropertyModel<String>(
				"searchString"), "desktop.search");
        searchInputField.typeString();
        searchInputField.setSize(InputBehavior.Size.Medium);
        searchInputField.hideLabel();
        searchInputField.removeSpanFromControlGroup();
		form.add(searchInputField);		
			 	
        extendingAgency = new DropDownField<>("extendingAgency",
                new RWComponentPropertyModel<Organization>("extendingAgency"), organizationProvider);
        //extendingAgency.setSize(InputBehavior.Size.Medium);
        extendingAgency.hideLabel();
        extendingAgency.removeSpanFromControlGroup();
    	
        
        form.add(extendingAgency);              
    	
        final DropDownField<String> formType = new DropDownField<>("formType",
                new RWComponentPropertyModel<String>("formType"), new FormTypeProvider(this));
        formType.setSize(InputBehavior.Size.Medium);
        formType.hideLabel();
        formType.removeSpanFromControlGroup();
        form.add(formType);
        
        
		final DropDownField<Area> recipient = new DropDownField<>("recipient", new RWComponentPropertyModel<Area>(
				"recipient"), areaProvider);
		recipient.setSize(InputBehavior.Size.Medium);
		recipient.hideLabel();
		recipient.removeSpanFromControlGroup();
        form.add(recipient);
        
		
		BootstrapSubmitButton submitButton = new BootstrapSubmitButton("submit",new StringResourceModel("desktop.searchbutton", this,null)) {

			private static final long serialVersionUID = -1342816632002116152L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			// Access the updated model object:
				if ((boxPanelForm.getSearchString() != null && boxPanelForm.getSearchString().length() > 1)
						|| boxPanelForm.getSector() != null || boxPanelForm.getYear() != null
						|| boxPanelForm.getRecipient() != null || boxPanelForm.getFormType() != null
						|| boxPanelForm.getExtendingAgency() != null) {
		            	SearchBoxPanel.this.listGenerator.setSearchBoxPanelForm(boxPanelForm);
		            	SearchBoxPanel.this.resultsPanel.generateListOfItems(1);
		            	SearchBoxPanel.this.resultsPanel.setVisible(true);
		            }
		            else
		            	SearchBoxPanel.this.resultsPanel.setVisible(false);
		            target.add(SearchBoxPanel.this.searchWrapperPanel);
				}
				
			};		
		form.add(submitButton);
		
		BootstrapCancelButton resetButton = new BootstrapCancelButton("reset", new StringResourceModel("desktop.resetbutton", this,null)) {

			private static final long serialVersionUID = -7554180087300408868L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					sectorPurposeCode.getField().setDefaultModelObject(null);
					searchInputField.getField().setDefaultModelObject(null);
					extendingAgency.getField().setDefaultModelObject(superUser?null:AuthUtils.getOrganizationForCurrentUser());
					year.getField().setDefaultModelObject(null);
					formType.getField().setDefaultModelObject(null);
					recipient.getField().setDefaultModelObject(null);
		            target.add(sectorPurposeCode.getField());
		            target.add(searchInputField.getField());
		            target.add(year.getField());
		            target.add(extendingAgency.getField());
		            target.add(formType.getField());
		            target.add(recipient.getField());
	            	SearchBoxPanel.this.resultsPanel.setVisible(false);
	            	target.add(SearchBoxPanel.this.searchWrapperPanel);
				}
				
			};		
		form.add(resetButton);
        
		this.add(form);
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		if(!superUser) {
    		extendingAgency.setEnabled(false);
    		extendingAgency.getField().setDefaultModelObject(AuthUtils.getOrganizationForCurrentUser());
    	}
	}
	
}
