
var stackedBarChart,
	stackedBarQueryResult;	// we should parameterized this

function initStackedBarChart(parametersJson) {
	'use strict';

	var stackedBarChartId = parametersJson.htmlObject;
	var stackedBarChartDefinition = new app.StackedBarDefinitionModel();
	stackedBarChartDefinition.get('chart').renderTo = stackedBarChartId;

	var stackedBarChartQuery = new app.ChartModel(_.extend(parametersJson, {
	    resultvar: "stackedBarQueryResult",
		postFetch: function (values) {
			return values;
		},
	    preExecution: function () {
	        // do nothing
	    },
	    postExecution: function () {
	        var resultSeries = {},
		        finalResultSeries = [],
		        resultCategories = [],
		        newObject,
		        key,
		        len,
		        i;

			len = stackedBarQueryResult.length;
			for (i = 0; i < len; i++) {
				resultCategories.push(stackedBarQueryResult[i][5]);
				if(resultSeries[stackedBarQueryResult[i][2]] === undefined) {
					resultSeries[stackedBarQueryResult[i][2]] = [];
				}

				if (stackedBarQueryResult[i][6] !== null && stackedBarQueryResult[i][6] != undefined) {
					resultSeries[stackedBarQueryResult[i][2]].push(parseInt(stackedBarQueryResult[i][6], 10));
				} else {
					resultSeries[stackedBarQueryResult[i][2]].push(0);
				}
			}

		    resultCategories = _.uniq(resultCategories, false);

		    for (var key in resultSeries) {
			    if (resultSeries.hasOwnProperty(key)) {
					// add only non-null values
				    if (app.checkArrayValue(resultSeries[key], 0) === false) {
					    newObject = {};
					    newObject.name = key;
					    newObject.data = resultSeries[key];

					    finalResultSeries.push(newObject);
				    }
			    }
		    }

		    stackedBarChartDefinition.get('xAxis').categories = resultCategories;
		    stackedBarChartDefinition.set({series: finalResultSeries});

	        stackedBarChart = new Highcharts.Chart(stackedBarChartDefinition.toJSON());
	    }
	}));

	stackedBarChartQuery = stackedBarChartQuery.toJSON();
	app.addComponent(stackedBarChartQuery);
}