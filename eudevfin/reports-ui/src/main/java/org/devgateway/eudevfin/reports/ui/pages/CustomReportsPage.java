package org.devgateway.eudevfin.reports.ui.pages;

import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Response;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.RWComponentPropertyModel;
import org.devgateway.eudevfin.ui.common.components.BootstrapCancelButton;
import org.devgateway.eudevfin.ui.common.components.BootstrapSubmitButton;
import org.devgateway.eudevfin.ui.common.components.CheckBoxField;
import org.devgateway.eudevfin.ui.common.components.DropDownField;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.devgateway.eudevfin.ui.common.providers.AreaChoiceProvider;
import org.devgateway.eudevfin.ui.common.providers.CategoryProviderFactory;
import org.json.JSONException;
import org.json.JSONWriter;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author idobre
 * @since 4/1/14
 */

@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
@MountPath(value = "/customreports")
public class CustomReportsPage extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(CustomReportsPage.class);

    @SpringBean
    private AreaChoiceProvider areaProvider;

    @SpringBean
    private CategoryProviderFactory categoryFactory;

    @SpringBean
    private CustomFinancialTransactionService txService;

    private List<Integer> possibleYears;

    private List<String> possibleGeopraphy;

    protected final NotificationPanel feedbackPanel;

    public CustomReportsPage () {
        final Form form = new Form("form");

        CustomReportsForm customReportsForm = new CustomReportsForm();
        CompoundPropertyModel<CustomReportsForm> model = new CompoundPropertyModel<>(customReportsForm);
        setModel(model);

        possibleGeopraphy = txService.findDistinctReportingGeopraphy();
        Collections.sort(possibleGeopraphy);
        DropDownField<String> geopraphy = new DropDownField<>("geopraphy", new RWComponentPropertyModel<String>("geopraphy"),
                new ChoiceProvider<String>() {
            @Override
            public void query(final String term, final int page, final Response<String> response) {
                final List<String> ret = new ArrayList<>();
                List<String> values;
                if (CustomReportsPage.this.possibleGeopraphy != null && CustomReportsPage.this.possibleGeopraphy.size() > 0) {
                    values = CustomReportsPage.this.possibleGeopraphy;
                } else {
                    values = new ArrayList<>(1);
                    values.add("");
                }
                for (final String el : values) {
                    if (el.toString().startsWith(term)) {
                        ret.add(el);
                    }
                }
                response.addAll(ret);
            }

            @Override
            public void toJson(final String choice, final JSONWriter writer) throws JSONException {
                writer.key("id").value(choice.toString()).key("text").value(choice);
            }

            @Override
            public Collection<String> toChoices(final Collection<String> ids) {
                final List<String> ret = new ArrayList<>();
                if (ids != null) {
                    for (final String id : ids) {
                        try {
                            ret.add(id);
                        } catch (final NumberFormatException e) {
                            logger.error(e.getMessage());
                        }
                    }
                }
                return ret;
            }
        });

        DropDownField<Area> recipient = new DropDownField<>("recipient",
                new RWComponentPropertyModel<Area>("recipient"), areaProvider);

        DropDownField<Category> sector = new DropDownField<>("sector", new RWComponentPropertyModel<Category>("sector"),
                categoryFactory.get(CategoryConstants.ALL_SECTOR_TAG));
        add(sector);

        possibleYears = txService.findDistinctReportingYears();
        Collections.sort(possibleYears);
        DropDownField<Integer> year = new DropDownField<>("year", new RWComponentPropertyModel<Integer>("year"),
                new ChoiceProvider<Integer>() {

            @Override
            public void query(final String term, final int page, final Response<Integer> response) {
                final List<Integer> ret = new ArrayList<>();
                List<Integer> values;
                if (CustomReportsPage.this.possibleYears != null && CustomReportsPage.this.possibleYears.size() > 0) {
                    values = CustomReportsPage.this.possibleYears;
                } else {
                    values = new ArrayList<>(1);
                    values.add(Calendar.getInstance().get(Calendar.YEAR) - 1);
                }
                for (final Integer el : values) {
                    if (el.toString().startsWith(term)) {
                        ret.add(el);
                    }
                }
                response.addAll(ret);
            }

            @Override
            public void toJson(final Integer choice, final JSONWriter writer) throws JSONException {
                writer.key("id").value(choice.toString()).key("text").value(choice.toString());
            }

            @Override
            public Collection<Integer> toChoices(final Collection<String> ids) {
                final List<Integer> ret = new ArrayList<>();
                if (ids != null) {
                    for (final String id : ids) {
                        try {
                            final Integer parsedInt = Integer.parseInt(id);
                            ret.add(parsedInt);
                        } catch (final NumberFormatException e) {
                            logger.error(e.getMessage());
                        }
                    }
                }
                return ret;
            }
        });

        CheckBoxField CoFinancingTransactionsOnly = new CheckBoxField("cofinancingtransactionsonly", new RWComponentPropertyModel<Boolean>("coFinancingTransactionsOnly"));
        CheckBoxField CPAOnly = new CheckBoxField("cpaonly", new RWComponentPropertyModel<Boolean>("CPAOnly"));

        form.add(geopraphy);
        form.add(recipient);
        form.add(sector);
        form.add(year);
        form.add(CoFinancingTransactionsOnly);
        form.add(CPAOnly);

        form.add(new BootstrapSubmitButton("submit", new StringResourceModel("button.submit", this, null, null)) {
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(feedbackPanel);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("Submitted ok!");
                logger.info("====================================");

                CustomReportsForm customReportsForm = (CustomReportsForm)CustomReportsPage.this.getModelObject();
//                logger.info(customReportsForm.getRecipient().getName());
//                logger.info(customReportsForm.getSector().getName());
//                logger.info(customReportsForm.getYear());
//                logger.info(customReportsForm.getCoFinancingTransactionsOnly());
//                logger.info(customReportsForm.getCPAOnly());

                PageParameters pageParameters = new PageParameters();
                pageParameters.add("msg", "this is parameter value");
                setResponsePage(CustomDashboardsCountrySector.class, pageParameters);
            }
        });

        form.add(new BootstrapCancelButton("reset", new StringResourceModel("button.reset", this, null, null)) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("Reset pressed");
                setResponsePage(CustomReportsPage.class);
            }
        });

        add(form);

        // also add the feedback panel
        feedbackPanel = new NotificationPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.hideAfter(Duration.seconds(3));
        add(feedbackPanel);
    }
}
