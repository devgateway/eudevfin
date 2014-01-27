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
 * Columns chart dashboard implementation
 */
public class ColumnsChart extends Panel implements IParametersProvider {
	private final WebMarkupContainer columnsChart;
	private ChartParameters parameters;
	
	public ColumnsChart(String id, String dataAccessId, String messageKey) {
        super(id);

        Label title = new Label("title", new StringResourceModel(messageKey, this, null, null));
        add(title);

        columnsChart = new WebMarkupContainer("columnsChart");
        columnsChart.setOutputMarkupId(true);
        add(columnsChart);

		String columnsChartId = columnsChart.getMarkupId();
		parameters = new ChartParameters(id, columnsChartId);

		parameters.getQueryDefinition().setDataAccessId(dataAccessId);
    }
	
	@Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ColumnsChart.class, "ColumnsChartInit.js")));
		response.render(OnDomReadyHeaderItem.forScript("initColumnsChart(" + parameters().toJson() + ");"));
    }

	@Override
	public BaseParameters parameters() {
		return parameters;
	}
}
