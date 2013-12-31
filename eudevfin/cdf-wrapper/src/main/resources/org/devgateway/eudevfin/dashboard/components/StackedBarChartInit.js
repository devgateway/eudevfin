
var stackedBarChart,
	stackedBarQueryResult;	// we should parameterized this

function initStackedBarChart(stackedBarChartId, dataAccessId) {	
	var stackedBarChartDefinition = new app.StackedBarDefinitionModel();
	stackedBarChartDefinition.get('chart').renderTo = stackedBarChartId;

	var stackedBarChartQuery = new app.ChartModel({
	    name: stackedBarChartId,
	    parameters: [],
	    resultvar: "stackedBarQueryResult",
	    queryDefinition: {
	        dataAccessId: "simpleSQLQuery",
	        path: '/some/path'
	    },
	    executeAtStart: true,
	    preExecution: function () {
	        // do nothing
	    },
	    postExecution: function () {
	        var resultSeries = [];

	        stackedBarChart = new Highcharts.Chart(stackedBarChartDefinition.toJSON());
	    }
	});

	stackedBarChartQuery = stackedBarChartQuery.toJSON();

    Dashboards.addComponent(stackedBarChartQuery);

//    Dashboards.update(stackedBarChartQuery);
}