
var odaByRegionChart,
	odaByRegionQueryResult,
	odaByIncomeGroupChart,
	odaByIncomeGroupQueryResult
	MILLION = 1000000;

function addOdaByIncomeGroupChart(parametersJson) {
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
		resultvar: "odaByIncomeGroupQueryResult",
		preExecution: function () {
			// do nothing
		},
		postExecution: function () {
			var resultSeries = [];
			for (var i = 0; i < odaByIncomeGroupQueryResult.length; i++) {
				var tmpArray = [];
				tmpArray.push(odaByIncomeGroupQueryResult[i][0]);
				tmpArray.push(parseFloat(sprintf('%.3f', odaByIncomeGroupQueryResult[i][1] / MILLION)));
				resultSeries.push(tmpArray);
			}

			if (_.isEmpty(odaByIncomeGroupQueryResult)) {
				pieChartDefinition.get('chart').height = 100;
				app.noData();
			} else {
				pieChartDefinition.get('chart').height = 200;
			}

            pieChartDefinition.get('tooltip').formatter = function () {
                var value = '$ ' + sprintf('%.3f', this.y).replace(/,/g, " ");

                return '<b>' + this.point.name + '</b><br />' + value;
            }

			pieChartDefinition.get('series')[0].data = resultSeries;
			odaByIncomeGroupChart = new Highcharts.Chart(pieChartDefinition.toJSON());
		}
	}));

	pieChartQuery = pieChartQuery.toJSON();
	app.addComponent(pieChartQuery);
}

function addOdaByRegionChart(parametersJson) {
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
		resultvar: "odaByRegionQueryResult",
		preExecution: function () {
			// do nothing
		},
		postExecution: function () {
			var resultSeries = [];
			for (var i = 0; i < odaByRegionQueryResult.length; i++) {
				var tmpArray = [];
				tmpArray.push(odaByRegionQueryResult[i][0]);
                tmpArray.push(parseFloat(sprintf('%.3f', odaByRegionQueryResult[i][1] / MILLION)));
				resultSeries.push(tmpArray);
			}

			if (_.isEmpty(odaByRegionQueryResult)) {
				pieChartDefinition.get('chart').height = 100;
				app.noData();
			}

            pieChartDefinition.get('tooltip').formatter = function () {
                var value = '$ ' + sprintf('%.3f', this.y).replace(/,/g, " ");

                return '<b>' + this.point.name + '</b><br />' + value;
            }

			pieChartDefinition.get('series')[0].data = resultSeries;
			odaByRegionChart = new Highcharts.Chart(pieChartDefinition.toJSON());
		}
	}));

	pieChartQuery = pieChartQuery.toJSON();
	app.addComponent(pieChartQuery);
}