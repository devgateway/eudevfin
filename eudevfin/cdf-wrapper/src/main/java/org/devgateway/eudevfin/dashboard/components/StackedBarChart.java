package org.devgateway.eudevfin.dashboard.components;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Stacked bar chart dashboard implementation
 */
public class StackedBarChart extends Panel {
	private final WebMarkupContainer stackedBarChart;
	private String dataAccessId;
	
	public StackedBarChart(String id, String dataAccessId, String messageKey) {
        super(id);
        this.dataAccessId = dataAccessId;
        
        Label title = new Label("title", new StringResourceModel(messageKey, this, null, null));
        add(title);

        stackedBarChart = new WebMarkupContainer("stackedBarChart");
        stackedBarChart.setOutputMarkupId(true);
        add(stackedBarChart);
    }
	
	@Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(StackedBarChart.class, "StackedBarChartInit.js")));
        response.render(OnDomReadyHeaderItem.forScript("initStackedBarChart('" + stackedBarChart.getMarkupId() + "', '" + dataAccessId + "');"));
    }
}
