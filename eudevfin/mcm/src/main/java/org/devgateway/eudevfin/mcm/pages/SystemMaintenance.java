/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.mcm.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.common.spring.integration.NullableWrapper;
import org.devgateway.eudevfin.exchange.common.domain.ExchangeRateConfiguration;
import org.devgateway.eudevfin.exchange.common.domain.ExchangeRateConfigurationConstants;
import org.devgateway.eudevfin.exchange.common.service.ExchangeRateConfigurationService;
import org.devgateway.eudevfin.mcm.models.SystemMaintenanceModel;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2/7/14
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/systemmaintenance")
public class SystemMaintenance extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(SystemMaintenance.class);

    private TextInputField<String> baseurl;
    private TextInputField<String> key;
    private ExchangeRateConfiguration exchangeRateBaseURL;
    private ExchangeRateConfiguration exchangeRateKey;

    protected final NotificationPanel feedbackPanel;

    protected SystemMaintenanceModel systemMaintenanceModel;

    @SpringBean
    private ExchangeRateConfigurationService exchangeRateConfigurationService;

	public SystemMaintenance(final PageParameters parameters) {
        super(parameters);

        Form form = new Form("systemform");

        systemMaintenanceModel = new SystemMaintenanceModel();
        CompoundPropertyModel<SystemMaintenanceModel> model = new CompoundPropertyModel<>(systemMaintenanceModel);
        form.setModel(model);

        // fetch the open exchange properties
        exchangeRateBaseURL = exchangeRateConfigurationService.
                findByEntityKey(ExchangeRateConfigurationConstants.OPEN_EXCHANGE_BASE_URL).getEntity();
        exchangeRateKey = exchangeRateConfigurationService.
                findByEntityKey(ExchangeRateConfigurationConstants.OPEN_EXCHANGE_KEY).getEntity();

        baseurl = new TextInputField<>("baseurl", new RWComponentPropertyModel<String>("baseurl"), "systemmaintenance.baseurl");
        baseurl.typeString();
        form.add(baseurl);
        if (exchangeRateBaseURL != null) {
            systemMaintenanceModel.setBaseurl(exchangeRateBaseURL.getEntitValue());
        }

        key = new TextInputField<>("key", new RWComponentPropertyModel<String>("key"), "systemmaintenance.key");
        key.typeString();
        form.add(key);
        if (exchangeRateKey != null) {
            systemMaintenanceModel.setKey(exchangeRateKey.getEntitValue());
        }

        form.add(new BootstrapSubmitButton("submit", new StringResourceModel("systemmaintenance.submit", this, null, null)) {
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(feedbackPanel);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                SystemMaintenanceModel systemMaintenanceModel = (SystemMaintenanceModel)form.getModelObject();

                if (exchangeRateBaseURL != null) {
                    exchangeRateBaseURL.setEntitValue(systemMaintenanceModel.getBaseurl());
                } else {
                    // this is the first time when we save the open exchange configurations
                    exchangeRateBaseURL = new ExchangeRateConfiguration(
                            ExchangeRateConfigurationConstants.OPEN_EXCHANGE_BASE_URL,
                            systemMaintenanceModel.getBaseurl());
                }
                if (exchangeRateKey != null) {
                    exchangeRateKey.setEntitValue(systemMaintenanceModel.getKey());
                } else {
                    // this is the first time when we save the open exchange configurations
                    exchangeRateKey = new ExchangeRateConfiguration(
                            ExchangeRateConfigurationConstants.OPEN_EXCHANGE_KEY,
                            systemMaintenanceModel.getKey());
                }

                logger.error(exchangeRateBaseURL);
                logger.error(exchangeRateKey);

                // save the new configurations
                exchangeRateConfigurationService.save(exchangeRateBaseURL);
                exchangeRateConfigurationService.save(exchangeRateKey);
            }
        });

        // also add the feedback panel
        feedbackPanel = new NotificationPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        form.add(feedbackPanel);

        add(form);
    }

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
	}
}
