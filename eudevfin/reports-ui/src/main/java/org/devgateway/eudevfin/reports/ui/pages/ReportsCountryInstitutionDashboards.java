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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author idobre
 * @since 4/16/14
 */
@MountPath(value = "/reportscountryinstitutiondashboards")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsCountryInstitutionDashboards extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(ReportsCountryInstitutionDashboards.class);

    /*
     * variables used to dynamically create the MDX queries
     */
    // variables used for 'institution table'
    private String institutionTableRowSet = "CrossJoin([Extending Agency].[Name].Members, [Country].[Name].Members)";
    private String institutionTableInstitution = "CrossJoin([Extending Agency].[Name].[__INSTITUTION__], [Country].[Name].Members)";
    private String institutionTableRecipient = "CrossJoin([Extending Agency].[Name].Members, [Country].[Name].[__RECIPIENT__])";
    private String institutionTableInstitutionRecipient = "CrossJoin([Extending Agency].[Name].[__INSTITUTION__], [Country].[Name].__RECIPIENT__)";
    private String institutionTableGeography = "CrossJoin([Extending Agency].[Name].Members, Filter({{[Country].[Name].Members}}, " +
            "(Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0)))";
    private String institutionTableInstitutionGeography = "CrossJoin([Extending Agency].[Name].[__INSTITUTION__], Filter({{[Country].[Name].Members}}, " +
            "(Exists(Ancestor([Country].CurrentMember, [Country].[Geography]), {[Country].[__GEOGRAPHY__]}).Count  > 0)))";

    private String institutionChartRowSet = "{Hierarchize({[Extending Agency].[Name].Members})}";
    private String institutionChartInstitution = "{Hierarchize({[Extending Agency].[Name].[__INSTITUTION__]})}";

    private String countryCurrency = "$";

    private int tableYear;
    // variables that holds the parameters received from filter
    private String currencyParam;
    private String geographyParam;
    private String recipientParam;
    private String institutionParam;
    private String yearParam;
    private String coFinancingParam;

    @SpringBean
    QueryService CdaService;

    public ReportsCountryInstitutionDashboards(final PageParameters parameters) {
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
        if(!parameters.get(ReportsConstants.GEOGRAPHY_PARAM).equals(StringValue.valueOf((String) null))) {
            geographyParam = parameters.get(ReportsConstants.GEOGRAPHY_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.RECIPIENT_PARAM).equals(StringValue.valueOf((String) null))) {
            recipientParam = parameters.get(ReportsConstants.RECIPIENT_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.INSTITUTION_PARAM).equals(StringValue.valueOf((String) null))) {
            institutionParam = parameters.get(ReportsConstants.INSTITUTION_PARAM).toString();
        }
        if(!parameters.get(ReportsConstants.YEAR_PARAM).equals(StringValue.valueOf((String) null))) {
            yearParam = parameters.get(ReportsConstants.YEAR_PARAM).toString();
            tableYear = Integer.parseInt(yearParam);
        }
        if(!parameters.get(ReportsConstants.COFINANCING_PARAM).equals(StringValue.valueOf((String) null))) {
            coFinancingParam = parameters.get(ReportsConstants.COFINANCING_PARAM).toString();
        }

        /*
         * create the MDX queries based on the filters
         */
        if (recipientParam != null) {
            institutionTableRecipient = institutionTableRecipient.replaceAll("__RECIPIENT__", recipientParam);

            if (institutionParam != null) {
                institutionTableInstitutionRecipient = institutionTableInstitutionRecipient.replaceAll("__INSTITUTION__", institutionParam);
                institutionTableInstitutionRecipient = institutionTableInstitutionRecipient.replaceAll("__RECIPIENT__", "[" + recipientParam + "]");

                institutionTableRowSet = institutionTableInstitutionRecipient;
            } else {
                institutionTableRowSet = institutionTableRecipient;
            }
        } else {
            if (geographyParam != null) {
                institutionTableGeography = institutionTableGeography.replaceAll("__GEOGRAPHY__", geographyParam);

                if (institutionParam != null) {
                    institutionTableInstitutionGeography = institutionTableInstitutionGeography.replaceAll("__INSTITUTION__", institutionParam);
                    institutionTableInstitutionGeography = institutionTableInstitutionGeography.replaceAll("__GEOGRAPHY__", geographyParam);

                    institutionTableRowSet = institutionTableInstitutionGeography;
                } else {
                    institutionTableRowSet = institutionTableGeography;
                }
            } else {
                if (institutionParam != null) {
                    institutionTableInstitutionRecipient = institutionTableInstitutionRecipient.replaceAll("__INSTITUTION__", institutionParam);
                    institutionTableInstitutionRecipient = institutionTableInstitutionRecipient.replaceAll("__RECIPIENT__", "Members");

                    institutionTableRowSet = institutionTableInstitutionRecipient;

                    institutionChartInstitution = institutionChartInstitution.replaceAll("__INSTITUTION__", institutionParam);
                    institutionChartRowSet = institutionChartInstitution;

                }
            }
        }

        addComponents();
    }

    private void addComponents() {
        Label title = new Label("title", new StringResourceModel("reportscountryinstitutiondashboards.title", this, null, null));
        add(title);

        addInstitutionTable();
        addInstitutionChart();
    }

    private void addInstitutionTable () {
        Label title = new Label("institutionTableTitle", "Net Disbursement by institution - " + (tableYear - 1) + "-" + tableYear +
                " - " + countryCurrency + " - full amount");
        add(title);

        Table table = new Table(CdaService, "institutionTable", "institutionTableRows", "customDashboardsInstitutionTable") {
            @Override
            public ListView<String[]> getTableRows () {
                super.getTableRows();

                this.rows = new ArrayList<>();
                this.result = this.runQuery();

                return ReportsDashboardsUtils.processTableRowsWithTotal(this.rows, this.result, this.rowId);
            }
        };

        // add MDX queries parameters
        table.setParam("paramFIRST_YEAR", Integer.toString(tableYear - 1));
        table.setParam("paramSECOND_YEAR", Integer.toString(tableYear));
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            table.setParam("paramCOFINANCED", "[1]");
        }
        if (currencyParam != null) {
            if (currencyParam.equals("true")) {
                table.setParam("paramcurrency", ReportsConstants.MDX_NAT_CURRENCY);
            }
        }

        table.setParam("paraminstitutionTableRowSet", institutionTableRowSet);

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

    private void addInstitutionChart () {
        Label title = new Label("institutionChartTitle", "Net Disbursement by institution - " + (tableYear - 1) + "-" + tableYear +
                " - " + countryCurrency + " - full amount");
        add(title);

        StackedBarChart stackedBarChart = new StackedBarChart(CdaService, "institutionChart", "customDashboardsInstitutionChart") {
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
        }
        if (geographyParam != null) {
            stackedBarChart.setParam("paramCOUNTRIES", "[" + geographyParam + "]");
        } else {
            if (recipientParam != null) {
                stackedBarChart.setParam("paramCOUNTRIES", "[Name].[" + recipientParam + "]");
            }
        }
        if(coFinancingParam != null && coFinancingParam.equals("true")) {
            stackedBarChart.setParam("paramCOFINANCED", "[1]");
        }

        stackedBarChart.setParam("paraminstitutionChartRowSet", institutionChartRowSet);

        List<List<Float>> resultSeries = stackedBarChart.getResultSeriesAsList();
        stackedBarChart.getOptions().setPlotOptions(new PlotOptionsChoice().
                setBar(new PlotOptions().
                        setMinPointLength(5).
                        setDataLabels(new DataLabels().
                                setEnabled(Boolean.TRUE))));
        stackedBarChart.getOptions().setTooltip(new Tooltip().setValueSuffix(" millions").setPercentageDecimals(2));
        // add 50px height for each row
        int numberOfRows = resultSeries.get(0).size();
        stackedBarChart.getOptions().getChartOptions().setHeight(300 + 50 * numberOfRows);

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
