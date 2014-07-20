/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
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
public class PieChartNVD3 extends Panel implements IParametersProvider {
    private final WebMarkupContainer pieChart;

    RunMdxQuery runMdxQuery;

    private String pieChartId;
    private String initFunction;
    private BaseParameters parameters;

    public PieChartNVD3 (QueryService CdaService, String chartId, String dataAccessId) {
        super(chartId);

        runMdxQuery = new RunMdxQuery(CdaService);
        this.setParam("dataAccessId", dataAccessId);

        initFunction = "displayPieChart";

        pieChart = new WebMarkupContainer("pieChart");
        pieChart.setOutputMarkupId(true);
        add(pieChart);

        pieChartId = pieChart.getMarkupId();
        parameters = new BaseParameters(pieChartId);
    }

    public void setParam (String key, String value) {
        runMdxQuery.setParam(key, value);
    }

    public BaseParameters parameters() {
        if (parameters == null) {
            parameters = new BaseParameters(pieChartId, runMdxQuery.runQuery());
        } else {
            if(parameters.getResult() == null) {
                parameters.setResult(runMdxQuery.runQuery());
            }
        }

        return parameters;
    }

    public void setUseMillion(Boolean useMillion) {
        parameters.setUseMillion(useMillion);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new CssResourceReference(Dashboards.class,
                "nvd3/nv.d3.css")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "nvd3/d3.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Dashboards.class, "nvd3/nv.d3.min.js")));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(PieChartNVD3.class, "PieChartNVD3.js")));
        response.render(OnDomReadyHeaderItem.forScript(this.initFunction + "(" + parameters().toJson() + ");"));
    }

    public void setInitFunction(String initFunction) {
        this.initFunction = initFunction;
    }
}
