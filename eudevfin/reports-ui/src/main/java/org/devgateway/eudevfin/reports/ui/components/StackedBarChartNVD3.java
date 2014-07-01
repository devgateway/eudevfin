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
 * @since 6/19/14
 */
public class StackedBarChartNVD3 extends Panel implements IParametersProvider {
    private final WebMarkupContainer stackedBarChart;

    RunMdxQuery runMdxQuery;

    private String stackedBarChartId;
    private String initFunction;
    private BaseParameters parameters;

    public StackedBarChartNVD3 (QueryService CdaService, String chartId, String dataAccessId) {
        super(chartId);

        runMdxQuery = new RunMdxQuery(CdaService);
        this.setParam("dataAccessId", dataAccessId);

        initFunction = "displayStackedBarChart";

        stackedBarChart = new WebMarkupContainer("stackedBarChart");
        stackedBarChart.setOutputMarkupId(true);
        add(stackedBarChart);

        stackedBarChartId = stackedBarChart.getMarkupId();
        parameters = new BaseParameters(stackedBarChartId);
    }

    public void setParam (String key, String value) {
        runMdxQuery.setParam(key, value);
    }

    public BaseParameters parameters() {
        if (parameters == null) {
            parameters = new BaseParameters(stackedBarChartId, runMdxQuery.runQuery());
        } else {
            if(parameters.getResult() == null) {
                parameters.setResult(runMdxQuery.runQuery());
            }
        }

        return parameters;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new CssResourceReference(Dashboards.class,
                "nvd3/nv.d3.css")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "nvd3/d3.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "nvd3/nv.d3.min.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(StackedBarChartNVD3.class, "StackedBarChartNVD3.js")));
        response.render(OnDomReadyHeaderItem.forScript(this.initFunction + "(" + parameters().toJson() + ");"));
    }

    public void setInitFunction(String initFunction) {
        this.initFunction = initFunction;
    }
}
