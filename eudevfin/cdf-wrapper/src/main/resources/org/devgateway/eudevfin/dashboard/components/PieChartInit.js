
var pieChart,
	pieQueryResult;	// we should parameterized this

function initPieChart(parametersJson) {
	'use strict';

	var pieChartId = parametersJson.htmlObject;
	var pieChartDefinition = new app.ChartPieDefinitionModel();
	pieChartDefinition.get('chart').renderTo = pieChartId;
	pieChartDefinition.get('plotOptions').series.events.click = function (event) {
	    setTimeout(function () {
	        console.log('pie chart clicked!');
	    }, 500);
	};
	
	var pieChartQuery = new app.ChartModel(_.extend(parametersJson, {
		resultvar: "pieQueryResult",
	    preExecution: function () {
	        // do nothing
	    },
	    postExecution: function () {
	        var resultSeries = [];
		    for (var i = 0; i < pieQueryResult.length; i++) {
			    var tmpArray = [];
			    tmpArray.push(pieQueryResult[i][0]);
			    tmpArray.push( parseInt(pieQueryResult[i][1], 10));
			    resultSeries.push(tmpArray);
		    }

	        pieChartDefinition.get('series')[0].data = resultSeries;
	        pieChart = new Highcharts.Chart(pieChartDefinition.toJSON());
	    }
	}));

    pieChartQuery = pieChartQuery.toJSON();
	app.addComponent(pieChartQuery);
}