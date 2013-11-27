/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    aartimon
 ******************************************************************************/

package org.devgateway.eudevfin.dim.pages;

import java.math.BigDecimal;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.dim.core.Constants;
import org.devgateway.eudevfin.dim.core.pages.HeaderFooter;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.Organization;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.financial.service.OrganizationService;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/")
@AuthorizeInstantiation(Constants.ROLE_USER)
public class HomePage extends HeaderFooter {
    protected ListView<FinancialTransaction> transactionListView = null;
    @SpringBean
    protected FinancialTransactionService txService;

    @SpringBean
    protected OrganizationService orgService;

    public HomePage() {
        super();

		Organization o			= new Organization();
        o.setName("WicketTest Org - default locale");
        o.setLocale("ro");
        o.setName("WicketTest Org - ro locale");
		orgService.save(o);
		FinancialTransaction ft = new FinancialTransaction();
		ft.setAmount(new BigDecimal(777));
		ft.setDescription("Wicket test descr - default locale");
		ft.setLocale("ro");
		ft.setDescription("Wicket test descr - ro locale");
		
		ft.setReportingOrganization(o);
		txService.save(ft);
		List<FinancialTransaction> allTransactions = txService.findAll();
		this.transactionListView				= new ListView<FinancialTransaction>("transaction-list", allTransactions  ) {

			@Override
			protected void populateItem(ListItem<FinancialTransaction> ftListItem) {
				// TODO Auto-generated method stub
				FinancialTransaction tempTx		= ftListItem.getModelObject();
				Label idLabel						= new Label("transaction-id", tempTx.getId() );
				ftListItem.add(idLabel);
				Label amountLabel						= new Label("transaction-value", tempTx.getAmount().toPlainString() );
				ftListItem.add(amountLabel);
				Label descriptionLabel						= new Label("transaction-description", tempTx.getDescription() );
				ftListItem.add(descriptionLabel);
				Label orgLabel						= new Label("organization-name", tempTx.getReportingOrganization().getName() );
				ftListItem.add(orgLabel);
				
			}
			
		};
		
		this.add(transactionListView);
    }

}
