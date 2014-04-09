package org.devgateway.eudevfin.reports.ui.pages;

import com.googlecode.wickedcharts.highcharts.options.Labels;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.functions.DefaultFormatter;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.financial.util.FinancialTransactionUtil;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.reports.core.domain.Metadatum;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.components.PieChart;
import org.devgateway.eudevfin.reports.ui.components.StackedBarChart;
import org.devgateway.eudevfin.reports.ui.components.Table;
import org.devgateway.eudevfin.reports.ui.scripts.Dashboards;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.joda.money.CurrencyUnit;
import org.wicketstuff.annotation.mount.MountPath;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author idobre
 * @since 3/11/14
 */

@MountPath(value = "/reports")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsPage extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(ReportsPage.class);

    private final int MILLION = 1000000;
    private int tableYear;
    private String countryCurrency;

    @SpringBean
    QueryService CdaService;

    public ReportsPage() {
        tableYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

        addComponents();
    }

    private void addComponents () {
        addReportingCountry();
        getCurrency();
        addNetODATable();
        addTopTenRecipientsTable();
        addTopTenMemoShareTAble();
        addOdaByRegionChart();
        addOdaByIncomeGroupChart();
        addOdaBySectorChart();
    }

    private void addReportingCountry () {
        String donorName = "";
        Organization organizationForCurrentUser = AuthUtils.getOrganizationForCurrentUser();

        if (organizationForCurrentUser != null) {
            donorName = organizationForCurrentUser.getDonorName();
        }

        Label reportingCountry = new Label("reportingCountry", donorName);
        add(reportingCountry);
    }

    private void getCurrency () {
        countryCurrency = "";
        CurrencyUnit currencyForCountryIso = FinancialTransactionUtil
                .getCurrencyForCountryIso(AuthUtils
                        .getIsoCountryForCurrentUser());
        if (currencyForCountryIso != null) {
            countryCurrency = currencyForCountryIso.getCode();
        }
    }

    private void addNetODATable () {
        Label title = new Label("netODADashboardTitle", new StringResourceModel("dashboards.netODA", this, null, null));
        add(title);

        Table table = new Table(CdaService, "netODADashboard", "netODARows", "netODATable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                final int NUMBER_OF_YEARS = 2;
                final String FIRST_YEAR = "" + (tableYear - 1);

                this.rows = new ArrayList<>();
                this.result = this.runQuery();
                List <List<String>> resultSet = this.result.getResultset();
                List <Metadatum>metadata = result.getMetadata();

                if(resultSet.get(0).size() != 1) {
                    // check programmatically that all columns are populated.
                    // there is no other elegant solution
                    // (we don't need to update the 'colIndex' property from metadata since we only use the results)
                    int len = resultSet.size();

                    if (metadata.size() > 1 && !metadata.get(1).getColName().equals(FIRST_YEAR)) {
                        // add new Metadatum item for missing year
                        Metadatum newItem = new Metadatum();
                        newItem.setColIndex(1);
                        newItem.setColType("Numeric");
                        newItem.setColName(FIRST_YEAR);

                        metadata.add(1, newItem);

                        // add the new dimension values for missing year
                        for (int i = 0; i < len; i++) {
                            resultSet.get(i).add(1, null);
                        }
                    }

                    if (metadata.size() > 2 && !metadata.get(2).getColName().equals("" + (Integer.parseInt(FIRST_YEAR) + 1))) {
                        // add new Metadatum item for missing year
                        Metadatum newItem = new Metadatum();
                        newItem.setColIndex(2);
                        newItem.setColType("Numeric");
                        newItem.setColName("" + (Integer.parseInt(FIRST_YEAR) + 1));

                        metadata.add(2, newItem);

                        // add the new dimension values for missing year
                        for (int i = 0; i < len; i++) {
                            resultSet.get(i).add(2, null);
                        }
                    }

                    // add '2013 / 2012' type of column
                    for (int i = 0; i < len - 2; i++) {
                        // add (last_year - 1 / last_year) column
                        // but first check if we have valid values
                        if (resultSet.get(i).get(NUMBER_OF_YEARS - 1) == null) {
                            resultSet.get(i).add(null);
                        } else {
                            if (resultSet.get(i).get(NUMBER_OF_YEARS) == null) {
                                resultSet.get(i).add(null);
                            } else {
                                Double result = Double.parseDouble(resultSet.get(i).get(NUMBER_OF_YEARS - 1)) /
                                        Double.parseDouble(resultSet.get(i).get(NUMBER_OF_YEARS)) * 100;
                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                resultSet.get(i).add(Float.valueOf(twoDForm.format(result)) + "%");
                            }
                        }
                    }

                    // add null values for '2013 / 2012' column on 'ODA/GNI' and 'Bilateral share' rows
                    for (int i = len - 2; i < len; i++) {
                        resultSet.get(i).add(null);
                    }

                    // display the results in millions
                    for (int i = 0; i < len - 2; i++) {
                        for (int j = 1; j <= NUMBER_OF_YEARS; j++) {
                            String item = resultSet.get(i).get(j);
                            if (item != null) {
                                resultSet.get(i).set(j, "" + Float.parseFloat(item) / ReportsPage.this.MILLION);
                            }
                        }
                    }

                    // format the amounts as #,###.##
                    // and other values like 'Bilateral share'
                    DecimalFormat df = new DecimalFormat("#,###.##");
                    for (int i = 0; i < len; i++) {
                        for (int j = 1; j <= NUMBER_OF_YEARS; j++) {
                            String item = resultSet.get(i).get(j);

                            if (item != null) {
                                item = df.format(Float.parseFloat(resultSet.get(i).get(j)));

                                // len - 2 row is the 'ODA/GNI' row and we need to add the percentages
                                if (i == len - 2) {
                                    item += "%";
                                }
                                resultSet.get(i).set(j, item);
                            }
                        }
                    }

                    for (List<String> item : resultSet) {
                        this.rows.add(item.toArray(new String[item.size()]));
                    }
                }

                ListView<String[]> tableRows = new ListView<String[]>(this.rowId, rows) {
                    @Override
                    protected void populateItem(ListItem<String[]> item) {
                        String[] row = item.getModelObject();

                        item.add(new Label("col0", row[0]));
                        item.add(new Label("col1", row[1]));
                        item.add(new Label("col2", row[2]));
                        item.add(new Label("col3", row[3]));
                    }
                };

                return tableRows;
            }
        };

        table.setParam("YEAR", Integer.toString(tableYear));
        table.setParam("YEAR1", Integer.toString(tableYear - 1));
        //table.setParam("REPORT_CURRENCY_CODE", countryCurrency + " (million)");

        Label tableYear1 = new Label("tableYear1", tableYear - 1);
        table.getTable().add(tableYear1);
        Label tableYear2 = new Label("tableYear2", tableYear);
        table.getTable().add(tableYear2);
        Label tableYear3 = new Label("tableYear3", tableYear + " / " + (tableYear - 1));
        table.getTable().add(tableYear3);

        add(table.getTable());
        table.addTableRows();
    }

    private void addTopTenRecipientsTable () {
        Label title = new Label("topTenRecipientsTitle", new StringResourceModel("dashboards.topTenRecipients", this, null, null));
        add(title);

        Table table = new Table(CdaService, "topTenRecipients", "topTenRows", "topTenRecipients") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();
                List <List<String>> resultSet = this.result.getResultset();

                // format the amounts as #,###.###
                DecimalFormat df = new DecimalFormat("#,###.###");
                for(int i = 0; i < resultSet.size(); i++) {
                    String item = resultSet.get(i).get(1);

                    if (item != null) {
                        item = df.format(Float.parseFloat(resultSet.get(i).get(1)) / ReportsPage.this.MILLION);
                        resultSet.get(i).set(1, item);
                    }
                }

                for (List<String> item : resultSet) {
                    this.rows.add(item.toArray(new String[item.size()]));
                }

                ListView<String[]> tableRows = new ListView<String[]>(this.rowId, rows) {
                    @Override
                    protected void populateItem(ListItem<String[]> item) {
                        String[] row = item.getModelObject();

                        item.add(new Label("col0", row[0]));
                        item.add(new Label("col1", row[1]));
                    }
                };

                return tableRows;
            }
        };

        table.setbSort(true);

        add(table.getTable());
        table.addTableRows();
    }

    private void addTopTenMemoShareTAble () {
        Label title = new Label("topTenMemoShareTitle", new StringResourceModel("dashboards.topTenMemoShare", this, null, null));
        add(title);

        Table table = new Table(CdaService, "topTenMemoShare", "topTenMemoRows", "topTenMemoShare") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                for (List<String> item : this.result.getResultset()) {
                    if (item.size() > 0) {
                        String[] newItem = new String[2];
                        newItem[0] = "Top 5 recipients";
                        newItem[1] = twoDecimalFormat(item.get(0)) + "%";

                        rows.add(newItem);
                    }

                    if (item.size() > 1) {
                        String[] newItem = new String[2];
                        newItem[0] = "Top 10 recipients";
                        newItem[1] = twoDecimalFormat(item.get(1)) + "%";

                        rows.add(newItem);
                    }

                    if (item.size() > 2) {
                        String[] newItem = new String[2];
                        newItem[0] = "Top 20 recipients";
                        newItem[1] = twoDecimalFormat(item.get(2)) + "%";

                        rows.add(newItem);
                    }
                }

                ListView<String[]> tableRows = new ListView<String[]>(this.rowId, rows) {
                    @Override
                    protected void populateItem(ListItem<String[]> item) {
                        String[] row = item.getModelObject();

                        item.add(new Label("col0", row[0]));
                        item.add(new Label("col1", row[1]));
                    }
                };

                return tableRows;
            }
        };

        add(table.getTable());
        table.addTableRows();
    }

    private void addOdaByIncomeGroupChart () {
        Label title = new Label("odaByIncomeGroupTitle", new StringResourceModel("dashboards.odaByIncomeGroupChart", this, null, null));
        add(title);

        PieChart pieChart = new PieChart(CdaService, "odaByIncomeGroupChart", "odaByIncomeGroupChart") {
            @Override
            public List<Point> getResultSeries () {
                this.result = this.runQuery();
                List<Point> resultSeries = new ArrayList<>();

                for (List<String> item : result.getResultset()) {
                    resultSeries.add(new Point(item.get(0), Float.parseFloat(item.get(1)) / ReportsPage.this.MILLION));
                }

                return resultSeries;
            }
        };

        Options options = pieChart.getOptions();
        options.addSeries(new PointSeries()
                .setType(SeriesType.PIE)
                .setData(pieChart.getResultSeries()));
        add(pieChart.getChart());
    }

    private void addOdaByRegionChart () {
        Label title = new Label("odaByRegionTitle", new StringResourceModel("dashboards.odaByRegionChart", this, null, null));
        add(title);

        PieChart pieChart = new PieChart(CdaService, "odaByRegionChart", "odaByRegionChart") {
            @Override
            public List<Point> getResultSeries () {
                this.result = this.runQuery();
                List<Point> resultSeries = new ArrayList<>();

                for (List<String> item : result.getResultset()) {
                    resultSeries.add(new Point(item.get(0), Float.parseFloat(item.get(1)) / ReportsPage.this.MILLION));
                }

                return resultSeries;
            }
        };

        Options options = pieChart.getOptions();
        // check if we have a result and make the chart slightly higher
        if (pieChart.getResultSeries().size() != 0) {
            options.getChartOptions().setHeight(350);
        }
        // options.getPlotOptions().getPie().getDataLabels().setEnabled(Boolean.FALSE); display data labels for now
        options.addSeries(new PointSeries()
                .setType(SeriesType.PIE)
                .setData(pieChart.getResultSeries()));
        add(pieChart.getChart());
    }

    private void addOdaBySectorChart () {
        Label title = new Label("odaBySectorTitle", new StringResourceModel("dashboards.odaBySectorChart", this, null, null));
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "odaBySectorChart", "odaBySectorChart") {
            @Override
            public Map<String, Float> getResultSeries () {
                float odaBySectorTotal = 0;

                this.result = this.runQuery();
                // use LinkedHashMap so we can keep the insert order
                Map<String, Float> resultSeries = new LinkedHashMap<>();
                Set<String> resultCategories = new HashSet<>();

                for (int i = result.getResultset().size() - 1; i >= 0; i--) {
                    List<String> item = result.getResultset().get(i);

                    // keep unique values
                    resultCategories.add(item.get(1));

                    resultSeries.put(item.get(0), Float.parseFloat(item.get(2)) / ReportsPage.this.MILLION);

                    odaBySectorTotal += (Float.parseFloat(item.get(2)) / ReportsPage.this.MILLION);
                }

                DecimalFormat twoDForm = new DecimalFormat("#.##");
                odaBySectorTotal = Float.valueOf(twoDForm.format(odaBySectorTotal));

                getOptions().getxAxis().get(0).setCategories(new ArrayList<>(resultCategories));
                getOptions().getyAxis().get(0).setMax(odaBySectorTotal)
                        .setTickInterval(odaBySectorTotal / 10)
                        .setLabels(new Labels()
                                .setFormatter(new DefaultFormatter().setFunction("return sprintf('%d', (this.value / " + odaBySectorTotal + ") * 100).replace(/,/g, \" \") + '%';")));

                return resultSeries;
            }
        };

        // remove the y-axis label ('ODA')
        stackedBarChart.getOptions().getxAxis().get(0).getLabels().setEnabled(Boolean.FALSE);

        for (Map.Entry<String, Float> entry : stackedBarChart.getResultSeries().entrySet()) {
            stackedBarChart.getOptions().addSeries(new SimpleSeries()
                    .setName(entry.getKey())
                    .setData(Arrays.asList(new Number[]{entry.getValue()})));
        }

        add(stackedBarChart.getChart());
    }

    private String twoDecimalFormat (String number) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");

        return twoDForm.format(Float.parseFloat(number));
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
