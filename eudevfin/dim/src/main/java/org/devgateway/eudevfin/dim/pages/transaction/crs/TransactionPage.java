/*
 * Copyright (c) 2013 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */

package org.devgateway.eudevfin.dim.pages.transaction.crs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.visit.IVisit;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.domain.PersistedUser;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.ui.common.components.util.MondrianCacheUtil;
import org.devgateway.eudevfin.dim.pages.HomePage;
import org.devgateway.eudevfin.financial.FinancialTransaction;
import org.devgateway.eudevfin.financial.service.CurrencyMetadataService;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.ui.common.AttributePrepender;
import org.devgateway.eudevfin.ui.common.Constants;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.tabs.BootstrapJSTabbedPanel;
import org.devgateway.eudevfin.ui.common.components.tabs.DefaultTabWithKey;
import org.devgateway.eudevfin.ui.common.components.tabs.ITabWithKey;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.permissions.PermissionAwarePage;
import org.devgateway.eudevfin.ui.common.permissions.RoleActionMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

@MountPath(value = "/transaction")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class TransactionPage extends HeaderFooter<FinancialTransaction> implements PermissionAwarePage {

	private static final long serialVersionUID = -3616689887136295555L;

	private static final Logger logger = Logger.getLogger(TransactionPage.class);

	public static final String PARAM_TRANSACTION_ID = "transactionId";

    public final String onUnloadScript;


    @SpringBean
    private FinancialTransactionService financialTransactionService;
	
	@SpringBean
	private CurrencyMetadataService currencyMetadaService;

    @SpringBean
    MondrianCacheUtil mondrianCacheUtil;

	private static final CRSTransactionPermissionProvider componentPermissions = new CRSTransactionPermissionProvider();

	protected final NotificationPanel feedbackPanel;

	protected Form form;

	public class TransactionPageSubmitButton extends BootstrapSubmitButton {
		private static final long serialVersionUID = -8310280845870280505L;

		public TransactionPageSubmitButton(String id, IModel<String> model) {
			super(id, model);
		}

		Model<Boolean> shownFirstSection = null;

		@Override
		protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			FinancialTransaction transaction = (FinancialTransaction) form.getInnermostModel().getObject();
			logger.debug("Object:" + transaction);
			logger.info("Trying to save!");
			try {
				FinancialTransaction saved = financialTransactionService.save(transaction).getEntity();
				TransactionPage.this.getModel().setObject(saved);
				info(new NotificationMessage(new StringResourceModel("notification.saved", TransactionPage.this, null, null)));				
				target.add(feedbackPanel);

                // clear the mondrian cache
                mondrianCacheUtil.flushMondrianCache();
			} catch (Exception e) {
				logger.error("Exception while trying to save:", e);
				return;
			}
			logger.info("Saved ok!");
		}

		@Override
		protected void onError(AjaxRequestTarget target, Form<?> form) {			
			shownFirstSection = Model.of(Boolean.FALSE);
			super.onError(target, form);
			error(new NotificationMessage(new StringResourceModel("notification.validationerrors", TransactionPage.this, null, null)));
			target.add(feedbackPanel);
		}

		@Override
		public void componentVisitor(AjaxRequestTarget target, FormComponent component, IVisit<Void> visit) {
			// TODO Auto-generated method stub
			super.componentVisitor(target, component, visit);
            if ((!component.isValid()) && (!shownFirstSection.getObject())) {
                target.focusComponent(component);
				target.appendJavaScript("$('#" + component.getMarkupId()
						+ "').parents('[class~=\"tab-pane\"]').siblings().attr(\"class\", \"tab-pane\");");
				target.appendJavaScript("$('#" + component.getMarkupId()
						+ "').parents('[class~=\"tab-pane\"]').attr(\"class\", \"tab-pane active\");");

				target.appendJavaScript("$('#" + component.getMarkupId()
						+ "').parents('[class~=\"tabbable\"]').children('ul').find('li').attr('class', '');");
				target.appendJavaScript("var idOfSection = $('#"
						+ component.getMarkupId()
						+ "').parents('[class~=\"tab-pane\"]').attr('id');$('#"
						+ component.getMarkupId()
						+ "').parents('[class~=\"tabbable\"]').children('ul').find('a[href=\"#' + idOfSection + '\"]').parent().attr('class', 'active');");

				shownFirstSection.setObject(Boolean.TRUE);
			}
		}
	}

	/**
	 * Initialized a previously freshly constructed {@link FinancialTransaction}
	 * @param transaction
	 * @param parameters the {@link PageParameters}
	 */
	public void initializeFinancialTransaction(FinancialTransaction transaction,PageParameters parameters) {
		PersistedUser user=(PersistedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();					
		FinancialTransactionUtil.initializeFinancialTransaction(transaction, this.currencyMetadaService, AuthUtils.getOrganizationForCurrentUser(), AuthUtils.getIsoCountryForCurrentUser());
	}
	
	@SuppressWarnings("unchecked")
	public TransactionPage(final PageParameters parameters) {
		super(parameters);
		
		//override the title
		pageTitle.setDefaultModel(new StringResourceModel(parameters.get(Constants.PARAM_TRANSACTION_TYPE).toString(""), this, null, null));
		
		onUnloadScript = "window.onbeforeunload = function(e) {\n" +
				"   var message = '" + new StringResourceModel("leaveMessage", this, null, null).getObject() + "';\n" +
				"   e = e || window.event;\n" +
				"   if(e) {\n" +
				"       e.returnValue = message;\n" +   // For IE 8 and old Firefox
				"   }\n" +
                "   return message;\n" +
                "};";

        // TODO: check that transactionType in the request parameters is the
		// same as the loaded transaction's type
		FinancialTransaction financialTransaction = null;

		if (!parameters.get(PARAM_TRANSACTION_ID).isNull()) {
			long transactionId = parameters.get(PARAM_TRANSACTION_ID).toLong();
			financialTransaction = financialTransactionService.findOne(transactionId).getEntity();
		} else {
			financialTransaction = getFinancialTransaction();
			initializeFinancialTransaction(financialTransaction, parameters);
		}

		CompoundPropertyModel<FinancialTransaction> model = new CompoundPropertyModel<FinancialTransaction>(
				financialTransaction);

		setModel(model);

		form = new Form("form");
		add(form);

		List<ITabWithKey> tabList = populateTabList(parameters);

		BootstrapJSTabbedPanel<ITabWithKey> bc = new BootstrapJSTabbedPanel<>("bc", tabList)
				.positionTabs(BootstrapJSTabbedPanel.Orientation.RIGHT);
		form.add(bc);

        TransactionPageSubmitButton submitButton = new TransactionPageSubmitButton("submit", new StringResourceModel("button.submit", this, null, null)) {
            private static final long serialVersionUID = -1909494416938537482L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("Submit pressed");
                super.onSubmit(target, form);
                setResponsePage(HomePage.class);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.appendJavaScript(onUnloadScript);
            }
        };
        submitButton.add(new AttributePrepender("onclick", new Model<String>("window.onbeforeunload = null;"), " "));
        form.add(submitButton);

        form.add(new TransactionPageSubmitButton("save", new StringResourceModel("button.save", this, null, null)));

		form.add(new TransactionPageSubmitButton("cancel", new StringResourceModel("button.cancel", this, null, null)) {

			private static final long serialVersionUID = -3097577464142022353L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Cancel pressed");
				setResponsePage(HomePage.class);
			}

		}.setDefaultFormProcessing(false));

		feedbackPanel = new NotificationPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.hideAfter(Duration.seconds(3));
		add(feedbackPanel);
	}

	protected FinancialTransaction getFinancialTransaction() {
		return new FinancialTransaction();
	}

	private List<ITabWithKey> populateTabList(PageParameters parameters) {
		List<Class<? extends Panel>> tabClasses = getTabs();
		ArrayList<ITabWithKey> tabs = new ArrayList<>();
		for (final Class<? extends Panel> p : tabClasses) {
			tabs.add(DefaultTabWithKey.of(p, this,parameters));
		}
		return tabs;
	}

	protected List<Class<? extends Panel>> getTabs() {
		List<Class<? extends Panel>> tabList = new ArrayList<>();
		tabList.add(IdentificationDataTab.class);
		tabList.add(BasicDataTab.class);
		tabList.add(SupplementaryDataTab.class);
		tabList.add(VolumeDataTab.class);
		tabList.add(ForLoansOnlyTab.class);
		return tabList;
	}

	@Override
	protected void onAfterRenderChildren() {
		super.onAfterRenderChildren();

	}

	@Override
	public HashMap<String, RoleActionMapping> getPermissions() {
		return componentPermissions.permissions();
	}

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(OnDomReadyHeaderItem.forScript(
                onUnloadScript));
    }
}
