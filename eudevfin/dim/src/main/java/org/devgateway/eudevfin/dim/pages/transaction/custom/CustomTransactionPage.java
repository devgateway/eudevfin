/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.CheckBoxField;
import org.devgateway.eudevfin.ui.common.events.Field12ChangedEventPayload;
import org.devgateway.eudevfin.ui.common.events.Field13ChangedEventPayload;
import org.devgateway.eudevfin.ui.common.events.Field14aChangedEventPayload;
import org.devgateway.eudevfin.ui.common.permissions.RoleActionMapping;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;

/**
 * Custom Transaction Page for the EU-DEVFIN Form, extends the CRS Form with extended tabs and permissions
 *
 * @author aartimon
 * @see TransactionPage
 * @since 11/12/13
 */
@MountPath(value = "/custom")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class CustomTransactionPage extends TransactionPage {
	
	
	private static final long serialVersionUID = -7808024425119532771L;
	private static final CustomTransactionPermissionProvider permissions = new CustomTransactionPermissionProvider();
	
	private CheckBoxField approved;
	private CheckBoxField draft;

    @Override
    protected List<Class<? extends Panel>> getTabs() {
        List<Class<? extends Panel>> tabList = new ArrayList<>();
        tabList.add(CustomIdentificationDataTab.class);
        tabList.add(CustomBasicDataTab.class);
        tabList.add(CustomSupplementaryDataTab.class);
        tabList.add(CustomVolumeDataTab.class);
        tabList.add(CustomForLoansOnlyTab.class);
        tabList.add(AdditionalInfoTab.class);
        return tabList;
    }

    @Override
    public HashMap<String, RoleActionMapping> getPermissions() {
        return permissions.permissions();
    }

    @Override
    protected FinancialTransaction getFinancialTransaction() {
        return new CustomFinancialTransaction();
    }

	@Override
	public void initializeFinancialTransaction(FinancialTransaction transaction, PageParameters parameters) {
		super.initializeFinancialTransaction(transaction, parameters);
		if (!parameters.get(Constants.PARAM_TRANSACTION_TYPE).isNull()) {
			String transactionType = parameters.get(Constants.PARAM_TRANSACTION_TYPE).toString();
			CustomFinancialTransaction customFinancialTransaction = (CustomFinancialTransaction) transaction;
			customFinancialTransaction.setFormType(transactionType);
			PersistedUser user=AuthUtils.getCurrentUser();			
			customFinancialTransaction.setPersistedUserGroup(user.getGroup());
		}
		
	}
	
	
    public CustomTransactionPage(PageParameters parameters) {
  		super(parameters);

  		
  		draft = new CheckBoxField("draft",
				new RWComponentPropertyModel<Boolean>("draft")) {  	
  		@Override
  		protected CheckBox newField(String id, IModel<Boolean> model) {
  			 return new AjaxCheckBox(id, model) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if(this.getModel().getObject()) {
  						info(new NotificationMessage(new StringResourceModel("notification.draftState", CustomTransactionPage.this, null, null)));
  						approved.getField().setModelObject(false);
  						target.add(approved.getField());
  						submitButton.setDefaultFormProcessing(false); //unvalidated form save is allowed as draft
					}
  					else {
  						info(new NotificationMessage(new StringResourceModel("notification.nonDraftState", CustomTransactionPage.this, null, null)));
  						submitButton.setDefaultFormProcessing(true); //unvalidated form save is NOT allowed as non-draft
  					}
					target.add(feedbackPanel);
				}
  			 };
  		}	
  		};
  		
  		
  		approved = new CheckBoxField("approved",
				new RWComponentPropertyModel<Boolean>("approved")) {  	
  		@Override
  		protected CheckBox newField(String id, IModel<Boolean> model) {
  			 return new AjaxCheckBox(id, model) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if(this.getModel().getObject()) {
  						info(new NotificationMessage(new StringResourceModel("notification.approvedState", CustomTransactionPage.this, null, null)));
  						draft.getField().setModelObject(false);
  						target.add(draft.getField());
					}
  					else
  						info(new NotificationMessage(new StringResourceModel("notification.unapprovedState", CustomTransactionPage.this, null, null)));
					target.add(feedbackPanel);
				}
  			 };
  		}	
  		};
  		
  		
  		MetaDataRoleAuthorizationStrategy.authorize(approved, Component.ENABLE, AuthConstants.Roles.ROLE_TEAMLEAD);
  		form.add(draft);
  		form.add(approved);
  	  
  		
		//always set this field to true when form is opened
  		//draft.getField().getModel().setObject(true);
  		
		draft.removeSpanFromControlGroup();
		approved.removeSpanFromControlGroup();	
  	}

	/**
	 * @return the approved
	 */
	public CheckBoxField getApproved() {
		return approved;
	}

	/**
	 * @return the draft
	 */
	public CheckBoxField getDraft() {
		return draft;
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		// send events on page load
		CustomFinancialTransaction transaction = (CustomFinancialTransaction) form.getInnermostModel().getObject();
		if (transaction.getTypeOfFinance() != null)
			send(getPage(), Broadcast.DEPTH, new Field12ChangedEventPayload(null, transaction.getTypeOfFinance()
					.getDisplayableCode()));

		if (transaction.getProjectCoFinanced()!=null)
			send(getPage(), Broadcast.DEPTH, new Field14aChangedEventPayload(null, (transaction.getProjectCoFinanced())));

		
		if (transaction.getTypeOfAid() != null)
			send(getPage(), Broadcast.DEPTH, new Field13ChangedEventPayload(null, transaction.getTypeOfAid()
					.getDisplayableCode()));

		if (transaction.getDraft())
			submitButton.setDefaultFormProcessing(false);
		else
			submitButton.setDefaultFormProcessing(true);
	}

}
