/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.mcm.pages;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.exchange.common.domain.HistoricalExchangeRate;
import org.devgateway.eudevfin.exchange.common.service.HistoricalExchangeRateService;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapDeleteButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.CheckBoxField;
import org.devgateway.eudevfin.ui.common.components.DateInputField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.components.FinancialAmountTextInputField;
import org.devgateway.eudevfin.ui.common.components.util.MondrianCDACacheUtil;
import org.devgateway.eudevfin.ui.common.models.DateToLocalDateTimeModel;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.providers.CurrencyUnitProviderFactory;
import org.jadira.usertype.exchangerate.ExchangeRateConstants;
import org.joda.money.CurrencyUnit;
import org.joda.money.ExchangeRate;
import org.joda.time.LocalDateTime;
import org.wicketstuff.annotation.mount.MountPath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

/**
 * @author mihai
 * @since 11.02.14
 */
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
@MountPath(value = "/rate")
public class EditHistoricalExchangeRatePage extends HeaderFooter {

	@SpringBean
	private HistoricalExchangeRateService historicalExchangeRateService;

    @SpringBean
    MondrianCDACacheUtil mondrianCacheUtil;

	@SpringBean
	private CurrencyUnitProviderFactory currencyUnitProviderFactory;

	protected final NotificationPanel feedbackPanel;

	private static final long serialVersionUID = -4276784345759050002L;
	private static final Logger logger = Logger.getLogger(EditHistoricalExchangeRatePage.class);
	public static final String PARAM_ID = "exchangeRateId";
	final HistoricalExchangeRate historicalExchangeRate;

    protected BigDecimal originalRate;

	@SuppressWarnings("unchecked")
	public EditHistoricalExchangeRatePage(final PageParameters parameters) {
		super(parameters);

		Form form = new Form("form");
		Long rateId = null;

		if (!parameters.get(PARAM_ID).isNull()) {
			rateId = parameters.get(PARAM_ID).toLong();
			historicalExchangeRate = historicalExchangeRateService.findOne(rateId).getEntity();
			ExchangeRate rate = historicalExchangeRate.getRate();
			if(rate!=null)
				historicalExchangeRate.setRate(ExchangeRate.of(rate.getBase(), rate.getCounter(), rate.getRate()));
		} else {
			historicalExchangeRate = new HistoricalExchangeRate().rate(ExchangeRate.of(CurrencyUnit.USD,
					CurrencyUnit.USD, BigDecimal.ONE.setScale(8, RoundingMode.CEILING)));
		}

		CompoundPropertyModel<? extends HistoricalExchangeRate> model = new CompoundPropertyModel<HistoricalExchangeRate>(
				historicalExchangeRate);
		setModel(model);

		ChoiceProvider<CurrencyUnit> currencyUnitProvider = this.currencyUnitProviderFactory
				.getCurrencyUnitProviderInstance(CurrencyUnitProviderFactory.ALL_SORTED_CURRENCIES_PROVIDER);

		final DropDownField<CurrencyUnit> baseCurrency = new DropDownField<CurrencyUnit>("baseCurrency",
				new RWComponentPropertyModel<CurrencyUnit>("rate.base"), currencyUnitProvider);
		baseCurrency.required();
		form.add(baseCurrency);

		final DropDownField<CurrencyUnit> counterCurrency = new DropDownField<CurrencyUnit>("counterCurrency",
				new RWComponentPropertyModel<CurrencyUnit>("rate.counter"), currencyUnitProvider);
		counterCurrency.required();
		form.add(counterCurrency);

		final FinancialAmountTextInputField rate = new FinancialAmountTextInputField("rate",
				new RWComponentPropertyModel<BigDecimal>("rate.rate")).required();
		form.add(rate);

		DateInputField date = new DateInputField("date", new DateToLocalDateTimeModel(
				new RWComponentPropertyModel<LocalDateTime>("date")));


		date.required();
		form.add(date);

		DropDownField<String> source = new DropDownField<String>("source", new RWComponentPropertyModel<String>(
				"source"), new TextChoiceProvider<String>() {

			@Override
			protected String getDisplayText(String choice) {
				return choice;
			}

			@Override
			protected Object getId(String choice) {
				return choice;
			}

			@Override
			public void query(String term, int page, Response<String> response) {
				response.add(ExchangeRateConstants.SOURCE_OECD);
				response.add(ExchangeRateConstants.SOURCE_NATIONAL);
			}

			@Override
			public Collection<String> toChoices(Collection<String> ids) {
				return ids;
			}
		});

		source.required();
		form.add(source);

        final CheckBoxField isInverted = new CheckBoxField("isInverted", new RWComponentPropertyModel<Boolean>("isInverted"));
        form.add(isInverted);

        rate.getField().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                originalRate = rate.getField().getModel().getObject();
                historicalExchangeRate.setIsInverted(Boolean.FALSE);

                target.add(isInverted);
            }
        });

        // Needed for Ajax to update it
        isInverted.setOutputMarkupId(true);
        isInverted.setRenderBodyOnly(false);

        isInverted.getField().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (historicalExchangeRate.getRate().getBase().equals(historicalExchangeRate.getRate().getCounter())) {
                    error(new NotificationMessage(new StringResourceModel("notification.equalcounterbase",
                            EditHistoricalExchangeRatePage.this, null, null)));
                    target.add(feedbackPanel);
                    return;
                }

                Boolean isInvertedValue = isInverted.getField().getModel().getObject();
                BigDecimal rateValue = rate.getField().getModel().getObject();

                if (originalRate == null) {
                    originalRate = rateValue;
                }

                BigDecimal invertedValue;
                if (isInvertedValue == Boolean.TRUE) {
                    invertedValue = new BigDecimal(1).divide(rateValue, 3, BigDecimal.ROUND_FLOOR);
                } else {
                    invertedValue = originalRate;
                }

                ExchangeRate newExchangeRate = ExchangeRate.of(baseCurrency.getField().getModel().getObject(),
                        counterCurrency.getField().getModel().getObject(),
                        invertedValue);
                historicalExchangeRate.setRate(newExchangeRate);

                target.add(rate);
            }
        });

        // Needed for Ajax to update it
        rate.setOutputMarkupId(true);
        rate.setRenderBodyOnly(false);

		form.add(new BootstrapSubmitButton("submit", new StringResourceModel("button.submit", this, null, null)) {

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				super.onError(target, form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (historicalExchangeRate.getRate().getBase().equals(historicalExchangeRate.getRate().getCounter())) {
					error(new NotificationMessage(new StringResourceModel("notification.equalcounterbase",
							EditHistoricalExchangeRatePage.this, null, null)));
					target.add(feedbackPanel);
					return;
				}

				logger.info("Submitted ok!");
				logger.info("Object:" + getModel().getObject());
				historicalExchangeRateService.save(historicalExchangeRate);
				mondrianCacheUtil.flushMondrianCDACache();
				setResponsePage(ListHistoricalExchangeRatePage.class);
			}

		});

		form.add(new BootstrapCancelButton("cancel", new StringResourceModel("button.cancel", this, null, null)) {
			private static final long serialVersionUID = -4820749323490316998L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Cancel pressed");
				setResponsePage(ListHistoricalExchangeRatePage.class);
			}

		}.setDefaultFormProcessing(false));

		BootstrapDeleteButton deleteButton=new BootstrapDeleteButton("delete", new StringResourceModel("button.delete", this, null, null)) {
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				logger.info("Rate Deleted!");
				historicalExchangeRateService.delete(historicalExchangeRate);
				mondrianCacheUtil.flushMondrianCDACache();
				setResponsePage(ListHistoricalExchangeRatePage.class);
			}

		};
		form.add(deleteButton);

		add(form);

		feedbackPanel = new NotificationPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.hideAfter(Duration.seconds(4));
		add(feedbackPanel);
	}
}
