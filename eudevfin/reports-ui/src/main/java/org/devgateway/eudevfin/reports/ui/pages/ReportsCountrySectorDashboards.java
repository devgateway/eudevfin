package org.devgateway.eudevfin.reports.ui.pages;

import com.googlecode.wickedcharts.highcharts.options.DataLabels;
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

    private int tableYear;

    private final int MILLION = 1000000;

    private static final String isCountry = "Country";
    private static final String isGeography = "Geography";
    private static final String isSector = "Sector";
    private static final String isParentSector = "ParentSector";

    private String countryTableRowSet = "{Hierarchize({[Country].[Geography].Members, [Country].[Name].Members, [Country].[Total]})}";
    private String countryGeography = "{Hierarchize({{[Country].[__GEOGRAPHY__]}, Filter({{[Country].[Name].Members}}, " +
            "(Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0))}), [Country].[Total]}";
    private String countryRecipient = "{Hierarchize({{[Country].[Name].[__RECIPIENT__]}}), [Country].[Total]}";
    private String countryGeographyRecipient = "{Hierarchize({{[Country].[__GEOGRAPHY__]}, [Country].[Name].[__RECIPIENT__], [Country].[Total]})}";
    private String countryTableTotal = "MEMBER [Country].[Total] AS SUM([Country].[Name].Members, [Measures].[Extended Amount Currency NATLOECD])";
    private String countryTableTotalGeography = "MEMBER [Country].[Total] AS SUM([Country].__GEOGRAPHY__, [Measures].[Extended Amount Currency NATLOECD])";

    private String geographyParam;
    private String recipientParam;
    private String sectorParam;
    private String yearParam;
    private String coFinancingParam;
    private String CPAOnlyParam;

    @SpringBean
    QueryService CdaService;

    public ReportsCountrySectorDashboards(final PageParameters parameters) {
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

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
            logger.error(">>> yearParam: " + parameters.get(ReportsConstants.YEAR_PARAM));
            if (parameters.get(ReportsConstants.YEAR_PARAM).equals(StringValue.valueOf((String) null)))
                logger.error("dlakjfldka is null!!!!!!");
            yearParam = parameters.get(ReportsConstants.YEAR_PARAM).toString();
            logger.error(">>>> yearParam: " + yearParam);
            tableYear = Integer.parseInt(yearParam);
        }
        if(!parameters.get(ReportsConstants.COFINANCING_PARAM).equals(StringValue.valueOf((String) null))) {
            coFinancingParam = parameters.get(ReportsConstants.COFINANCING_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.CPAONLY_PARAM).equals(StringValue.valueOf((String) null))) {
            CPAOnlyParam = parameters.get(ReportsConstants.CPAONLY_PARAM).toString();
        }

        logger.info("geographyParam: " + geographyParam);
        logger.info("recipientParam: " + recipientParam);
        logger.info("sectorParam: " + sectorParam);
        logger.info("yearParam: " + yearParam);
        logger.info("coFinancingParam: " + coFinancingParam);
        logger.info("CPAOnlyParam: " + CPAOnlyParam);

        if (geographyParam != null && recipientParam != null) {
            countryGeographyRecipient = countryGeographyRecipient.replaceAll("__GEOGRAPHY__", geographyParam);
            countryGeographyRecipient = countryGeographyRecipient.replaceAll("__RECIPIENT__", recipientParam);
            countryTableRowSet = countryGeographyRecipient;

            // use the region to calculate the total amount
            countryTableTotalGeography = countryTableTotalGeography.replaceAll("__GEOGRAPHY__", "[" + geographyParam + "]");
            countryTableTotal = countryTableTotalGeography;
        } else {
            if (geographyParam != null) {
                countryGeography = countryGeography.replaceAll("__GEOGRAPHY__", geographyParam);
                countryTableRowSet = countryGeography;

                countryTableTotalGeography = countryTableTotalGeography.replaceAll("__GEOGRAPHY__", "[" + geographyParam + "]");
                countryTableTotal = countryTableTotalGeography;
            } else {
                if (recipientParam != null) {
                    countryRecipient = countryRecipient.replaceAll("__RECIPIENT__", recipientParam);
                    countryTableRowSet = countryRecipient;

                    countryTableTotalGeography = countryTableTotalGeography.replaceAll("__GEOGRAPHY__", "[Name].[" + recipientParam + "]");
                    countryTableTotal = countryTableTotalGeography;
                }
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
        Label title = new Label("countryTableTitle", new StringResourceModel("reportscountrysectordashboards.countryTable", this, null, null));
        add(title);

        Table table = new Table(CdaService, "countryTable", "countryTableRows", "customDashboardsCountryTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();
                List <List<String>> resultSet = this.result.getResultset();
                logger.error(resultSet);
                if(resultSet.size() != 0 && resultSet.get(0).size() > 3) {
                    // check if we have data for the 'first year' or 'second year'
                    // and add null values
                    if (resultSet.get(0).size() == 4) {
                        for (int i = 0; i < resultSet.size(); i++) {
                            if (this.result.getMetadata().get(1).getColName().equals("First Year")) {
                                resultSet.get(i).add(3, null);
                                resultSet.get(i).add(4, null);
                            } else {
                                resultSet.get(i).add(1, null);
                                resultSet.get(i).add(2, null);
                            }
                        }
                    }

                    // this is usually happening when we filter by both region and geography
                    if (resultSet.get(0).size() == 5) {
                        for (int i = 0; i < resultSet.size(); i++) {
                            if (this.result.getMetadata().get(2).getColName().equals("First Year %")) {
                                resultSet.get(i).add(4, null);
                            } else {
                                resultSet.get(i).add(2, null);
                            }
                        }
                    }

                    logger.error(">>>>>>>> " + this.result.getMetadata().get(1).getColName());
                    logger.error(resultSet);

                    // format the amounts as #,###.##
                    // and other values like percentages
                    DecimalFormat df = new DecimalFormat("#,###.##");
                    for (int i = 0; i < resultSet.size(); i++) {
                        if (resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) {
                            String item = df.format(Float.parseFloat(resultSet.get(i).get(1)) /
                                    ReportsCountrySectorDashboards.this.MILLION); // amounts (first year)
                            resultSet.get(i).set(1, item);
                        }

                        if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                            String item = df.format(Float.parseFloat(resultSet.get(i).get(2)) * 100) + '%'; // percentages (first year)
                            resultSet.get(i).set(2, item);
                        }

                        if (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null) {
                            String item = df.format(Float.parseFloat(resultSet.get(i).get(3)) /
                                    ReportsCountrySectorDashboards.this.MILLION); // amounts (second year)
                            resultSet.get(i).set(3, item);
                        }

                        if (resultSet.get(i).size() > 4 && resultSet.get(i).get(4) != null) {
                            String item = df.format(Float.parseFloat(resultSet.get(i).get(4)) * 100) + '%'; // percentages (second year)
                            resultSet.get(i).set(4, item);
                        }
                    }

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
                    resultSet.get(resultSet.size() - 1).set(6, isGeography.toLowerCase());

                    for (List<String> item : resultSet) {
                        this.rows.add(item.toArray(new String[item.size()]));
                    }
                }

                ListView<String[]> tableRows = new ListView<String[]>(this.rowId, rows) {
                    @Override
                    protected void populateItem(ListItem<String[]> item) {
                        String[] row = item.getModelObject();

                        // use different color for geography items
                        if (row[6].toLowerCase().equals(isGeography.toLowerCase())) {
                            item.add(new AttributeModifier("class", "geography"));
                        }

                        item.add(new Label("col0", row[0]));
                        item.add(new Label("col1", row[1]));
                        item.add(new Label("col2", row[2]));
                        item.add(new Label("col3", row[3]));
                        item.add(new Label("col4", row[4]));
                        item.add(new Label("col5", row[5]));
                    }
                };

                return tableRows;
            }
        };

        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        if (sectorParam != null) {
            logger.error("[Name].[" + sectorParam + "]");
            table.setParam("paramSECTOR", "[Name].[" + sectorParam + "]");
        }

        table.setParam("paramcountryTableRowSet", countryTableRowSet);
        table.setParam("paramcountryTableTotal", countryTableTotal);

        logger.error("countryTableTotal: " + countryTableTotal);
        logger.error("countryTableRowSet: " + countryTableRowSet);

        table.setdisableStripeClasses(Boolean.TRUE);

        Label firstYear = new Label("firstYear", tableYear - 1);
        table.getTable().add(firstYear);
        Label secondYear = new Label("secondYear", tableYear);
        table.getTable().add(secondYear);

        add(table.getTable());
        table.addTableRows();
    }

    private void addCountryChart () {
        Label title = new Label("countryChartTitle", new StringResourceModel("reportscountrysectordashboards.countryChart", this, null, null));
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "countryChart", "customDashboardsCountryChart") {
            @Override
            public List<List<Float>> getResultSeriesAsList () {
                this.result = this.runQuery();
                List<List<Float>> resultSeries = new ArrayList<>();
                List<String> resultCategories = new ArrayList<>();

                List<Float> firstYearList = new ArrayList<>();
                resultSeries.add(firstYearList);
                List<Float> secondYearList = new ArrayList<>();
                resultSeries.add(secondYearList);

                for (List<String> item : result.getResultset()) {
                    resultCategories.add(item.get(0));

                    if (item.size() > 1 && item.get(1) != null) {
                        resultSeries.get(0).add(Float.parseFloat(item.get(1)) / 1000000);
                    } else {
                        resultSeries.get(0).add((float) 0);
                    }

                    if (item.size() > 2 && item.get(2) != null) {
                        resultSeries.get(1).add(Float.parseFloat(item.get(2)) / 1000000);
                    } else {
                        resultSeries.get(1).add((float) 0);
                    }
                }

                getOptions().getxAxis().get(0).setCategories(new ArrayList<>(resultCategories));

                return resultSeries;
            }
        };

        stackedBarChart.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        stackedBarChart.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        List<List<Float>> resultSeries = stackedBarChart.getResultSeriesAsList();
        stackedBarChart.getOptions().setPlotOptions(new PlotOptionsChoice().
                setBar(new PlotOptions().
                        setDataLabels(new DataLabels().
                                setEnabled(Boolean.TRUE))));
        stackedBarChart.getOptions().setTooltip(new Tooltip().setValueSuffix(" millions").setPercentageDecimals(2));
        stackedBarChart.getOptions().getChartOptions().setHeight(400);

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
                List <List<String>> resultSet = this.result.getResultset();

                if(resultSet.size() != 0) {
                    // check if we have data for the 'first year' or 'second year'
                    // and add null values
                    if (resultSet.get(0).size() == 4) {
                        for (int i = 0; i < resultSet.size(); i++) {
                            if (this.result.getMetadata().get(1).getColName().equals("First Year")) {
                                resultSet.get(i).add(3, null);
                                resultSet.get(i).add(4, null);
                            } else {
                                resultSet.get(i).add(1, null);
                                resultSet.get(i).add(2, null);
                            }
                        }
                    }

                    // format the amounts as #,###.##
                    // and other values like percentages
                    DecimalFormat df = new DecimalFormat("#,###.##");
                    for (int i = 0; i < resultSet.size(); i++) {
                        if (resultSet.get(i).size() > 1 && resultSet.get(i).get(1) != null) {
                            String item = df.format(Float.parseFloat(resultSet.get(i).get(1)) /
                                    ReportsCountrySectorDashboards.this.MILLION); // amounts (first year)
                            resultSet.get(i).set(1, item);
                        }

                        if (resultSet.get(i).size() > 2 && resultSet.get(i).get(2) != null) {
                            String item = df.format(Float.parseFloat(resultSet.get(i).get(2)) * 100) + '%'; // percentages (first year)
                            resultSet.get(i).set(2, item);
                        }

                        if (resultSet.get(i).size() > 3 && resultSet.get(i).get(3) != null) {
                            String item = df.format(Float.parseFloat(resultSet.get(i).get(3)) /
                                    ReportsCountrySectorDashboards.this.MILLION); // amounts (second year)
                            resultSet.get(i).set(3, item);
                        }

                        if (resultSet.get(i).size() > 4 && resultSet.get(i).get(4) != null) {
                            String item = df.format(Float.parseFloat(resultSet.get(i).get(4)) * 100) + '%'; // percentages (second year)
                            resultSet.get(i).set(4, item);
                        }
                    }

                    // find which row is a country or a geography
                    for (int i = 0; i < resultSet.size(); i++) {
                        String region = resultSet.get(i).get(0);
                        if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                                equals(isSector.toLowerCase())) {
                            resultSet.get(i).add(1, region);
                            resultSet.get(i).set(0, null);
                        } else {
                            if (resultSet.get(i).get(resultSet.get(i).size() - 1).toLowerCase().
                                    equals(isParentSector.toLowerCase())) {
                                resultSet.get(i).add(1, null);
                            }
                        }
                    }

                    // add the percentage for the 'Total'
                    if (resultSet.get(resultSet.size() - 1).get(2) != null) {
                        resultSet.get(resultSet.size() - 1).set(3, "100%");
                    }
                    if (resultSet.get(resultSet.size() - 1).get(4) != null) {
                        resultSet.get(resultSet.size() - 1).set(5, "100%");
                    }
                    resultSet.get(resultSet.size() - 1).set(6, isParentSector.toLowerCase());

                    for (List<String> item : resultSet) {
                        this.rows.add(item.toArray(new String[item.size()]));
                    }
                }

                ListView<String[]> tableRows = new ListView<String[]>(this.rowId, rows) {
                    @Override
                    protected void populateItem(ListItem<String[]> item) {
                        String[] row = item.getModelObject();

                        // use different color for geography items
                        if (row[6].toLowerCase().equals(isParentSector.toLowerCase())) {
                            item.add(new AttributeModifier("class", "geography"));
                        }

                        item.add(new Label("col0", row[0]));
                        item.add(new Label("col1", row[1]));
                        item.add(new Label("col2", row[2]));
                        item.add(new Label("col3", row[3]));
                        item.add(new Label("col4", row[4]));
                        item.add(new Label("col5", row[5]));
                    }
                };

                return tableRows;
            }
        };

        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        table.setdisableStripeClasses(Boolean.TRUE);

        Label firstYear = new Label("firstYear", tableYear - 1);
        table.getTable().add(firstYear);
        Label secondYear = new Label("secondYear", tableYear);
        table.getTable().add(secondYear);

        add(table.getTable());
        table.addTableRows();
    }

    private void addSectorChart () {
        Label title = new Label("sectorChartTitle", new StringResourceModel("reportscountrysectordashboards.sectorChart", this, null, null));
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "sectorChart", "customDashboardsSectorChart") {
            @Override
            public List<List<Float>> getResultSeriesAsList () {
                this.result = this.runQuery();
                List<List<Float>> resultSeries = new ArrayList<>();
                List<String> resultCategories = new ArrayList<>();

                List<Float> firstYearList = new ArrayList<>();
                resultSeries.add(firstYearList);
                List<Float> secondYearList = new ArrayList<>();
                resultSeries.add(secondYearList);

                for (List<String> item : result.getResultset()) {
                    resultCategories.add(item.get(0));

                    if (item.size() > 1 && item.get(1) != null) {
                        resultSeries.get(0).add(Float.parseFloat(item.get(1)) / 1000000);
                    } else {
                        resultSeries.get(0).add((float) 0);
                    }

                    if (item.size() > 2 && item.get(2) != null) {
                        resultSeries.get(1).add(Float.parseFloat(item.get(2)) / 1000000);
                    } else {
                        resultSeries.get(1).add((float) 0);
                    }
                }

                getOptions().getxAxis().get(0).setCategories(new ArrayList<>(resultCategories));

                return resultSeries;
            }
        };

        stackedBarChart.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        stackedBarChart.setParam("paramSECOND_YEAR", Integer.toString(tableYear));

        List<List<Float>> resultSeries = stackedBarChart.getResultSeriesAsList();
        stackedBarChart.getOptions().setPlotOptions(new PlotOptionsChoice().
                setBar(new PlotOptions().
                        setDataLabels(new DataLabels().
                                setEnabled(Boolean.TRUE))));
        stackedBarChart.getOptions().setTooltip(new Tooltip().setValueSuffix(" millions").setPercentageDecimals(2));
        stackedBarChart.getOptions().getChartOptions().setHeight(400);

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
