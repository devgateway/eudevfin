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
 * Pie chart dashboard implementation
 */
public class PieChart extends Panel {
	private final WebMarkupContainer pieChart;
	private String dataAccessId;
	
	public PieChart(String id, String dataAccessId, String messageKey) {
        super(id);
        this.dataAccessId = dataAccessId;
        
        Label title = new Label("title", new StringResourceModel(messageKey, this, null, null));
        add(title);

        pieChart = new WebMarkupContainer("pieChart");
        pieChart.setOutputMarkupId(true);
        add(pieChart);
    }
	
	@Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(PieChart.class, "PieChartInit.js")));
        response.render(OnDomReadyHeaderItem.forScript("initPieChart('" + pieChart.getMarkupId() + "', '" + dataAccessId + "');"));
    }
}
