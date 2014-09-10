package org.devgateway.eudevfin.mcm.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.exchange.common.domain.ExchangeRateConfiguration;
import org.devgateway.eudevfin.exchange.common.domain.ExchangeRateConfigurationConstants;
import org.devgateway.eudevfin.exchange.common.service.ExchangeRateConfigurationService;
import org.devgateway.eudevfin.mcm.models.OnlineExchangeRateModel;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.CheckBoxField;
import org.devgateway.eudevfin.ui.common.components.TextInputField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 9/9/14
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/onlineexchangerate")
public class OnlineExchangeRatePage extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(OnlineExchangeRatePage.class);

    private TextInputField<String> baseurl;
    private TextInputField<String> key;
    private CheckBoxField jobActive;

    private ExchangeRateConfiguration exchangeRateBaseURL;
    private ExchangeRateConfiguration exchangeRateKey;
    private ExchangeRateConfiguration exchangeJobActivey;

    protected final NotificationPanel feedbackPanel;

    protected OnlineExchangeRateModel onlineExchangeRateModel;

    @SpringBean
    private ExchangeRateConfigurationService exchangeRateConfigurationService;

    public OnlineExchangeRatePage(final PageParameters parameters) {
        super(parameters);

        Label rateNote = new Label("ratenote",
                new StringResourceModel("onlineexchangerate.ratenote", this, null, null).getObject());
        rateNote.setEscapeModelStrings(false);
        add(rateNote);

        Form form = new Form("exchangerateform");

        onlineExchangeRateModel = new OnlineExchangeRateModel();
        CompoundPropertyModel<OnlineExchangeRateModel> model = new CompoundPropertyModel<>(onlineExchangeRateModel);
        form.setModel(model);

        // fetch the open exchange properties
        exchangeRateBaseURL = exchangeRateConfigurationService.
                findByEntityKey(ExchangeRateConfigurationConstants.OPEN_EXCHANGE_BASE_URL).getEntity();
        exchangeRateKey = exchangeRateConfigurationService.
                findByEntityKey(ExchangeRateConfigurationConstants.OPEN_EXCHANGE_KEY).getEntity();
        exchangeJobActivey = exchangeRateConfigurationService.
                findByEntityKey(ExchangeRateConfigurationConstants.EXCHANGE_JOB_ACTIVE).getEntity();

        baseurl = new TextInputField<>("baseurl", new RWComponentPropertyModel<String>("baseurl"), "onlineexchangerate.baseurl");
        baseurl.typeString();
        form.add(baseurl);
        if (exchangeRateBaseURL != null) {
            onlineExchangeRateModel.setBaseurl(exchangeRateBaseURL.getEntitValue());
        }

        key = new TextInputField<>("key", new RWComponentPropertyModel<String>("key"), "onlineexchangerate.key");
        key.typeString();
        form.add(key);
        if (exchangeRateKey != null) {
            onlineExchangeRateModel.setKey(exchangeRateKey.getEntitValue());
        }

        jobActive = new CheckBoxField("jobActive", new RWComponentPropertyModel<Boolean>("jobActive"), "onlineexchangerate.jobActive");
        form.add(jobActive);
        if (exchangeJobActivey != null) {
            if (exchangeJobActivey.getEntitValue().
                    equals(ExchangeRateConfigurationConstants.EXCHANGE_JOB_ACTIVE_TRUE)) {
                onlineExchangeRateModel.setJobActive(Boolean.TRUE);
            } else {
                onlineExchangeRateModel.setJobActive(Boolean.FALSE);
            }
        }

        form.add(new BootstrapSubmitButton("submit", new StringResourceModel("onlineexchangerate.submit", this, null, null)) {
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(feedbackPanel);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                OnlineExchangeRateModel onlineExchangeRateModel = (OnlineExchangeRateModel)form.getModelObject();

                if (exchangeRateBaseURL != null) {
                    exchangeRateBaseURL.setEntitValue(onlineExchangeRateModel.getBaseurl());
                } else {
                    // this is the first time when we save the open exchange configurations
                    exchangeRateBaseURL = new ExchangeRateConfiguration(
                            ExchangeRateConfigurationConstants.OPEN_EXCHANGE_BASE_URL,
                            onlineExchangeRateModel.getBaseurl());
                }
                if (exchangeRateKey != null) {
                    exchangeRateKey.setEntitValue(onlineExchangeRateModel.getKey());
                } else {
                    // this is the first time when we save the open exchange configurations
                    exchangeRateKey = new ExchangeRateConfiguration(
                            ExchangeRateConfigurationConstants.OPEN_EXCHANGE_KEY,
                            onlineExchangeRateModel.getKey());
                }
                if (exchangeJobActivey != null) {
                    if (onlineExchangeRateModel.getJobActive()) {
                        exchangeJobActivey.setEntitValue(ExchangeRateConfigurationConstants.EXCHANGE_JOB_ACTIVE_TRUE);
                    } else {
                        exchangeJobActivey.setEntitValue(ExchangeRateConfigurationConstants.EXCHANGE_JOB_ACTIVE_FALSE);
                    }
                } else {
                    // this is the first time when we save the open exchange configurations
                    exchangeJobActivey = new ExchangeRateConfiguration(
                            ExchangeRateConfigurationConstants.EXCHANGE_JOB_ACTIVE,
                            onlineExchangeRateModel.getJobActive() == true ?
                                    ExchangeRateConfigurationConstants.EXCHANGE_JOB_ACTIVE_TRUE :
                                    ExchangeRateConfigurationConstants.EXCHANGE_JOB_ACTIVE_FALSE);
                }

                // save the new configurations
                exchangeRateConfigurationService.save(exchangeRateBaseURL);
                exchangeRateConfigurationService.save(exchangeRateKey);
                exchangeRateConfigurationService.save(exchangeJobActivey);
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
