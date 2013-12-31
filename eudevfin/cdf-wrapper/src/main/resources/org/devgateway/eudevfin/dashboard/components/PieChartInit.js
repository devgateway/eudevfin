
var pieChart,
	pieQueryResult;	// we should parameterized this

function initPieChart(pieChartId, dataAccessId) {	
	var pieChartDefinition = new app.ChartPieDefinitionModel();
//	pieChartDefinition.get('chart').renderTo = 'pie-chart';
	pieChartDefinition.get('chart').renderTo = pieChartId;
	pieChartDefinition.get('plotOptions').series.events.click = function (event) {
	    setTimeout(function () {
	        console.log('pie chart clicked!');
	    }, 500);
	};
	
	var pieChartQuery = new app.ChartModel({
//	    name: "pieChartQuery",
	    name: pieChartId,
	    listeners: ['sectorListParameter'],
	    parameters: [],
	    resultvar: "pieQueryResult",
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
	        for (var i = 0; i < pieQueryResult.length && i < 10; i++) {
	            // filter the results
	            pieQueryResult[i].splice(0, 1);
	            // we need to do this because Highcharts needs 'int' instead of 'string' :(
	            // pieQueryResult[i][1] = parseInt(pieQueryResult[i][1], 10);
	            pieQueryResult[i][1] = Math.floor(Math.random() * 1000);

	            resultSeries.push(pieQueryResult[i]);
	        }

	        pieChartDefinition.get('series')[0].data = resultSeries;
	        pieChart = new Highcharts.Chart(pieChartDefinition.toJSON());
	    }
	});

    pieChartQuery = pieChartQuery.toJSON();

    Dashboards.addComponent(pieChartQuery);

//    Dashboards.update(pieChartQuery);
}