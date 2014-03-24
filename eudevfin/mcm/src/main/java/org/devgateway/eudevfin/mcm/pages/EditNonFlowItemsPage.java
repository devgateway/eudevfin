/*******************************************************************************
a * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *    mihai
 ******************************************************************************/

package org.devgateway.eudevfin.mcm.pages;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ComponentDetachableModel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.CurrencyMetadataService;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.metadata.common.service.CategoryService;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.FinancialAmountTextInputField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.components.util.MondrianCacheUtil;
import org.devgateway.eudevfin.ui.common.models.BigMoneyModel;
import org.devgateway.eudevfin.ui.common.models.YearToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.LocalDateTime;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

/**
 * @author mihai
 * @since 30.01.2014
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/nonflow")
public class EditNonFlowItemsPage extends HeaderFooter {

	@SpringBean
	private FinancialTransactionService financialTransactionService;

	@SpringBean
	private CategoryService categoryService;

	@SpringBean
	private CurrencyMetadataService currencyMetadataService;

    @SpringBean
    MondrianCacheUtil mondrianCacheUtil;

	private static final Logger logger = Logger
			.getLogger(EditNonFlowItemsPage.class);

	private WebMarkupContainer populationContainer;

	private WebMarkupContainer gniContainer;

	private WebMarkupContainer totalFlowsContainer;

	private WebMarkupContainer odaOfGniContainer;

	private TextInputField<Integer> reportingYearField;

	private NotificationPanel feedbackPanel;

	private TextInputField<String> reportingCountryField;

	private TextInputField<String> reportingCurrencyField;

	/**
	 * Simple model compliant with {@link IComponentAssignedModel} even if we
	 * dont need to know the {@link Component}
	 * 
	 * @author mihai
	 * 
	 * @param <T>
	 */
	public class LocalComponentDetachableModel<T> extends
			ComponentDetachableModel<T> {

		transient T t;

		@Override
		protected T getObject(Component component) {
			return t;
		}

		/**
		 * no state between refreshes
		 */
		@Override
		protected void attach() {
			t = null;
		}

		/**
		 * we receive the component and we really don't care much...
		 */
		@Override
		protected void setObject(Component component, T object) {
			t = object;
		}
	};

	public WebMarkupContainer initializeFakeFinancialContainer(String id,
			String fieldId, PageParameters parameters,
			String typeOfFinanceCode, boolean percentage) {
		// initialize a wrapping container
		WebMarkupContainer container = new WebMarkupContainer(id);

		// initialize a non flow transaction
		final FinancialTransaction financialTransaction = initializeNonFlowTransaction(
				new FinancialTransaction(), parameters, typeOfFinanceCode, null);

		// create a compoundmodel and assign it to he container
		container
				.setDefaultModel(new CompoundPropertyModel<FinancialTransaction>(
						financialTransaction));

		// create a read only model to read the currency
		ComponentPropertyModel<CurrencyUnit> readOnlyCurrencyModel = new ComponentPropertyModel<>(
				"currency");

		// create textinputfield to edit the amount
		FinancialAmountTextInputField field = new FinancialAmountTextInputField(
				fieldId, new BigMoneyModel(
						new RWComponentPropertyModel<BigMoney>(
								"amountsExtended"), readOnlyCurrencyModel)).required();
		if (percentage)
			field.range(new BigDecimal(0), new BigDecimal(100));
		container.add(field);

		return container;
	}

	@SuppressWarnings("unchecked")
	public EditNonFlowItemsPage(final PageParameters parameters) {
		super(parameters);

		Form form = new Form("form");
		add(form);

		// this only holds the current year selection

		final LocalComponentDetachableModel<LocalDateTime> reportingYearModel = new LocalComponentDetachableModel<LocalDateTime>();

		final LocalComponentDetachableModel<String> countryModel = new LocalComponentDetachableModel<String>() {
			protected void attach() {
				Organization organizationForCurrentUser = AuthUtils
						.getOrganizationForCurrentUser();
				if (organizationForCurrentUser != null)
					t = organizationForCurrentUser.getDonorName();
			}
		};

		final LocalComponentDetachableModel<String> currencyModel = new LocalComponentDetachableModel<String>() {
			protected void attach() {
				CurrencyUnit currencyForCountryIso = FinancialTransactionUtil
						.getCurrencyForCountryIso(AuthUtils
								.getIsoCountryForCurrentUser());
				if (currencyForCountryIso != null)
					t = currencyForCountryIso.getCode();
			}
		};

		populationContainer = (WebMarkupContainer) initializeFakeFinancialContainer(
				"populationContainer", "population", parameters,
				CategoryConstants.TypeOfFinance.NonFlow.POPULATION, false)
				.setEnabled(false).setOutputMarkupId(true);
		gniContainer = (WebMarkupContainer) initializeFakeFinancialContainer(
				"gniContainer", "gni", parameters,
				CategoryConstants.TypeOfFinance.NonFlow.GNI, false).setEnabled(
				false).setOutputMarkupId(true);
		totalFlowsContainer = (WebMarkupContainer) initializeFakeFinancialContainer(
				"totalFlowsContainer",
				"totalFlows",
				parameters,
				CategoryConstants.TypeOfFinance.NonFlow.TOTAL_FLOWS_PERCENT_GNI,
				true).setEnabled(false).setOutputMarkupId(true);
		odaOfGniContainer = (WebMarkupContainer) initializeFakeFinancialContainer(
				"odaOfGniContainer", "odaOfGni", parameters,
				CategoryConstants.TypeOfFinance.NonFlow.ODA_PERCENT_GNI, true)
				.setEnabled(false).setOutputMarkupId(true);

		// initialize textinput for reportingYear

		reportingCountryField = new TextInputField<String>("reportingCountry",
				countryModel);
        reportingCountryField.typeString().setVisible(false);
        form.add(reportingCountryField);
		
		reportingCurrencyField = new TextInputField<String>("reportingCurrency",
				currencyModel);
        reportingCurrencyField.typeString().setVisible(false);
        form.add(reportingCurrencyField);

		reportingYearField = new TextInputField<Integer>("reportingYear",
				new YearToLocalDateTimeModel(reportingYearModel)) {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				super.onUpdate(target);

				LocalDateTime reportingYear = reportingYearModel
						.wrapOnAssignment(this).getObject();
				List<FinancialTransaction> findByReportingYearAndTypeOfFlowCode = financialTransactionService
						.findByReportingYearAndTypeOfFlowCode(reportingYear,
								CategoryConstants.TypeOfFlow.NON_FLOW);

				// put the transactions in a map for quicker access
				Map<String, FinancialTransaction> trnsByTypeOfFinance = new HashMap<>();
				for (FinancialTransaction fakeNonFlowTransaction : findByReportingYearAndTypeOfFlowCode)
					if (fakeNonFlowTransaction.getTypeOfFinance() != null)
						trnsByTypeOfFinance.put(fakeNonFlowTransaction
								.getTypeOfFinance().getCode(),
								fakeNonFlowTransaction);

				refreshTransactionInContainer(populationContainer,
						CategoryConstants.TypeOfFinance.NonFlow.POPULATION,
						parameters, trnsByTypeOfFinance, reportingYear);
				
				refreshTransactionInContainer(gniContainer,
						CategoryConstants.TypeOfFinance.NonFlow.GNI,
						parameters, trnsByTypeOfFinance, reportingYear);
				
				refreshTransactionInContainer(
						odaOfGniContainer,
						CategoryConstants.TypeOfFinance.NonFlow.ODA_PERCENT_GNI,
						parameters, trnsByTypeOfFinance, reportingYear);
				refreshTransactionInContainer(
						totalFlowsContainer,
						CategoryConstants.TypeOfFinance.NonFlow.TOTAL_FLOWS_PERCENT_GNI,
						parameters, trnsByTypeOfFinance, reportingYear);

				target.add(populationContainer.setEnabled(true));
				target.add(gniContainer.setEnabled(true));
				target.add(totalFlowsContainer.setEnabled(true));
				target.add(odaOfGniContainer.setEnabled(true));
			}
		};
		reportingYearField.typeInteger().required().range(2000, 2099)
				.decorateMask("9999");

		form.add(reportingYearField);

		form.add(populationContainer);
		form.add(gniContainer);
		form.add(totalFlowsContainer);
		form.add(odaOfGniContainer);

		form.add(new BootstrapSubmitButton("submit", new StringResourceModel(
				"button.submit", this, null, null)) {
			private static final long serialVersionUID = 1064657874335641769L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				saveTransactionFromContainer(populationContainer);
				saveTransactionFromContainer(gniContainer);
				saveTransactionFromContainer(odaOfGniContainer);
				saveTransactionFromContainer(totalFlowsContainer);
				logger.info("Submitted ok!");
				info(new NotificationMessage(new StringResourceModel(
						"notification.saved", EditNonFlowItemsPage.this, null,
						null)));
				target.add(feedbackPanel);

                // clear the mondrian cache
                mondrianCacheUtil.flushMondrianCache();
			}

		});
		
		
		form.add(new BootstrapCancelButton("cancel", new StringResourceModel("button.cancel", this, null, null)) {
			private static final long serialVersionUID = -8507390274067369368L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Cancel pressed");
				setResponsePage(getApplication().getHomePage());
			}

		}.setDefaultFormProcessing(false));

		feedbackPanel = new NotificationPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.hideAfter(Duration.seconds(3));
		add(feedbackPanel);

	}

	public void saveTransactionFromContainer(WebMarkupContainer container) {
		FinancialTransaction transaction = (FinancialTransaction) container
				.getInnermostModel().getObject();
		FinancialTransaction saved = financialTransactionService.save(
				transaction).getEntity();
		container.setDefaultModelObject(saved);
	}

	/**
	 * 
	 * @param container
	 * @param typeOfFinanceCode
	 * @param parameters
	 * @param trnsByTypeOfFinance
	 * @param reportingYear
	 */
	public void refreshTransactionInContainer(WebMarkupContainer container,
			String typeOfFinanceCode, PageParameters parameters,
			Map<String, FinancialTransaction> trnsByTypeOfFinance,
			LocalDateTime reportingYear) {
		if (trnsByTypeOfFinance.get(typeOfFinanceCode) != null)
			container
					.setDefaultModel(new CompoundPropertyModel<FinancialTransaction>(
							trnsByTypeOfFinance.get(typeOfFinanceCode)));
		else
			container
					.setDefaultModel((new CompoundPropertyModel<FinancialTransaction>(
							initializeNonFlowTransaction(
									new FinancialTransaction(), parameters,
									typeOfFinanceCode, reportingYear))));
	}

	/**
	 * Initialize a {@link CategoryConstants.TypeOfFlow#NON_FLOW} transaction
	 * 
	 * @param transaction
	 *            the {@link FinancialTransaction}
	 * @param parameters
	 * @param typeOfFinanceCode
	 *            {@link CategoryConstants.TypeOfFinance}
	 * @param reportingYear
	 * @return
	 */
	public FinancialTransaction initializeNonFlowTransaction(
			FinancialTransaction transaction, PageParameters parameters,
			String typeOfFinanceCode, LocalDateTime reportingYear) {
		PersistedUser user = (PersistedUser) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		// get a stub financial transaction initialized
		FinancialTransactionUtil.initializeFinancialTransaction(transaction,
				this.currencyMetadataService,
				AuthUtils.getOrganizationForCurrentUser(),
				AuthUtils.getIsoCountryForCurrentUser());

		// initialize type of finance
		Category typeOfFinanceCategory = categoryService.findByCodeAndClass(
				typeOfFinanceCode, Category.class, false).getEntity();
		transaction.setTypeOfFinance(typeOfFinanceCategory);

		// initialize type of flow
		Category typeOfFlowNonFlowCategory = categoryService
				.findByCodeAndClass(CategoryConstants.TypeOfFlow.NON_FLOW,
						Category.class, false).getEntity();
		transaction.setTypeOfFlow(typeOfFlowNonFlowCategory);

		transaction.setReportingYear(reportingYear);

		return transaction;
	}
}
