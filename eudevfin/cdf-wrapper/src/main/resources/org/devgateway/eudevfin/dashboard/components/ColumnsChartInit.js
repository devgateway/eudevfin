
var columnChart,
	columnQueryResult;	// we should parameterized this

function initColumnsChart(parametersJson) {
	'use strict';

	var columnChartId = parametersJson.htmlObject;
	var columnsChartDefinition = new app.ChartColumnDefinitionModel();
	columnsChartDefinition.get('chart').renderTo = columnChartId;
	columnsChartDefinition.get('series')[0].pointWidth = 115;
	columnsChartDefinition.get('xAxis').labels.rotation = -15;
	columnsChartDefinition.get('plotOptions').series.events.click = function (event) {
	    setTimeout(function () {
	        console.log('column chart clicked!');
	    }, 200);
	};
	
	var columnChartQuery = new app.ChartModel(_.extend(parametersJson, {
	    resultvar: "columnQueryResult",
	    preExecution: function () {
	        // do nothing
	    },
	    postExecution: function () {
		    var resultCategories = [],
	            resultSeries = [],
	            colors = Highcharts.getOptions().colors,
	            len = Highcharts.getOptions().colors.length,
	            i;

	        for (i = 0; i < columnQueryResult.length; i++) {
	            if (parseInt(columnQueryResult[i][3], 10) > 0) {
	                resultCategories.push(columnQueryResult[i][2]);
	                resultSeries.push({
	                    name: columnQueryResult[i][2],
	                    y: parseInt(columnQueryResult[i][3], 10),
	                    color: colors[i % len] // access colors array in a circular manner
	                });
	            }
	        }

	        if (resultCategories.length > 0) {
	            columnsChartDefinition.get('series')[0].data = resultSeries;
	            columnsChartDefinition.get('xAxis').categories = resultCategories;
	            columnChart = new Highcharts.Chart(columnsChartDefinition.toJSON());
	        }
	    }
	}));

	columnChartQuery = columnChartQuery.toJSON();
	app.addComponent(columnChartQuery);
}