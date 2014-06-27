
var displayStackedBarChart = function (parametersJson) {
    'use strict';

    // display the bar chart
    nv.addGraph(function () {
        var chart = nv.models.multiBarHorizontalChart()
            .x(function (data) {
                return data[0];
            })
            .y(function (data) {
                return data[1];
            })
            .tooltipContent(function(key, y, e, graph) {
                var formatValue = d3.format(',.2f');
                return y + '<br/>' + '<h4>' + key + ': $ million ' + formatValue(graph.point[2]) + '</h4>';
            })
            .showXAxis(false)
            .showValues(true)           // Show bar value next to each bar.
            .tooltips(true)             // Show tooltips on hover.
            .showLegend(true)
            .transitionDuration(500)
            .stacked(true)
            .showControls(false);        // Allow user to switch between "Grouped" and "Stacked" mode.

        chart.yAxis
            .axisLabel('Amount')
            .tickFormat(d3.format(".1p"));

        d3.select("#" + parametersJson.id + " svg")
            .datum(formatStackedBarResultSet(parametersJson.result.resultset))
            .transition().duration(500)
            .call(chart);

        nv.utils.windowResize(chart.update);

        return chart;
    });
}

/**
 * format the results for the nvd3 pie chart
 */
var formatStackedBarResultSet = function (result) {
    var i,
        data = [],
        myColors = ["#4572A7", "#AA4643", "#89A54E", "#80699B", "#3D96AE", "#DB843D", "#92A8CD", "#A47D7C", "#B5CA92", "#DDDF00", "#ED561B", "#6AF9C4"];

    var odaBySectorTotal = 0;

    for (i = 0; i < result.length; i++) {
        odaBySectorTotal += parseFloat(result[i][2], 10)  / 1000000;
    }

    for (i = 0; i < result.length; i++) {
        var newObj = {
            "key": result[i][0],
            "color": myColors[i % myColors.length],
            "values": [[result[i][1], parseFloat(result[i][2], 10)  / 1000000 / odaBySectorTotal, parseFloat(result[i][2], 10)  / 1000000]]
        }

        data.push(newObj);
    }

    return data;
}
