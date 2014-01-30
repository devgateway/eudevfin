/*******************************************************************************
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    mihai
 ******************************************************************************/

package org.devgateway.eudevfin.mcm.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.joda.time.LocalDateTime;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mihai
 * @since 30.01.2014
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/nonflow")
public class EditNonFlowItemsPage extends HeaderFooter {

	@SpringBean
	private FinancialTransactionService financialTransactionService;

	private static final Logger logger = Logger.getLogger(EditNonFlowItemsPage.class);

	@SuppressWarnings("unchecked")
	public EditNonFlowItemsPage(final PageParameters parameters) {
		super(parameters);

		Form form = new Form("form");
		add(form);

		TextInputField<Integer> reportingYear = new TextInputField<>("reportingYear", new YearToLocalDateTimeModel(
				new RWComponentPropertyModel<LocalDateTime>("reportingYear")));
		reportingYear.typeInteger().required().range(1900, 2099).decorateMask("9999");
		form.add(reportingYear);
		
//		TextInputField<Integer> reportingYear = new TextInputField<>("reportingYear", new YearToLocalDateTimeModel(
//				new RWComponentPropertyModel<LocalDateTime>("reportingYear")));
//		reportingYear.typeInteger().required().range(1900, 2099).decorateMask("9999");
//		form.add(reportingYear);
		
		
	}

}
