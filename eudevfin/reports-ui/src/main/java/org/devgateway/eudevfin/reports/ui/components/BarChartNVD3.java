
package org.devgateway.eudevfin.reports.ui.components;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.devgateway.eudevfin.reports.core.service.QueryService;
import org.devgateway.eudevfin.reports.ui.scripts.Dashboards;

/**
 * Pie chart that uses the NVD3 library (http://nvd3.org/)
 *
 * @author idobre
 * @since 6/17/14
 */
public class BarChartNVD3 extends Panel implements IParametersProvider {
    private final WebMarkupContainer barChart;

    RunMdxQuery runMdxQuery;

    private String barChartId;
    private String initFunction;
    private BaseParameters parameters;

    public BarChartNVD3 (QueryService CdaService, String chartId, String dataAccessId) {
        super(chartId);

        runMdxQuery = new RunMdxQuery(CdaService);
        this.setParam("dataAccessId", dataAccessId);

        initFunction = "displayBarChart";

        barChart = new WebMarkupContainer("barChart");
        barChart.setOutputMarkupId(true);
        add(barChart);

        barChartId = barChart.getMarkupId();
        parameters = new BaseParameters(barChartId);
    }

    public void setParam (String key, String value) {
        runMdxQuery.setParam(key, value);
    }

    public BaseParameters parameters() {
        if (parameters == null) {
            parameters = new BaseParameters(barChartId, runMdxQuery.runQuery());
        } else {
            if(parameters.getResult() == null) {
                parameters.setResult(runMdxQuery.runQuery());
            }
        }

        return parameters;
    }

    public void setSeries2(String series2) {
        parameters.setSeries2(series2);
    }

    public void setSeries1(String series1) {
        parameters.setSeries1(series1);
    }

    public void setNumberOfSeries(int numberOfSeries) {
        parameters.setNumberOfSeries(numberOfSeries);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new CssResourceReference(Dashboards.class,
                "nvd3/nv.d3.css")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "nvd3/d3.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "nvd3/nv.d3.min.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(PieChartNVD3.class, "BarChartNVD3.js")));
        response.render(OnDomReadyHeaderItem.forScript(this.initFunction + "(" + parameters().toJson() + ");"));
    }

    public void setInitFunction(String initFunction) {
        this.initFunction = initFunction;
    }
}

