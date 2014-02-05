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
public class PieChart extends Panel implements IParametersProvider {
	private final WebMarkupContainer pieChart;
	private ChartParameters parameters;
	private String initFunction;
	
	public PieChart(String id, String dataAccessId, String messageKey) {
        super(id);
        
        Label title = new Label("title", new StringResourceModel(messageKey, this, null, null));
        add(title);

        pieChart = new WebMarkupContainer("pieChart");
        pieChart.setOutputMarkupId(true);
        add(pieChart);

		String pieChartId = pieChart.getMarkupId();
		parameters = new ChartParameters(id, pieChartId);

		parameters.getQueryDefinition().setDataAccessId(dataAccessId);
    }
	
	@Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(PieChart.class, "PieChartInit.js")));
		response.render(OnDomReadyHeaderItem.forScript(this.initFunction + "(" + parameters().toJson() + ");"));
    }

	@Override
	public BaseParameters parameters() {
		return parameters;
	}

	public ChartParameters getParameters() {
		return parameters;
	}

	public void setParameters(ChartParameters parameters) {
		this.parameters = parameters;
	}

	public String getInitFunction() {
		return initFunction;
	}

	public void setInitFunction(String initFunction) {
		this.initFunction = initFunction;
	}
}
