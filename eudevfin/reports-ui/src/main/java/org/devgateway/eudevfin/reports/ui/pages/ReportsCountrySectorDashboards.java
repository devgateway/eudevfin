package org.devgateway.eudevfin.reports.ui.pages;

import com.googlecode.wickedcharts.highcharts.options.DataLabels;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
import com.googlecode.wickedcharts.highcharts.options.Tooltip;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.StackedBarChart;
import org.devgateway.eudevfin.reports.ui.components.Table;
import org.devgateway.eudevfin.reports.ui.scripts.Dashboards;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;

import java.text.DecimalFormat;
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

    private final int MILLION = 1000000;

    private static final String isCountry = "Country";
    private static final String isGeography = "Geography";
    private static final String isSector = "Sector";
    private static final String isParentSector = "ParentSector";

    /*
     * variables used to dynamically create the MDX queries
     */
    // variables used for 'country table'
    private String countryTableRowSet = "{Hierarchize({[Country].[Geography].Members, [Country].[Name].Members, [Country].[Total]})}";
    private String countryTableGeography = "{Hierarchize({{[Country].[__GEOGRAPHY__]}, Filter({{[Country].[Name].Members}}, " +
            "(Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0))}), [Country].[Total]}";
    private String countryTableRecipient = "{Hierarchize({{[Country].[Name].[__RECIPIENT__]}}), [Country].[Total]}";
    private String countryTableGeographyRecipient = "{Hierarchize({{[Country].[__GEOGRAPHY__]}, [Country].[Name].[__RECIPIENT__], [Country].[Total]})}";
    private String countryTableTotal = "MEMBER [Country].[Total] AS SUM([Country].[Name].Members, [Measures].[Extended Amount Currency NATLOECD])";
    private String countryTableTotalGeography = "MEMBER [Country].[Total] AS SUM([Country].__GEOGRAPHY__, [Measures].[Extended Amount Currency NATLOECD])";

    // variables used for 'country chart'
    private String countryChartRowSet = "{[Country].[Name].Members}";
    private String countryChartGeography = "{Filter({{[Country].[Name].Members}}, (Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0))}";
    private String countryChartRecipient = "{[Country].[Name].[__RECIPIENT__]}";

    // variables used for 'sector table'
    private String sectorTableRowSet = "{Hierarchize({[SectorHierarchy].[ParentName].Members, [SectorHierarchy].[Name].Members, [SectorHierarchy].[Total]})}";
    private String sectorTableSector = "{Hierarchize({[SectorHierarchy].[Name].[__SECTOR__], [SectorHierarchy].[Total]})}";
    private String sectorTableTotal = "MEMBER[SectorHierarchy].[Total] AS SUM([SectorHierarchy].[Name].Members, [Measures].[Extended Amount Currency NATLOECD])";
    private String sectorTableTotalSector = "MEMBER[SectorHierarchy].[Total] AS SUM([SectorHierarchy].[Name].[__SECTOR__], [Measures].[Extended Amount Currency NATLOECD])";

    // variables used for 'sector chart'
    private String sectorChartRowSet = "{[SectorHierarchy].[Name].Members}";
    private String sectorChartSector = "{[SectorHierarchy].[Name].[__SECTOR__]}";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String geographyParam;
    private String recipientParam;
    private String sectorParam;
    private String yearParam;
    private String coFinancingParam;
    private String CPAOnlyParam;

    @SpringBean
    QueryService CdaService;

    public ReportsCountrySectorDashboards(final PageParameters parameters) {
        // get a default year if it's not specified
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // process the parameters received from the filters
        if(!parameters.get(ReportsConstants.GEOGRAPHY_PARAM).equals(StringValue.valueOf((String) null))) {
            geographyParam = parameters.get(ReportsConstants.GEOGRAPHY_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.RECIPIENT_PARAM).equals(StringValue.valueOf((String) null))) {
            recipientParam = parameters.get(ReportsConstants.RECIPIENT_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.SECTOR_PARAM).equals(StringValue.valueOf((String) null))) {
            sectorParam = parameters.get(ReportsConstants.SECTOR_PARAM).toString();
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
            sectorTableSector = sectorTableSector.replaceAll("__SECTOR__", sectorParam);
            sectorTableRowSet = sectorTableSector;

            // calculate 'sector table' Total value
            sectorTableTotalSector = sectorTableTotalSector.replaceAll("__SECTOR__", sectorParam);
            sectorTableTotal = sectorTableTotalSector;

            // calculate 'sector chart' row set
            sectorChartSector = sectorChartSector.replaceAll("__SECTOR__", sectorParam);
            sectorChartRowSet = sectorChartSector;
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
        Label title = new Label("countryTableTitle", new StringResourceModel("reportscountrysectordashboards.countryTable", this, null, null));
        add(title);

        Table table = new Table(CdaService, "countryTable", "countryTableRows", "customDashboardsCountryTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return processTableRows(this.rows, this.result, this.rowId, isCountry);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
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

        Label nationalCurrencyFirst = new Label("nationalCurrencyFirst", new StringResourceModel("reportscountrysectordashboards.nationalCurrency", this, null, null));
        table.getTable().add(nationalCurrencyFirst);
        Label nationalCurrencySecond = new Label("nationalCurrencySecond", new StringResourceModel("reportscountrysectordashboards.nationalCurrency", this, null, null));
        table.getTable().add(nationalCurrencySecond);
    }

    private void addCountryChart () {
        Label title = new Label("countryChartTitle", new StringResourceModel("reportscountrysectordashboards.countryChart", this, null, null));
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "countryChart", "customDashboardsCountryChart") {
            @Override
            public List<List<Float>> getResultSeriesAsList () {
                this.result = this.runQuery();

                return processChartRows(this.runQuery(), getOptions());
            }
        };

        // add MDX queries parameters
        stackedBarChart.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        stackedBarChart.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
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
        Label title = new Label("sectorTableTitle", new StringResourceModel("reportscountrysectordashboards.sectorTable", this, null, null));
        add(title);

        Table table = new Table(CdaService, "sectorTable", "sectorTableRows", "customDashboardsSectorTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return processTableRows(this.rows, this.result, this.rowId, isSector);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
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

        Label nationalCurrencyFirst = new Label("nationalCurrencyFirst", new StringResourceModel("reportscountrysectordashboards.nationalCurrency", this, null, null));
        table.getTable().add(nationalCurrencyFirst);
        Label nationalCurrencySecond = new Label("nationalCurrencySecond", new StringResourceModel("reportscountrysectordashboards.nationalCurrency", this, null, null));
        table.getTable().add(nationalCurrencySecond);
    }

    private void addSectorChart () {
        Label title = new Label("sectorChartTitle", new StringResourceModel("reportscountrysectordashboards.sectorChart", this, null, null));
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "sectorChart", "customDashboardsSectorChart") {
            @Override
            public List<List<Float>> getResultSeriesAsList () {
                return processChartRows(this.runQuery(), getOptions());
            }
        };

        // add MDX queries parameters
        stackedBarChart.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        stackedBarChart.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
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

    /*
     * since the country/sector tables are similar we use only one function to process the rows
     */
    private ListView<String[]> processTableRows (List<String[]> rows, QueryResult result, String rowId, String typeOfTable) {
        List <List<String>> resultSet = result.getResultset();

        if(resultSet.size() != 0 && resultSet.get(0).size() > 3) {
            // check if we have data for the 'first year' or 'second year'
            // and add null values
            if (resultSet.get(0).size() == 5) {
                for (int i = 0; i < resultSet.size(); i++) {
                    if (result.getMetadata().get(1).getColName().equals("First Year National")) {
                        resultSet.get(i).add(4, null);
                        resultSet.get(i).add(5, null);
                        resultSet.get(i).add(6, null);
                    } else {
                        resultSet.get(i).add(1, null);
                        resultSet.get(i).add(2, null);
                        resultSet.get(i).add(3, null);
                    }
                }
            }

            // this is usually happening when we filter by both region and geography
            if (resultSet.get(0).size() == 6) {
                for (int i = 0; i < resultSet.size(); i++) {
                    if (result.getMetadata().get(3).getColName().equals("First Year %")) {
                        resultSet.get(i).add(5, null);
                    } else {
                        resultSet.get(i).add(3, null);
                    }
                }
            }

            // format the amounts as #,###.##
            // and other values like percentages
            DecimalFormat df = new DecimalFormat("#,###.##");
            for (int i = 0; i < resultSet.size(); i++) {
                if (resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) {
                    String item = df.format(Float.parseFloat(resultSet.get(i).get(1))); // amounts - national currency (first year)
                    resultSet.get(i).set(1, item);
                }

                if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                    String item = df.format(Float.parseFloat(resultSet.get(i).get(2))); // amounts (first year)
                    resultSet.get(i).set(2, item);
                }

                if (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null) {
                    String item = df.format(Float.parseFloat(resultSet.get(i).get(3)) * 100) + '%'; // percentages (first year)
                    resultSet.get(i).set(3, item);
                }

                if (resultSet.get(i).size() > 4 && resultSet.get(i).get(4) != null) {
                    String item = df.format(Float.parseFloat(resultSet.get(i).get(4))); // amounts - national currency (second year)
                    resultSet.get(i).set(4, item);
                }

                if (resultSet.get(i).size() > 5 && resultSet.get(i).get(5) != null) {
                    String item = df.format(Float.parseFloat(resultSet.get(i).get(5))); // amounts (second year)
                    resultSet.get(i).set(5, item);
                }

                if (resultSet.get(i).size() > 6 && resultSet.get(i).get(6) != null) {
                    String item = df.format(Float.parseFloat(resultSet.get(i).get(6)) * 100) + '%'; // percentages (second year)
                    resultSet.get(i).set(6, item);
                }
            }

            /*
             * country/sector table are processes differently
             */
            if(typeOfTable.equals(isCountry)) {
                // find which row is a country or a geography
                for (int i = 0; i < resultSet.size(); i++) {
                    String region = resultSet.get(i).get(0);
                    if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                            equals(isCountry.toLowerCase())) {
                        resultSet.get(i).add(1, region);
                        resultSet.get(i).set(0, null);
                    } else {
                        if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                                equals(isGeography.toLowerCase())) {
                            resultSet.get(i).add(1, null);
                        }
                    }
                }

                // set 'Total' as Geography
                resultSet.get(resultSet.size() - 1).set(8, isGeography.toLowerCase());
            }
            if(typeOfTable.equals(isSector)) {
                // find which row is a sector or a parent-sector
                for (int i = 0; i < resultSet.size(); i++) {
                    String sector = resultSet.get(i).get(0);
                    if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                            equals(isSector.toLowerCase())) {
                        resultSet.get(i).add(1, sector);
                        resultSet.get(i).set(0, null);
                    } else {
                        if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                                equals(isParentSector.toLowerCase())) {
                            resultSet.get(i).add(1, null);
                        }
                    }
                }

                // set 'Total' as Geography
                resultSet.get(resultSet.size() - 1).set(8, isParentSector.toLowerCase());
            }

            for (List<String> item : resultSet) {
                rows.add(item.toArray(new String[item.size()]));
            }
        }

        ListView<String[]> tableRows = new ListView<String[]>(rowId, rows) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                // use different color for geography items
                if (row[row.length - 1].toLowerCase().equals(isGeography.toLowerCase()) ||
                        row[row.length - 1].toLowerCase().equals(isParentSector.toLowerCase())) {
                    item.add(new AttributeModifier("class", "geography"));
                }

                item.add(new Label("col0", row[0]));
                item.add(new Label("col1", row[1]));
                item.add(new Label("col2", row[2]));
                item.add(new Label("col3", row[3]));
                item.add(new Label("col4", row[4]));
                item.add(new Label("col5", row[5]));
                item.add(new Label("col6", row[6]));
                item.add(new Label("col7", row[7]));
            }
        };

        return tableRows;
    }

    /*
     * since the country/sector charts are similar we use only one function to process the rows
     */
    private List<List<Float>> processChartRows (QueryResult result, Options options) {
        List<List<Float>> resultSeries = new ArrayList<>();
        List<String> resultCategories = new ArrayList<>();

        List<Float> firstYearList = new ArrayList<>();
        resultSeries.add(firstYearList);
        List<Float> secondYearList = new ArrayList<>();
        resultSeries.add(secondYearList);

        for (List<String> item : result.getResultset()) {
            resultCategories.add(item.get(0));

            if (item.size() > 1 && item.get(1) != null) {
                resultSeries.get(0).add(Float.parseFloat(item.get(1)) / MILLION);
            } else {
                resultSeries.get(0).add((float) 0);
            }

            if (item.size() > 2 && item.get(2) != null) {
                resultSeries.get(1).add(Float.parseFloat(item.get(2)) / MILLION);
            } else {
                resultSeries.get(1).add((float) 0);
            }
        }

        options.getxAxis().get(0).setCategories(new ArrayList<>(resultCategories));

        return resultSeries;
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
