
var columnChart,
	columnQueryResult;	// we should parameterized this

function initColumnsChart(columnChartId, dataAccessId) {		
	var columnsChartDefinition = new app.ChartColumnDefinitionModel();
	columnsChartDefinition.get('chart').renderTo = columnChartId;
	columnsChartDefinition.get('series')[0].pointWidth = 115;
	columnsChartDefinition.get('xAxis').labels.rotation = -15;
	columnsChartDefinition.get('plotOptions').series.events.click = function (event) {
	    setTimeout(function () {
	        console.log('column chart clicked!');
	    }, 200);
	};
	
	var columnChartQuery = new app.ChartModel({
	    name: columnChartId,
	    parameters: [],
	    resultvar: "columnQueryResult",
	    queryDefinition: {
	        dataAccessId: "simpleSQLQuery",
	        path: '/some/path'
	    },
	    executeAtStart: true,
	    preExecution: function () {
	        // do nothing
	    },
	    postExecution: function () {
	        var resultCategories = [],
	            resultSeries = [],
	            colors = Highcharts.getOptions().colors,
	            len = Highcharts.getOptions().colors.length,
	            i;

	        for (i = 0; i < columnQueryResult.length && i < 8; i++) {
	            if (parseInt(columnQueryResult[i][2], 10) > 0) {
	                resultCategories.push(columnQueryResult[i][1]);
	                resultSeries.push({
	                    name: columnQueryResult[i][1],
	                    y: Math.floor(Math.random() * 1000),
	                    // y: parseInt(columnQueryResult[i][2], 10), 
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
	});

	columnChartQuery = columnChartQuery.toJSON();

    Dashboards.addComponent(columnChartQuery);

    Dashboards.update(columnChartQuery);
}