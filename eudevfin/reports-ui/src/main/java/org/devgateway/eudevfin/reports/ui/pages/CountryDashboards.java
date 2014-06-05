package org.devgateway.eudevfin.reports.ui.pages;

import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.PieChart;
import org.springframework.stereotype.Component;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author idobre
 * @since 6/2/14
 */
@MountPath(value = "/countrydashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class CountryDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(CountryDashboards.class);

    private int tableYear;
    private String recipientParam;

    @SpringBean
    protected QueryService CdaService;

    public CountryDashboards(final PageParameters parameters) {
        // get the reporting year
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        if(!parameters.get(ReportsConstants.RECIPIENT_PARAM).equals(StringValue.valueOf((String) null))) {
            recipientParam = parameters.get(ReportsConstants.RECIPIENT_PARAM).toString();
        }

        Label country = new Label("country", (recipientParam != null ? recipientParam : ""));
        add(country);

        addComponents();
    }

    private void addComponents() {

        addChart();
    }

    protected void addChart () {
        Label title = new Label("countryChartTitle", new StringResourceModel("CountryDashboards.countryChart", this, null, null));
        add(title);

        PieChart pieChart = new PieChart(CdaService, "countryChart", "countryDashboardsChart") {
            @Override
            public List<Point> getResultSeries () {
                this.result = this.runQuery();
                List<Point> resultSeries = new ArrayList<>();

                for (List<String> item : result.getResultset()) {
                    resultSeries.add(new Point(item.get(0), Float.parseFloat(item.get(1))));
                }

                return resultSeries;
            }
        };

        pieChart.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        pieChart.setParam("paramCountry", (recipientParam != null ? recipientParam : ""));

        Options options = pieChart.getOptions();
        // check if we have a result and make the chart slightly higher
        if (pieChart.getResultSeries().size() != 0) {
            options.getChartOptions().setHeight(350);
        }

        options.addSeries(new PointSeries()
                .setType(SeriesType.PIE)
                .setData(pieChart.getResultSeries()));
        add(pieChart.getChart());
    }
}
