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
 * @since 3/27/14
 */
@MountPath(value = "/reportscountrysectordashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsCountrySectorDashboards extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(ReportsCountrySectorDashboards.class);

    /*
     * variables used to dynamically create the MDX queries
     */
    // variables used for 'country table'
    private String countryTableRowSet = "{Hierarchize({[Country].[Geography].Members, [Country].[Name].Members, [Country].[TOTAL]})}";
    private String countryTableGeography = "{Hierarchize({{[Country].[__GEOGRAPHY__]}, Filter({{[Country].[Name].Members}}, " +
            "(Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0))}), [Country].[TOTAL]}";
    private String countryTableRecipient = "{Hierarchize({{[Country].[Name].[__RECIPIENT__]}}), [Country].[Total]}";
    private String countryTableGeographyRecipient = "{Hierarchize({{[Country].[__GEOGRAPHY__]}, [Country].[Name].[__RECIPIENT__], [Country].[TOTAL]})}";
    private String countryTableTotal = "MEMBER [Country].[Total] AS SUM([Country].[Name].Members __CURRENCY__)";
    private String countryTableTotalGeography = "MEMBER [Country].[Total] AS SUM([Country].__GEOGRAPHY__ __CURRENCY__)";

    // variables used for 'country chart'
    private String countryChartRowSet = "{[Country].[Name].Members}";
    private String countryChartGeography = "{Filter({{[Country].[Name].Members}}, (Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0))}";
    private String countryChartRecipient = "{[Country].[Name].[__RECIPIENT__]}";

    // variables used for 'sector table'
    private String sectorTableRowSet = "{Hierarchize({[SectorHierarchy].[ParentName].Members, [SectorHierarchy].[Name].Members, [SectorHierarchy].[TOTAL]})}";
    private String sectorTableSector = "{Hierarchize({[SectorHierarchy].[Name].[__SECTOR__], [SectorHierarchy].[TOTAL]})}";
    private String sectorTableParentSector = "{Hierarchize({{[SectorHierarchy].[__PARENT_SECTOR__]}, Filter({{[SectorHierarchy].[Name].Members}}, " +
            "(Exists(Ancestor([SectorHierarchy].CurrentMember, [SectorHierarchy].[ParentName]), {[SectorHierarchy].[__PARENT_SECTOR__]}).Count  > 0))}), [SectorHierarchy].[TOTAL]}";

    private String sectorTableTotal = "MEMBER[SectorHierarchy].[Total] AS SUM([SectorHierarchy].[Name].Members __CURRENCY__)";
    private String sectorTableTotalSector = "MEMBER[SectorHierarchy].[Total] AS SUM([SectorHierarchy].[Name].[__SECTOR__] __CURRENCY__)";
    private String sectorTableTotalParentSector = "MEMBER [SectorHierarchy].[Total] AS SUM([SectorHierarchy].[__PARENT_SECTOR__] __CURRENCY__)";

    // variables used for 'sector chart'
    private String sectorChartRowSet = "{[SectorHierarchy].[Name].Members}";
    private String sectorChartSector = "{[SectorHierarchy].[Name].[__SECTOR__]}";
    private String sectorChartParentSector = "{Filter({{[SectorHierarchy].[Name].Members}}, (Exists(Ancestor([SectorHierarchy].CurrentMember, [SectorHierarchy].[ParentName]), {[SectorHierarchy].[__PARENT_SECTOR__]}).Count  > 0))}";

    private String countryCurrency = "$";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String currencyParam;
    private String geographyParam;
    private String recipientParam;
    private String sectorParam;
    private String isRootSectorParam;
    private String yearParam;
    private String coFinancingParam;
    private String CPAOnlyParam;

    @SpringBean
    protected QueryService CdaService;

    public ReportsCountrySectorDashboards(final PageParameters parameters) {
        // get a default year if it's not specified
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).equals(StringValue.valueOf((String) null))) {
            currencyParam = parameters.get(ReportsConstants.ISNATIONALCURRENCY_PARAM).toString();
            if (currencyParam.equals("true")) {
                countryCurrency = ReportsDashboardsUtils.getCurrency();
            }
        }
        if(!parameters.get(ReportsConstants.GEOGRAPHY_PARAM).equals(StringValue.valueOf((String) null))) {
            geographyParam = parameters.get(ReportsConstants.GEOGRAPHY_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.RECIPIENT_PARAM).equals(StringValue.valueOf((String) null))) {
            recipientParam = parameters.get(ReportsConstants.RECIPIENT_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.SECTOR_PARAM).equals(StringValue.valueOf((String) null))) {
            sectorParam = parameters.get(ReportsConstants.SECTOR_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.ISROOTSECTOR_PARAM).equals(StringValue.valueOf((String) null))) {
            isRootSectorParam = parameters.get(ReportsConstants.ISROOTSECTOR_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.YEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            yearParam = parameters.get(ReportsConstants.YEAR_PARAM).toString();
            tableYear = Integer.parseInt(yearParam);
        }
        if(!parameters.get(ReportsConstants.COFINANCING_PARAM).equals(StringValue.valueOf((String) null))) {
            coFinancingParam = parameters.get(ReportsConstants.COFINANCING_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.CPAONLY_PARAM).equals(StringValue.valueOf((String) null))) {
            CPAOnlyParam = parameters.get(ReportsConstants.CPAONLY_PARAM).toString();
        }

        /*
         * create the MDX queries based on the filters
         */
        if (geographyParam != null && recipientParam != null) {
            // calculate 'country table' row set
            countryTableGeographyRecipient = countryTableGeographyRecipient.replaceAll("__GEOGRAPHY__", geographyParam);
            countryTableGeographyRecipient = countryTableGeographyRecipient.replaceAll("__RECIPIENT__", recipientParam);
            countryTableRowSet = countryTableGeographyRecipient;

            // calculate 'country table' Total value
            // use the region to calculate the total amount
            countryTableTotalGeography = countryTableTotalGeography.replaceAll("__GEOGRAPHY__", "[" + geographyParam + "]");
            countryTableTotal = countryTableTotalGeography;

            // calculate 'country chart' row set
            countryChartRecipient = countryChartRecipient.replaceAll("__RECIPIENT__", recipientParam);
            countryChartRowSet = countryChartRecipient;
        } else {
            if (geographyParam != null) {
                // calculate 'country table' row set
                countryTableGeography = countryTableGeography.replaceAll("__GEOGRAPHY__", geographyParam);
                countryTableRowSet = countryTableGeography;

                // calculate 'country table' Total value
                countryTableTotalGeography = countryTableTotalGeography.replaceAll("__GEOGRAPHY__", "[" + geographyParam + "]");
                countryTableTotal = countryTableTotalGeography;

                // calculate 'country chart' row set
                countryChartGeography = countryChartGeography.replaceAll("__GEOGRAPHY__", geographyParam);
                countryChartRowSet = countryChartGeography;
            } else {
                if (recipientParam != null) {
                    // calculate 'country table' row set
                    countryTableRecipient = countryTableRecipient.replaceAll("__RECIPIENT__", recipientParam);
                    countryTableRowSet = countryTableRecipient;

                    // calculate 'country table' Total value
                    countryTableTotalGeography = countryTableTotalGeography.replaceAll("__GEOGRAPHY__", "[Name].[" + recipientParam + "]");
                    countryTableTotal = countryTableTotalGeography;

                    // calculate 'country chart' row set
                    countryChartRecipient = countryChartRecipient.replaceAll("__RECIPIENT__", recipientParam);
                    countryChartRowSet = countryChartRecipient;
                }
            }
        }

        if(sectorParam != null) {
            // calculate 'sector table' row set
            if (isRootSectorParam != null && isRootSectorParam.equals("true")) {
                sectorTableParentSector = sectorTableParentSector.replaceAll("__PARENT_SECTOR__", sectorParam);
                sectorTableRowSet = sectorTableParentSector;

                sectorTableTotalParentSector = sectorTableTotalParentSector.replaceAll("__PARENT_SECTOR__", sectorParam);
                sectorTableTotal = sectorTableTotalParentSector;

                sectorChartParentSector = sectorChartParentSector.replaceAll("__PARENT_SECTOR__", sectorParam);
                sectorChartRowSet = sectorChartParentSector;
            } else {
                sectorTableSector = sectorTableSector.replaceAll("__SECTOR__", sectorParam);
                sectorTableRowSet = sectorTableSector;

                // calculate 'sector table' Total value
                sectorTableTotalSector = sectorTableTotalSector.replaceAll("__SECTOR__", sectorParam);
                sectorTableTotal = sectorTableTotalSector;

                // calculate 'sector chart' row set
                sectorChartSector = sectorChartSector.replaceAll("__SECTOR__", sectorParam);
                sectorChartRowSet = sectorChartSector;
            }
        }

        addComponents();
    }

    private void addComponents() {
        Label title = new Label("title", new StringResourceModel("reportscountrysectordashboards.title", this, null, null));
        add(title);

        addCountryTable();
        addCountryChart();
        addSectorTable();
        addSectorChart();
    }

    private void addCountryTable () {
        Label title = new Label("countryTableTitle", "Net Disbursement by country - " + (tableYear - 1) + "-" + tableYear +
                " - " + countryCurrency + " - full amount");
        add(title);

        Table table = new Table(CdaService, "countryTable", "countryTableRows", "customDashboardsCountryTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRows(this.rows, this.result, this.rowId, ReportsConstants.isCountry);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        table.setParam("paramcountryTableTotalLine", countryTableTotal.replaceAll("__CURRENCY__", "").replaceAll("Total", "TOTAL"));

        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
                countryTableTotal = countryTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_NAT_CURRENCY + "]");
            } else {
                countryTableTotal = countryTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_USD_CURRENCY + "]");
            }
        } else {
            table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            countryTableTotal = countryTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_NAT_CURRENCY + "]");
        }

        if (sectorParam != null) {
            table.setParam("paramSECTOR", "[Name].[" + sectorParam + "]");
        }
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            table.setParam("paramCOFINANCED", "[1]");
        }
        if(CPAOnlyParam != null && CPAOnlyParam.equals("true")) {
            table.setParam("paramCPA", "[1]");
        }
        table.setParam("paramcountryTableRowSet", countryTableRowSet);
        table.setParam("paramcountryTableTotal", countryTableTotal);

        table.setdisableStripeClasses(Boolean.TRUE);

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

    private void addCountryChart () {
        Label title = new Label("countryChartTitle", "Net Disbursement by country - " + (tableYear - 1) + "-" + tableYear +
                " - " + countryCurrency + " - full amount");
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "countryChart", "customDashboardsCountryChart") {
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
                stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        } else {
            stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
        }

        if (sectorParam != null) {
            stackedBarChart.setParam("paramSECTOR", "[Name].[" + sectorParam + "]");
        }
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            stackedBarChart.setParam("paramCOFINANCED", "[1]");
        }
        if(CPAOnlyParam != null && CPAOnlyParam.equals("true")) {
            stackedBarChart.setParam("paramCPA", "[1]");
        }
        stackedBarChart.setParam("paramcountryChartRowSet", countryChartRowSet);

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

    private void addSectorTable () {
        Label title = new Label("sectorTableTitle", "Net Disbursement by sector - " + (tableYear - 1) + "-" + tableYear +
                " - " + countryCurrency + " - full amount");
        add(title);

        Table table = new Table(CdaService, "sectorTable", "sectorTableRows", "customDashboardsSectorTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRows(this.rows, this.result, this.rowId, ReportsConstants.isSector);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        table.setParam("paramsectorTableTotalLine", sectorTableTotal.replaceAll("__CURRENCY__", "").replaceAll("Total", "TOTAL"));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
                sectorTableTotal = sectorTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_NAT_CURRENCY + "]");
            } else {
                sectorTableTotal = sectorTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_USD_CURRENCY + "]");
            }
        } else {
            table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            sectorTableTotal = sectorTableTotal.replaceAll("__CURRENCY__", ", [Measures].[" + ReportsConstants.MDX_NAT_CURRENCY + "]");
        }
        if (geographyParam != null && recipientParam != null) {
            table.setParam("paramCOUNTRIES", "[" + geographyParam + "].[" + recipientParam + "]");
        } else {
            if (geographyParam != null) {
                table.setParam("paramCOUNTRIES", "[" + geographyParam + "]");
            } else {
                if (recipientParam != null) {
                    table.setParam("paramCOUNTRIES", "[Name].[" + recipientParam + "]");
                }
            }
        }
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            table.setParam("paramCOFINANCED", "[1]");
        }
        if(CPAOnlyParam != null && CPAOnlyParam.equals("true")) {
            table.setParam("paramCPA", "[1]");
        }
        table.setParam("paramsectorTableRowSet", sectorTableRowSet);
        table.setParam("paramsectorTableTotal", sectorTableTotal);

        table.setdisableStripeClasses(Boolean.TRUE);

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

    private void addSectorChart () {
        Label title = new Label("sectorChartTitle", "Net Disbursement by sector - " + (tableYear - 1) + "-" + tableYear +
                " - " + countryCurrency + " - full amount");
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "sectorChart", "customDashboardsSectorChart") {
            @Override
            public List<List<Float>> getResultSeriesAsList () {
                return ReportsDashboardsUtils.processChartRows(this.runQuery(), getOptions());
            }
        };

        // add MDX queries parameters
        stackedBarChart.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        stackedBarChart.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        } else {
            stackedBarChart.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
        }
        if (geographyParam != null && recipientParam != null) {
            stackedBarChart.setParam("paramCOUNTRIES", "[" + geographyParam + "].[" + recipientParam + "]");
        } else {
            if (geographyParam != null) {
                stackedBarChart.setParam("paramCOUNTRIES", "[" + geographyParam + "]");
            } else {
                if (recipientParam != null) {
                    stackedBarChart.setParam("paramCOUNTRIES", "[Name].[" + recipientParam + "]");
                }
            }
        }
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            stackedBarChart.setParam("paramCOFINANCED", "[1]");
        }
        if(CPAOnlyParam != null && CPAOnlyParam.equals("true")) {
            stackedBarChart.setParam("paramCPA", "[1]");
        }
        stackedBarChart.setParam("paramsectorChartRowSet", sectorChartRowSet);

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
