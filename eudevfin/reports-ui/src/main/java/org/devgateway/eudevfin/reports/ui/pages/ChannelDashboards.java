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
 * @since 6/4/14
 */
@MountPath(value = "/channeldashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ChannelDashboards extends ReportsDashboards {
    private static final Logger logger = Logger.getLogger(ChannelDashboards.class);

    private String countryCurrency = "$";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String currencyParam;
    private String agencyParam;

    @SpringBean
    protected QueryService CdaService;

    @SpringBean
    private FinancialTransactionService financialTransactionService;

    public ChannelDashboards(final PageParameters parameters) {
        // get the reporting year
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            currencyParam = parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).toString();
            if (currencyParam.equals("true")) {
                countryCurrency = ReportsDashboardsUtils.getCurrency();
            }
        } else {
            countryCurrency = ReportsDashboardsUtils.getCurrency();
        }
        if(!parameters.get(ReportsConstants.AGENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            agencyParam = parameters.get(ReportsConstants.AGENCY_PARAM).toString();
        }

        String subTitle = new StringResourceModel("page.subtitle", this, null, null).getObject();
        Label country = new Label("channel", (agencyParam != null ? subTitle + " - " + agencyParam : ""));
        add(country);

        addComponents();
    }

    private void addComponents() {
        addTableList();
        addBarChart();
    }

    protected void addTableList () {
        Label title = new Label("channelTableListTitle", "Net Disbursement - " + tableYear + " - " + countryCurrency + " - full amount");
        add(title);

        Table table = new Table(CdaService, "channelTableList", "channelTableListRows", "channelDashboardsTableList") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRowsTransactions(financialTransactionService,
                        this.rows, this.result, this.rowId, currencyParam, ReportsConstants.isChannel);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        table.setParam("paramChannel", (agencyParam != null ? agencyParam : ""));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
                table.setParam("paramcurrencyDisbursement", ReportsConstants.MDX_NAT_EXTENDED_CURRENCY);
            }
        } else {
            table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            table.setParam("paramcurrencyDisbursement", ReportsConstants.MDX_NAT_EXTENDED_CURRENCY);
        }

        Label firstYear = new Label("firstYear", tableYear + " Disbursement");
        table.getTable().add(firstYear);

        add(table.getTable());
        table.addTableRows();
    }

    private void addBarChart() {
        Label title = new Label("channelBarChartTitle", "Net Disbursement - " + tableYear + " - " + countryCurrency + " - full amount");
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "channelBarChart", "channelDashboardsBarChart") {
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

        stackedBarChart.setParam("paramFIRST_YEAR", Integer.toString(tableYear));
        stackedBarChart.setParam("paramChannel", (agencyParam != null ? agencyParam : ""));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        } else {
            stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
        }

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
}
