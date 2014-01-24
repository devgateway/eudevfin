
var stackedBarChart,
	stackedBarQueryResult;	// we should parameterized this

function initStackedBarChart(parametersJson) {
	var stackedBarChartId = parametersJson.htmlObject;
	var stackedBarChartDefinition = new app.StackedBarDefinitionModel();
	stackedBarChartDefinition.get('chart').renderTo = stackedBarChartId;

	var stackedBarChartQuery = new app.ChartModel(_.extend(parametersJson, {
	    resultvar: "stackedBarQueryResult",
	    preExecution: function () {
	        // do nothing
	    },
	    postExecution: function () {
	        var resultSeries = [];

	        stackedBarChart = new Highcharts.Chart(stackedBarChartDefinition.toJSON());
	    }
	}));

	stackedBarChartQuery = stackedBarChartQuery.toJSON();

    Dashboards.addComponent(stackedBarChartQuery);

    Dashboards.update(stackedBarChartQuery);
}