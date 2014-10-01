package org.devgateway.eudevfin.reports.ui.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.service.CustomFinancialTransactionService;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author idobre
 * @since 10/1/14
 */
@MountPath(value = "/publishreports")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_SUPERVISOR)
public class PublishReports extends HeaderFooter<Object> {
    private final String REPORT_AQ = "AQ";
    private final String REPORT_DATASOURCE_AQ = "Advance Questionnaire input form";
    private final String REPORT_DATASOURCE_CRS = "CRS++ input form";

    @SpringBean
    private CustomFinancialTransactionService txService;

    public PublishReports (final PageParameters parameters) {
        super(parameters);
        String reportType = parameters.get("reportType").toString("");

        pageTitle.setDefaultModel(new StringResourceModel("navbar.reports.export." + reportType, this, null, null));

        final List<Integer> years = this.txService.findDistinctReportingYears();
        // if the list of years is empty add the current year (ODAEU-322)
        if (years.size() == 0) {
            years.add(Calendar.getInstance().get(Calendar.YEAR) - 1);
        }
        DropDownChoice<Integer> year = new DropDownChoice<Integer>("reportYear", years,
                new IChoiceRenderer<Integer>() {
                    private static final long serialVersionUID = 8801460052416367398L;

                    public Object getDisplayValue(Integer value)
                    {
                        return value;
                    }

                    public String getIdValue(Integer object, int index) {
                        return String.valueOf(years.get(index));
                    }
                });
        add(year);

        List<String> dataSources = new ArrayList<String>();
        dataSources.add(REPORT_DATASOURCE_AQ);
        dataSources.add(REPORT_DATASOURCE_CRS);

        DropDownChoice<String> dataSource = new DropDownChoice<String>("dataSource", dataSources,
                new IChoiceRenderer<String>() {

                    @Override
                    public Object getDisplayValue(String object) {
                        return object;
                    }

                    @Override
                    public String getIdValue(String object, int index) {
                        return object;
                    }
                });


        WebMarkupContainer dataSourceGroup = new WebMarkupContainer("dataSourceGroup");
        if(!reportType.equalsIgnoreCase(REPORT_AQ)) {
            dataSourceGroup.setVisibilityAllowed(false);
        }
        dataSourceGroup.add(dataSource);
        add(dataSourceGroup);

        HiddenField<String> field = new HiddenField<String>("reportType", Model.of(""));
        field.setModelValue(new String[]{reportType});
        add(field);
    }
}
