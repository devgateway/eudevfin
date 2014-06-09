package org.devgateway.eudevfin.reports.ui.pages;

import com.googlecode.wickedcharts.highcharts.options.DataLabels;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Tooltip;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.financial.service.FinancialTransactionService;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.PieChart;
import org.devgateway.eudevfin.reports.ui.components.StackedBarChart;
import org.devgateway.eudevfin.reports.ui.components.Table;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author idobre
 * @since 6/2/14
 */

@MountPath(value = "/institutiondashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class InstitutionDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(InstitutionDashboards.class);

    private int tableYear;
    private String institutionParam;

    @SpringBean
    protected QueryService CdaService;

    @SpringBean
    private FinancialTransactionService financialTransactionService;

    public InstitutionDashboards(final PageParameters parameters) {
        // get the reporting year
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        if(!parameters.get(ReportsConstants.INSTITUTION_PARAM).equals(StringValue.valueOf((String) null))) {
            institutionParam = parameters.get(ReportsConstants.INSTITUTION_PARAM).toString();
        }

        Label country = new Label("institution", (institutionParam != null ? institutionParam : ""));
        add(country);

        addComponents();
    }

    private void addComponents() {
        addTable();
        addPieChart();
        addBarChart();
        addTableList();
    }

    protected void addTable () {
        Label title = new Label("institutionTableTitle", new StringResourceModel("InstitutionDashboards.institutionChart", this, null, null));
        add(title);

        Table table = new Table(CdaService, "institutionTable", "institutionTableRows", "institutionDashboardsTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRowsWithTotalOneYear(this.rows, this.result, this.rowId, true,
                        ReportsConstants.isCountry, true);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        table.setParam("paramInstitution", (institutionParam != null ? institutionParam : ""));

        Label firstYear = new Label("firstYear", tableYear);
        table.getTable().add(firstYear);

        add(table.getTable());
        table.addTableRows();
    }

    private void addPieChart() {
        Label title = new Label("institutionPieChartTitle", new StringResourceModel("InstitutionDashboards.institutionChart", this, null, null));
        add(title);

        PieChart pieChart = new PieChart(CdaService, "institutionPieChart", "institutionDashboardsPieChart") {
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

        // add MDX queries parameters
        pieChart.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        pieChart.setParam("paramInstitution", (institutionParam != null ? institutionParam : ""));

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

    private void addBarChart() {
        Label title = new Label("institutionBarChartTitle", new StringResourceModel("InstitutionDashboards.institutionChart", this, null, null));
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "institutionBarChart", "institutionDashboardsBarChart") {
            @Override
            public List<List<Float>> getResultSeriesAsList () {
                this.result = this.runQuery();

                List<List<Float>> resultSeries = new ArrayList<>();
                List<String> resultCategories = new ArrayList<>();

                List<Float> firstYearList = new ArrayList<>();
                resultSeries.add(firstYearList);

                for (List<String> item : result.getResultset()) {
                    resultCategories.add(item.get(0));

                    if (result.getMetadata().get(1).getColName().equals("FIRST YEAR")) {
                        if (item.size() > 1 && item.get(1) != null) {
                            resultSeries.get(0).add(Float.parseFloat(item.get(1)));
                        } else {
                            resultSeries.get(0).add((float) 0);
                        }
                    }
                }

                getOptions().getxAxis().get(0).setCategories(new ArrayList<>(resultCategories));

                return resultSeries;
            }
        };

        // add MDX queries parameters
        stackedBarChart.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        stackedBarChart.setParam("paramInstitution", (institutionParam != null ? institutionParam : ""));

        List<List<Float>> resultSeries = stackedBarChart.getResultSeriesAsList();
        stackedBarChart.getOptions().setPlotOptions(new PlotOptionsChoice().
                setBar(new PlotOptions().
                        setMinPointLength(5).
                        setDataLabels(new DataLabels().
                                setEnabled(Boolean.TRUE))));
        stackedBarChart.getOptions().setTooltip(new Tooltip().setValueSuffix(" millions").setPercentageDecimals(2));
        // add 35px height for each row
        int numberOfRows = resultSeries.get(0).size();
        stackedBarChart.getOptions().getChartOptions().setHeight(300 + 35 * numberOfRows);

        stackedBarChart.getOptions().addSeries(new SimpleSeries()
                .setName("Year " + (tableYear))
                .setData(resultSeries.get(0).toArray(new Float[resultSeries.get(0).size()]))
                .setColor(new HexColor("#3D96AE").brighten(new Float(-0.1))));

        add(stackedBarChart.getChart());
    }

    protected void addTableList () {
        Label title = new Label("institutionTableListTitle", new StringResourceModel("InstitutionDashboards.institutionChart", this, null, null));
        add(title);

        Table table = new Table(CdaService, "institutionTableList", "institutionTableListRows", "institutionDashboardsTableList") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRowsTransactions(financialTransactionService,
                        this.rows, this.result, this.rowId, ReportsConstants.isInstitution);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        table.setParam("paramInstitution", (institutionParam != null ? institutionParam : ""));

        Label firstYear = new Label("firstYear", tableYear + " Disbursement");
        table.getTable().add(firstYear);

        add(table.getTable());
        table.addTableRows();
    }
}