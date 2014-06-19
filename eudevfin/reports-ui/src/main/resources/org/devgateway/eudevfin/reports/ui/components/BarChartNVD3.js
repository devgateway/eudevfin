var myBarColors = ["#3D96AE", "#AA4643"];

var displayBarChart = function (parametersJson) {
    'use strict';

    var numberOfRows = parametersJson.result.resultset.length;
    var height = 300 + numberOfRows * 35;

    // display the bar chart
    nv.addGraph(function () {
        var chart = nv.models.multiBarHorizontalChart()
            .x(function (data) {
                return data[0];
            })
            .y(function (data) {
                return data[1];
            })
            .height(height)             // set the height
            .margin({left: 70})
            .showValues(true)           // Show bar value next to each bar.
            .tooltips(true)             // Show tooltips on hover.
            .showLegend(true)
            .transitionDuration(500)
            .showControls(parametersJson.numberOfSeries === 1 ? false : true); // Allow user to switch between "Grouped" and "Stacked" mode.

        chart.yAxis
            .axisLabel('Amount')
            .tickFormat(d3.format(',.2f'));

        d3.select("#" + parametersJson.id + " svg")
            .datum(formatBarResultSet(parametersJson.result.resultset, parametersJson.numberOfSeries,
                parametersJson.Series1, parametersJson.Series2))
            .transition().duration(500)
            .call(chart);

        // update the height of the svg
        $("#" + parametersJson.id + " svg").height(height);

        nv.utils.windowResize(chart.update);

        return chart;
    });
}

/**
 * format the results for the nvd3 bar chart
 */
var formatBarResultSet = function (result, numberOfSeries, Series1, Series2) {
    var i,
        data;

    if(numberOfSeries == 1) {
        data = [{
            "key": Series1,
            "color": "#3D96AE",
            "values": []
            }
        ];
    } else {
        if(numberOfSeries == 2) {
            data = [{
                "key": Series1,
                "color": "#3D96AE",
                "values": []
                },
                {
                    "key": Series2,
                    "color": "#AA4643",
                    "values": []
                }
            ];
        }
    }

    for (i = 0; i < result.length; i++) {
        // add data for the first series
        data[0].values.push([result[i][0], parseFloat(result[i][1], 10)]);

        if(numberOfSeries == 2) {
            data[1].values.push([result[i][0], parseFloat(result[i][2], 10)]);
        }
    }

    return data;
}
