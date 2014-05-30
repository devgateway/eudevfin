package org.devgateway.eudevfin.reports.ui.pages;

import com.googlecode.wickedcharts.highcharts.options.DataLabels;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
import com.googlecode.wickedcharts.highcharts.options.Tooltip;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.StackedBarChart;
import org.devgateway.eudevfin.reports.ui.components.Table;
import org.devgateway.eudevfin.reports.ui.scripts.Dashboards;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author idobre
 * @since 4/16/14
 */
@MountPath(value = "/reportsimplementationstatusdashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsImplementationStatusDashboards extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(ReportsImplementationStatusDashboards.class);

    private String countryCurrency = "$";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String currencyParam;
    private String expenditureParam;
    private String startingYearParam;
    private String completionYearParam;
    private String valueParam;
    private String budgetCodesParam;
    private String coFinancingParam;

    @SpringBean
    QueryService CdaService;

    public ReportsImplementationStatusDashboards(final PageParameters parameters) {
        // get a default year if it's not specified
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            currencyParam = parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).toString();
            if (currencyParam.equals("true")) {
                countryCurrency = ReportsDashboardsUtils.getCurrency();
            }
        }

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.EXPENDITURE_PARAM).equals(StringValue.valueOf((String) null))) {
            expenditureParam = parameters.get(ReportsConstants.EXPENDITURE_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.STARTINGYEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            startingYearParam = parameters.get(ReportsConstants.STARTINGYEAR_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.COMPLETIONYEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            completionYearParam = parameters.get(ReportsConstants.COMPLETIONYEAR_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.VALUE_PARAM).equals(StringValue.valueOf((String) null))) {
            valueParam = parameters.get(ReportsConstants.VALUE_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.BUDGETCODES_PARAM).equals(StringValue.valueOf((String) null))) {
            budgetCodesParam = parameters.get(ReportsConstants.BUDGETCODES_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.COFINANCING_PARAM).equals(StringValue.valueOf((String) null))) {
            coFinancingParam = parameters.get(ReportsConstants.COFINANCING_PARAM).toString();
        }

        addComponents();
    }

    private void addComponents() {
        Label title = new Label("title", new StringResourceModel("reportsimplementationstatusdashboards.title", this, null, null));
        add(title);

        addImplementationStatusTable();
        addImplementationStatusChart();
    }

    private void addImplementationStatusTable () {
        String expenditure = "";
        if (expenditureParam != null && expenditureParam.equals("true")) {
            expenditure = new StringResourceModel("commitment", this, null, null).getObject();
        } else {
            expenditure = new StringResourceModel("disbursement", this, null, null).getObject();
        }
        Label title = new Label("implementationTableTitle", "Implementation Status - " + expenditure+ " - " +
                (tableYear - 1) + "-" + tableYear + " - " + countryCurrency + " - full amount");
        add(title);

        Table table = new Table(CdaService, "implementationTable", "implementationTableRows", "customDashboardsImplementationStatusTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRowsWithoutMainCategory(this.rows, this.result, this.rowId);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                if (expenditureParam != null && expenditureParam.equals("true")) {
                    table.setParam("paramcurrency", ReportsConstants.MDX_NAT_COMMITMENT_CURRENCY);
                } else {
                    table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
                }
            } else {
                if (expenditureParam != null && expenditureParam.equals("true")) {
                    table.setParam("paramcurrency", ReportsConstants.MDX_USD_COMMITMENT_CURRENCY);
                }
            }
        } else {
            if (expenditureParam != null && expenditureParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_COMMITMENT_CURRENCY);
            } else {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }
        if (valueParam != null) {
            if (valueParam.equals("true")) {
                table.setParam("paramvalueParam", "> 500000");
            } else {
                if (valueParam.equals("false")) {
                    table.setParam("paramvalueParam", "< 500000");
                }
            }
        }
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            table.setParam("paramCOFINANCED", "[1]");
        }
        if(startingYearParam != null) {
            table.setParam("paramstartingYear", startingYearParam);
        }
        if(completionYearParam != null) {
            table.setParam("paramcompletionYear", completionYearParam);
        }

        Label firstYear = new Label("firstYear", tableYear - 1);
        table.getTable().add(firstYear);
        Label secondYear = new Label("secondYear", tableYear);
        table.getTable().add(secondYear);

        add(table.getTable());
        table.addTableRows();

        Label currencyFirstYear = new Label("currencyFirstYear", countryCurrency);
        table.getTable().add(currencyFirstYear);
        Label currencySecondYear = new Label("currencySecondYear", countryCurrency);
        table.getTable().add(currencySecondYear);
    }

    private void addImplementationStatusChart () {
        String expenditure = "";
        if (expenditureParam != null && expenditureParam.equals("true")) {
            expenditure = new StringResourceModel("commitment", this, null, null).getObject();
        } else {
            expenditure = new StringResourceModel("disbursement", this, null, null).getObject();
        }
        Label title = new Label("implementationChartTitle", "Implementation Status - " + expenditure+ " - " +
                (tableYear - 1) + "-" + tableYear + " - " + countryCurrency + " - full amount");
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "implementationChart", "customDashboardsImplementationStatusChart") {
            @Override
            public List<List<Float>> getResultSeriesAsList () {
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processChartRows(this.runQuery(), getOptions());
            }
        };

        // add MDX queries parameters
        stackedBarChart.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        stackedBarChart.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                if (expenditureParam != null && expenditureParam.equals("true")) {
                    stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_NAT_COMMITMENT_CURRENCY);
                } else {
                    stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
                }
            } else {
                if (expenditureParam != null && expenditureParam.equals("true")) {
                    stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_USD_COMMITMENT_CURRENCY);
                }
            }
        } else {
            if (expenditureParam != null && expenditureParam.equals("true")) {
                stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_NAT_COMMITMENT_CURRENCY);
            } else {
                stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }
        if (valueParam != null) {
            if (valueParam.equals("true")) {
                stackedBarChart.setParam("paramvalueParam", "> 500000");
            } else {
                if (valueParam.equals("false")) {
                    stackedBarChart.setParam("paramvalueParam", "< 500000");
                }
            }
        }
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            stackedBarChart.setParam("paramCOFINANCED", "[1]");
        }
        if(startingYearParam != null) {
            stackedBarChart.setParam("paramstartingYear", startingYearParam);
        }
        if(completionYearParam != null) {
            stackedBarChart.setParam("paramcompletionYear", completionYearParam);
        }

        List<List<Float>> resultSeries = stackedBarChart.getResultSeriesAsList();
        stackedBarChart.getOptions().setPlotOptions(new PlotOptionsChoice().
                setBar(new PlotOptions().
                        setMinPointLength(5).
                        setDataLabels(new DataLabels().
                                setEnabled(Boolean.TRUE))));
        stackedBarChart.getOptions().setTooltip(new Tooltip().setValueSuffix(" millions").setPercentageDecimals(2));
        // add 35px height for each row
        int numberOfRows = Math.max(resultSeries.get(0).size(), resultSeries.get(1).size());
        stackedBarChart.getOptions().getChartOptions().setHeight(300 + 35 * numberOfRows);

        stackedBarChart.getOptions().addSeries(new SimpleSeries()
                .setName("Year " + (tableYear - 1))
                .setData(resultSeries.get(0).toArray(new Float[resultSeries.get(0).size()])));

        stackedBarChart.getOptions().addSeries(new SimpleSeries()
                .setName("Year " + tableYear)
                .setData(resultSeries.get(1).toArray(new Float[resultSeries.get(1).size()])));

        add(stackedBarChart.getChart());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "Dashboards.utilities.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "highcharts-no-data-to-display.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "canvg-1.3/rgbcolor.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "canvg-1.3/StackBlur.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "canvg-1.3/canvg.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "html2canvas.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "jspdf.min.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "reports.js")));
    }
}
