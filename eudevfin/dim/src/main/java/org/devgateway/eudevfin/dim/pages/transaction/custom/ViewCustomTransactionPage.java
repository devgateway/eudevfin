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
package org.devgateway.eudevfin.dim.pages.transaction.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.dim.pages.transaction.crs.TransactionPage;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.components.tabs.DefaultTabWithKey;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.ui.common.components.tabs.PreviewTabbedPannel;
import org.devgateway.eudevfin.ui.common.pages.PrintableHeaderFooter;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwarePage;
import org.devgateway.eudevfin.ui.common.permissions.RoleActionMapping;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mihai
 * 
 */
@MountPath(value = "/transaction/view")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ViewCustomTransactionPage extends PrintableHeaderFooter<FinancialTransaction> implements PermissionAwarePage {

	/**	
	 * 
	 */
	private static final long serialVersionUID = -4729673458654381557L;
	private static final CustomTransactionPermissionProvider componentPermissions = new CustomTransactionPermissionProvider();
	protected Form form;
	public static final String PARAM_TRANSACTION_ID = "transactionId";

	@SpringBean
	private FinancialTransactionService financialTransactionService;

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

	private List<ITabWithKey> populateTabList(PageParameters parameters) {
		List<Class<? extends Panel>> tabClasses = getTabs();
		ArrayList<ITabWithKey> tabs = new ArrayList<>();
		for (final Class<? extends Panel> p : tabClasses) {
			tabs.add(DefaultTabWithKey.of(p, this, parameters));
		}
		return tabs;
	}

	/**
	 * 
	 */
	public ViewCustomTransactionPage(final PageParameters parameters) {
		super(parameters);
		
		pageTitle.setDefaultModel(new StringResourceModel(parameters.get(Constants.PARAM_TRANSACTION_TYPE).toString(""), this, null, null));
		
		List<ITabWithKey> tabList = populateTabList(parameters);

		long transactionId = parameters.get(PARAM_TRANSACTION_ID).toLong();		
		
		CustomFinancialTransaction financialTransaction = (CustomFinancialTransaction) financialTransactionService.findOne(transactionId).getEntity();
				
		// redirect to self if the transactionType parameter is empty, throw an
		// exception to prevent any infinite cycle
		if (parameters.get(Constants.PARAM_TRANSACTION_TYPE).isEmpty()) {
			if (Strings.isEmpty(financialTransaction.getFormType()))
				throw new RuntimeException("Cannot guess the form type because the persisted property is empty");
			PageParameters newPageParameters = new PageParameters();
			newPageParameters.add(Constants.PARAM_TRANSACTION_TYPE, financialTransaction.getFormType());
			newPageParameters.add(TransactionPage.PARAM_TRANSACTION_ID, financialTransaction.getId());
			getRequestCycle().setResponsePage(ViewCustomTransactionPage.class, newPageParameters);
			return;
		}
			
		
		CompoundPropertyModel<FinancialTransaction> model = new CompoundPropertyModel<FinancialTransaction>(
				financialTransaction);
		setModel(model);

		form = new Form("form");
		add(form);

		PreviewTabbedPannel<ITabWithKey> bc = new PreviewTabbedPannel<>("bc", tabList);
		form.add(bc);
	}

	@Override
	public HashMap<String, RoleActionMapping> getPermissions() {
		return componentPermissions.permissions();
	}


	
}
