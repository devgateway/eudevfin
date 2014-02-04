
var odaBySectorChart,
	odaBySectorQueryResult,
	MILLION = 1000000;

function addOdaBySectorChart(parametersJson) {
	'use strict';

	var stackedBarChartId = parametersJson.htmlObject;
	var stackedBarChartDefinition = new app.StackedBarDefinitionModel();
	stackedBarChartDefinition.get('chart').renderTo = stackedBarChartId;

	var stackedBarChartQuery = new app.ChartModel(_.extend(parametersJson, {
	    resultvar: "odaBySectorQueryResult",
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

		    len = odaBySectorQueryResult.length;
		    for (i = 0; i < len; i++) {
				resultCategories.push(odaBySectorQueryResult[i][1]);
				if(resultSeries[odaBySectorQueryResult[i][0]] === undefined) {
					resultSeries[odaBySectorQueryResult[i][0]] = [];
				}

				if (odaBySectorQueryResult[i][2] !== null && odaBySectorQueryResult[i][2] != undefined) {
					resultSeries[odaBySectorQueryResult[i][0]].push(parseFloat(odaBySectorQueryResult[i][2]) / MILLION);
				} else {
					resultSeries[odaBySectorQueryResult[i][0]].push(0);
				}
		    }

		    resultCategories = _.uniq(resultCategories, false);

		    for (key in resultSeries) {
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

		    odaBySectorChart = new Highcharts.Chart(stackedBarChartDefinition.toJSON());
	    }
	}));

	stackedBarChartQuery = stackedBarChartQuery.toJSON();
	app.addComponent(stackedBarChartQuery);
}