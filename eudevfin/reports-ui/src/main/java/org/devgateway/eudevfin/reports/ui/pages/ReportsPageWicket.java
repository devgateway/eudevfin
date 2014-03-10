package org.devgateway.eudevfin.reports.ui.pages;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.functions.DefaultFormatter;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.eudevfin.auth.common.domain.AuthConstants;
import org.devgateway.eudevfin.auth.common.util.AuthUtils;
import org.devgateway.eudevfin.metadata.common.domain.Organization;
import org.devgateway.eudevfin.reports.core.domain.QueryResult;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.scripts.Dashboards;
import org.devgateway.eudevfin.ui.common.pages.HeaderFooter;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.datatables.DemoDatatable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by idobre on 3/5/14.
 */

@MountPath(value = "/reportswicket")
@AuthorizeInstantiation(AuthConstants.Roles.ROLE_USER)
public class ReportsPageWicket extends HeaderFooter {
    private static final Logger logger = Logger.getLogger(ReportsPageWicket.class);

    @SpringBean
    QueryService CdaService;

    public ReportsPageWicket () {
        addComponents();
    }

    private void addComponents () {
        addReportingCountry();

        Map<String, String> params2 = new HashMap<>();
        params2.put("dataAccessId", "netODATable");

        QueryResult result2 = CdaService.doQuery(params2);

        DataTable table = new DataTable("netODADashboard");

        add(table);

        List<String[]> rows = new ArrayList<>();

        for (List<String> item : result2.getResultset()) {
            rows.add(item.toArray(new String[item.size()]));
        }

        ListView<String[]> lv = new ListView<String[]>("rows", rows) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                for(int i = 0; i < row.length; i++) {
                    if(row[i] == null) {
                        row[i] = "0";
                    }
                }

                item.add(new Label("col1", row[0]));
                item.add(new Label("col2", row[1]));
                item.add(new Label("col3", row[2]));
            }
        };

        table.add(lv);





        Map<String, String> params3 = new HashMap<>();
        params3.put("dataAccessId", "topTenRecipients");

        QueryResult result3 = CdaService.doQuery(params3);

        DataTable table2 = new DataTable("topTenRecipients");

        add(table2);

        List<String[]> rows2 = new ArrayList<>();

        for (List<String> item : result3.getResultset()) {
            rows2.add(item.toArray(new String[item.size()]));
        }

        ListView<String[]> lv2 = new ListView<String[]>("rows2", rows2) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                item.add(new Label("col1", row[0]));
                item.add(new Label("col2", row[1]));
            }
        };

        table2.add(lv2);



        Map<String, String> params4 = new HashMap<>();
        params4.put("dataAccessId", "topTenMemoShare");

        QueryResult result4 = CdaService.doQuery(params4);

        DataTable table3 = new DataTable("topTenMemoShare");

        add(table3);

        List<String[]> rows3 = new ArrayList<>();

        for (List<String> item : result4.getResultset()) {
            String[] newItem = new String[2];

            if (item.get(0) != null) {
                newItem[0] = "Top 5 recipients";
                newItem[1] = item.get(0);
            }

            rows3.add(newItem);
        }

        ListView<String[]> lv3 = new ListView<String[]>("rows3", rows3) {
            @Override
            protected void populateItem(ListItem<String[]> item) {
                String[] row = item.getModelObject();

                item.add(new Label("col1", row[0]));
                item.add(new Label("col2", row[1]));
            }
        };

        table3.add(lv3);




        Map<String, String> params = new HashMap<>();
        params.put("dataAccessId", "odaByIncomeGroupChart");

        QueryResult result = CdaService.doQuery(params);

        List<Point> resultSeries = new ArrayList<>();
        for (List<String> item : result.getResultset()) {
            resultSeries.add(new Point(item.get(0), Float.parseFloat(item.get(1)) / 1000000));
        }

        Options options = new Options();

        options.setTitle(new Title("ODA By Income Group Chart"));

        options.setChartOptions(new ChartOptions()
                .setAnimation(Boolean.TRUE)
                .setBorderColor(new HexColor("#DEEDF7"))
                .setBorderRadius(20)
                .setBorderWidth(2)
                .setHeight(250)
                .setMarginLeft(null)
                .setMarginRight(null)
                .setMarginTop(null)
                .setMarginBottom(null)
                .setPlotShadow(Boolean.FALSE)
                .setType(SeriesType.PIE));

        options.setCreditOptions(new CreditOptions().setEnabled(Boolean.FALSE));

        options.setExporting(new ExportingOptions().setEnabled(Boolean.TRUE));

        options.setLegend(new Legend()
                .setAlign(HorizontalAlignment.RIGHT)
                .setVerticalAlign(VerticalAlignment.TOP)
                .setBackgroundColor(new HexColor("#FCFFC5"))
                .setBorderColor(new HexColor("#C98657"))
                .setEnabled(Boolean.TRUE)
                .setFloating(Boolean.FALSE)
                .setItemStyle(new CssStyle()
                        .setProperty("lineHeight", "14px")
                        .setProperty("width", "450"))
                .setLayout(LegendLayout.VERTICAL)
                .setX(0)
                .setY(50));

        options.setTooltip(new Tooltip()
                .setBorderWidth(1)
                .setStyle(new CssStyle()
                        .setProperty("padding", "10"))
                .setPercentageDecimals(2)
                .setFormatter(new DefaultFormatter().setFunction("var value = '$ ' + sprintf('%.3f', this.y).replace(/,/g, \" \");\n" +
                        "return '<b>' + this.point.name + '</b><br />' + value;"))
                .setShared(Boolean.FALSE)
                .setUseHTML(Boolean.TRUE));

        options.setPlotOptions(new PlotOptionsChoice()
                .setPie(new PlotOptions()
                        .setAllowPointSelect(Boolean.TRUE)
                        .setCursor(Cursor.POINTER)
                        .setAnimation(Boolean.TRUE)
                        .setDataLabels(new DataLabels()
                                .setEnabled(Boolean.TRUE)
                                .setColor(new HexColor("#000000"))
                                .setConnectorColor(new HexColor("#000000")))
                        .setShowInLegend(Boolean.TRUE)));

        options.addSeries(new PointSeries()
                .setType(SeriesType.PIE)
                .setData(resultSeries));

        add(new Chart("odaByIncomeGroupChart", options));







        Map<String, String> params1 = new HashMap<>();
        params.put("dataAccessId", "odaBySectorChart");

        QueryResult result1 = CdaService.doQuery(params);
        float odaBySectorTotal = 0;

        Map<String, Float> resultSeries1 = new HashMap<>();
        Set<String> resultCategories = new HashSet<>();

        for (List<String> item : result1.getResultset()) {
            // keep uniq values
            resultCategories.add(item.get(1));

            resultSeries1.put(item.get(0), Float.parseFloat(item.get(2)) / 1000000);

            odaBySectorTotal += (Float.parseFloat(item.get(2)) / 1000000);
        }

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        odaBySectorTotal = Float.valueOf(twoDForm.format(odaBySectorTotal));

        Options options1 = new Options();

        options1.setTitle(new Title("ODA By Sector"));

        options1.setChartOptions(new ChartOptions()
                .setAnimation(Boolean.TRUE)
                .setBorderColor(new HexColor("#DEEDF7"))
                .setBorderRadius(20)
                .setBorderWidth(2)
                .setHeight(350)
                .setMarginLeft(null)
                .setMarginRight(null)
                .setMarginTop(null)
                .setMarginBottom(null)
                .setPlotShadow(Boolean.FALSE)
                .setType(SeriesType.BAR));

        options1.setCreditOptions(new CreditOptions().setEnabled(Boolean.FALSE));

        options1.setExporting(new ExportingOptions().setEnabled(Boolean.TRUE));

        options1.setxAxis(new Axis()
                .setLabels(new Labels()
                        .setRotation(-15)
                        .setAlign(HorizontalAlignment.RIGHT)
                        .setStyle(new CssStyle()
                                .setProperty("fontSize", "13px")
                                .setProperty("fontFamily", "Verdana, sans-serif")))
                .setCategories(new ArrayList<>(resultCategories)));

        options1.setyAxis(new Axis()
                .setMin(0)
                .setTitle(new Title("Amount"))
                .setMax(odaBySectorTotal)
                .setTickInterval(odaBySectorTotal / 10)
                .setLabels(new Labels()
                        .setFormatter(new DefaultFormatter().setFunction("return sprintf('%d', (this.value / " + odaBySectorTotal + ") * 100).replace(/,/g, \" \") + '%';"))));

        options1.setTooltip(new Tooltip()
                .setBorderWidth(1)
                .setPercentageDecimals(2)
                .setFormatter(new DefaultFormatter().setFunction("var value = '$ ' + sprintf('%.3f', this.y).replace(/,/g, \" \");\n" +
                        "return this.point.category + '<br />' + '<b>' + this.point.series.name + '</b>: ' + value;"))
                .setShared(Boolean.FALSE)
                .setUseHTML(Boolean.TRUE));

        options1.setLegend(new Legend()
                .setBackgroundColor(new HexColor("#FFFFFF"))
                .setReversed(Boolean.TRUE));

        options1.setPlotOptions(new PlotOptionsChoice()
                .setSeries(new PlotOptions()
                        .setStacking(Stacking.NORMAL)
                        .setCursor(Cursor.POINTER)));

        for (String key : resultSeries1.keySet()) {
            options1.addSeries(new SimpleSeries()
                    .setName(key)
                    .setData(Arrays.asList(new Number[]{resultSeries1.get(key)})));
        }

        add(new Chart("odaBySectorChart", options1));
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

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        // load CDF plugin for some functions - we can remove it latter and add only those functions
        response.render(JavaScriptHeaderItem.forUrl("/js/cdfplugin.min.js"));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "reports.js")));
    }
}

class DataTable extends DemoDatatable {

    public DataTable(String id) {
        super(id);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        renderDemoCSS(response);
        renderBasicJS(response);

        StringBuilder js = new StringBuilder();
        js.append("$(document).ready( function() {\n");
        js.append("	$('#" + getMarkupId() + "').dataTable( {\n");
        js.append("		\"bJQueryUI\": true,\n");
        js.append("		\"bPaginate\": false,\n");
        js.append("		\"bFilter\": false,\n");
        js.append("		\"bInfo\": false,\n");
        js.append("		\"bSort\": false,\n");
        js.append("		\"bLengthChange\": false,\n");
        js.append("		\"iDisplayLength\": 10,\n");
        js.append("		\"sPaginationType\": \"full_numbers\"\n");
        js.append("	} );\n");
        js.append("} );");

        response.render(JavaScriptHeaderItem.forScript(js, getId() + "_datatables"));
    }

    private void renderDemoCSS(IHeaderResponse response) {
        final Class<DemoDatatable> _ = DemoDatatable.class;
        response.render(CssHeaderItem.forReference(new PackageResourceReference(_,
                "media/css/demo_table_jui.css"), "screen"));
    }

    private String getJUITheme() {
        return "smoothness";
    }

    private void renderBasicJS(IHeaderResponse response) {
        final Class<DemoDatatable> _ = DemoDatatable.class;

//        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(_, "media/js/jquery.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(_,"media/js/jquery.dataTables.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(_,"media/js/jquery-ui-1.8.10.custom.min.js")));
    }
}
